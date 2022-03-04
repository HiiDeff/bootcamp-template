package org.firstinspires.ftc.teamcode.tensorflow;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class TensorFlowDuckDetection {

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private static final String VUFORIA_KEY = "YOUR_VUFORIA_KEY_HERE";

    private static final double SCREEN_WIDTH = 1280;

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private final HardwareMap hardwareMap;
    
    public TensorFlowDuckDetection(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(2, 16.0 / 9.0);
        }
    }
    
    public DuckPlacement getDuckPlacement() {
        List<Recognition> objects = tfod == null ? null : tfod.getUpdatedRecognitions();
        if (objects != null && objects.size() > 0) {
            Recognition object = objects.get(0);
            double middle = (object.getLeft() + object.getRight()) / 2;
            if (middle < SCREEN_WIDTH / 2) {
                return DuckPlacement.LEFT;
            }
            return DuckPlacement.RIGHT;
        }
        return DuckPlacement.NONE;
    }
    
    public void shutdown() {
        if (tfod != null) tfod.shutdown();
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}
