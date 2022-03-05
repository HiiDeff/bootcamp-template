package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.tensorflow.DuckPlacement;
import org.firstinspires.ftc.teamcode.tensorflow.TensorFlowDuckDetection;

@TeleOp(name = "TensorFlow Op-Mode", group = "Demonstration")
public class TensorFlowOpMode extends LinearOpMode {
    @Override
    public void runOpMode() {
        TensorFlowDuckDetection detection = new TensorFlowDuckDetection(hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            DuckPlacement duck = detection.getDuckPlacement();
            telemetry.addData("Duck position", duck == DuckPlacement.LEFT ? "left" :
                    (duck == DuckPlacement.RIGHT ? "right" : "none"));
            telemetry.update();
        }
        detection.shutdown();
    }
}
