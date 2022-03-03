package org.firstinspires.ftc.teamcode.pixelanalysis;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class CameraWrapper {
    
    public static final int kImageWidth = 640;
    public static final int kImageHeight = 480;
    private static final String TAG = "Image Processor";
    
    /**
     * How long we are to wait to be granted permission to use the camera before giving up. Here,
     * we wait indefinitely
     */
    private static final int secondsPermissionTimeout = Integer.MAX_VALUE;
    private final File captureDirectory = AppUtil.ROBOT_DATA_DIR;

    private final WebcamName cameraName;
    private CameraManager cameraManager;
    private Camera camera;
    private CameraCaptureSession cameraCaptureSession;
    private EvictingBlockingQueue<Bitmap> frameQueue;
    private Handler callbackHandler;

    public CameraWrapper(WebcamName cameraName) {
        this.cameraName = cameraName;
        AppUtil.getInstance().ensureDirectoryExists(captureDirectory);
    }

    public boolean initCamera() {
        if (camera != null || cameraCaptureSession != null) return true;
        callbackHandler = CallbackLooper.getDefault().getHandler();
        cameraManager = ClassFactory.getInstance().getCameraManager();
        initializeFrameQueue(2);
        // Opens the camera:
        camera = cameraManager.requestPermissionAndOpenCamera(new Deadline(Integer.MAX_VALUE, TimeUnit.SECONDS), cameraName, null);
        if (camera == null) return false;
        // Starts the camera:        
        CameraCharacteristics cameraCharacteristics = cameraName.getCameraCharacteristics();
        int imageFormat = ImageFormat.YUY2;
        if (!contains(cameraCharacteristics.getAndroidFormats(), imageFormat)) {
            RobotLog.ee(TAG, "Image format not supported");
            return false;
        }
        final Size size = new Size(kImageWidth, kImageHeight);
        final int fps = cameraCharacteristics.getMaxFramesPerSecond(imageFormat, size);
        final ContinuationSynchronizer<CameraCaptureSession> synchronizer = new ContinuationSynchronizer<>();
        try {
            camera.createCaptureSession(Continuation.create(callbackHandler,
                    new CameraCaptureSession.StateCallbackDefault() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            try {
                                final CameraCaptureRequest captureRequest = camera.createCaptureRequest(imageFormat, size, fps);
                                session.startCapture(captureRequest, (session1, request, cameraFrame) -> {
                                            Bitmap bmp = captureRequest.createEmptyBitmap();
                                            cameraFrame.copyToBitmap(bmp);
                                            frameQueue.offer(bmp);
                                        },
                                        Continuation.create(callbackHandler, (session12,
                                                cameraCaptureSequenceId, lastFrameNumber) -> RobotLog.ii(
                                                        TAG, "capture sequence %s reports completed: lastFrame=%d",
                                                        cameraCaptureSequenceId, lastFrameNumber)));
                                synchronizer.finish(session);
                            } catch (CameraException | RuntimeException e) {
                                RobotLog.ee(TAG, e, "exception starting capture");
                                session.close();
                                synchronizer.finish(null);
                            }
                        }
                    }));
        } catch (CameraException | RuntimeException e) {
            RobotLog.ee(TAG, e, "exception starting camera");
            synchronizer.finish(null);
        }
        try {
            synchronizer.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        cameraCaptureSession = synchronizer.getValue();
        return cameraCaptureSession != null;
    }

    public void stopCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.stopCapture();
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }
        if (camera != null) {
            camera.close();
            camera = null;
        }
    }

    public Bitmap takePicture() {
        return frameQueue.poll();
    }

    public boolean[][] extractObjects(ClipBound bound, ColorFilter filter, boolean debugMode) {
        Bitmap bitmap = takePicture();
        if (bitmap == null) return null;
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, bound.left, bound.top, bound.width, bound.height);
        if (debugMode) {
            saveImage("webcam-full.jpg", bitmap);
            saveImage("webcam-cropped.jpg", newBitmap);
        }
        bitmap.recycle();
        DebugImagesContainer debugImages = null;
        if (debugMode) {
            debugImages = new DebugImagesContainer(bound.width * bound.height);
        }
        boolean[][] objectArea = new boolean[bound.height][bound.width];
        for (int i = 0; i < bound.height; i++) {
            for (int j = 0; j < bound.width; j++) {
                HSV hsv = new HSV();
                int color = newBitmap.getPixel(j, i);
                int r = Color.red(color);
                int g = Color.green(color);
                int b = Color.blue(color);
                int cMax = max(r, g, b);
                int cMin = min(r, g, b);
                int diff = cMax - cMin;
                // Finds h:
                if (diff == 0) {
                    hsv.h = 0;
                } else if (cMax == r) {
                    hsv.h = (60 * (g - b) / diff + 360) % 360;
                } else if (cMax == g) {
                    hsv.h = (60 * (b - r) / diff + 120) % 360;
                } else {
                    hsv.h = (60 * (r - g) / diff + 240) % 360;
                }
                // Finds s:
                if (cMax == 0) {
                    hsv.s = 0;
                } else {
                    hsv.s = diff * 100 / cMax;
                }
                boolean keep = filter.filter(hsv);
                objectArea[i][j] = keep;
                if (debugImages != null) {
                    int index = i * bound.width + j;
                    if (hsv.s < 40) {
                        debugImages.color1[index] = 0;
                        debugImages.color2[index] = 0;
                    } else {
                        int hue = (int) hsv.h;
                        debugImages.color1[index] = hue | hue << 8 | hue << 16 | hue << 24;
                        debugImages.color2[index] = keep ? 0xFFFFFFFF : 0;
                    }
                }
            }
        }
        if (debugMode) {
            Bitmap hueImage = Bitmap.createBitmap(debugImages.color1, bound.width, bound.height, Bitmap.Config.RGB_565);
            saveImage("webcam-hue.jpg", hueImage);
            Bitmap objImage = Bitmap.createBitmap(debugImages.color2, bound.width, bound.height, Bitmap.Config.RGB_565);
            saveImage("webcam-obj.jpg", objImage);
        }
        return objectArea;
    }

    private void saveImage(String fileName, Bitmap bitmap) {
        File file = new File(captureDirectory, String.format(Locale.getDefault(), fileName));
        try {
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }
        } catch (IOException e) {
            RobotLog.ee("ObjectFinder", e, "exception in saveBitmap()");
        }
    }

    private void initializeFrameQueue(int capacity) {
        frameQueue = new EvictingBlockingQueue<Bitmap>(new ArrayBlockingQueue<Bitmap>(capacity));
        frameQueue.setEvictAction(new Consumer<Bitmap>() {
            @Override
            public void accept(Bitmap frame) {
                frame.recycle();
            }
        });
    }

    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) return true;
        }
        return false;
    }
    
    private static int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
