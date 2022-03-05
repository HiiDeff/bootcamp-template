package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.pixelanalysis.BarcodeDetection;
import org.firstinspires.ftc.teamcode.pixelanalysis.TSEPlacement;

@TeleOp(name = "Pixel Analysis Op-Mode", group = "Demonstration")
public class PixelAnalysisOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        BarcodeDetection detection = new BarcodeDetection(
                hardwareMap.get(WebcamName.class, "Webcam 1"), 120, 520);
        detection.setDebugMode(true);
        waitForStart();
        while (opModeIsActive()) {
            TSEPlacement tse = detection.getObjectPosition();
            telemetry.addData("TSE position", tse == TSEPlacement.LEFT ? "left" :
                    (tse == TSEPlacement.RIGHT ? "right" : "none"));
            telemetry.update();
        }
        detection.shutDown();
    }
}
