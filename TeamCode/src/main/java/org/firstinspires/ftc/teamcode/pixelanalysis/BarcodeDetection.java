package org.firstinspires.ftc.teamcode.pixelanalysis;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public class BarcodeDetection {

    private static final int THRESHOLD = 500;
    private final CameraWrapper camera;
    private final int left, width;
    private boolean debugMode = false;
    private TSEPlacement placement = TSEPlacement.NONE;

    public BarcodeDetection(WebcamName cameraName, int left, int right) {
        this.left = left;
        this.width = right - left + 1;
        camera = new CameraWrapper(cameraName);
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public TSEPlacement getObjectPosition() {
        int top = 120;
        int height = 240;
        ClipBound bound = new ClipBound(left, top, width, height);
        boolean[][] objectArea = camera.extractObjects(bound, ImageProcessor::isBlueOrRed, debugMode);
        if (objectArea != null) {
            int leftCount = 0;
            int rightCount = 0;
            int half = width / 2;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (objectArea[i][j]) {
                        if (j < half) {
                            leftCount++;
                        } else {
                            rightCount++;
                        }
                    }
                }
            }
            if (leftCount > THRESHOLD && rightCount > THRESHOLD) {
                placement = TSEPlacement.NONE;
            } else if (leftCount > THRESHOLD) {
                placement = TSEPlacement.RIGHT;
            } else {
                placement = TSEPlacement.LEFT;
            }
        }
        return placement;
    }

    public boolean setUp() {
        return camera.initCamera();
    }

    public void shutDown() {
        camera.stopCamera();
    }
}
