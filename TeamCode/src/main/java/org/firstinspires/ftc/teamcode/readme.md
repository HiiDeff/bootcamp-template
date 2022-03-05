## NOTICE

In the [TensorFlowDuckDetection.java](/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/tensorflow/TensorFlowDuckDetection.java) class, there is a constant called `VUFORIA_KEY` on line 22. You will need your own Vuforia key to make the TensorFlow work.

In order for your pixel analysis code to work, you must provide a webcam name. This is the name of the webcam in your robot configuration. If your webcam is not in your robot configuration, make sure you load it in by hitting the "scan" button in the configuration after putting the camera in.

In the [BarcodeDetection.java](/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/pixelanalysis/BarcodeDetection.java) class, there is a constant called `THRESHOLD` on line 7. Since the pixel count of a tape marker depends on the angle of the camera to the ground, you *may* need to update this variable if the code is not working. Note that if you completely cover up the tape markers, the current thresholds should work (our camera to which the threshold was made to match is already very low of an angle).

If you are not using the official field tape, often times the tape surface will be reflective, meaning if you have too low of an angle, the color will appear close to white, making it impossible for the program to pick out. In this case, consider manually moving the webcam upwards for the purpose of this exercise.

You need to provide a range for the left and right bounds of the image used in the pixel detection. The total width of the image is 640 pixels. To determine what range is good, take a picture with an op-mode **with debug mode enabled**, then pull the image from the control hub to your computer to look at the full webcam image. Then, you can just estimate the left and right bounds judging by how far across the image you want to place the bounds. **Use the following command to retrieve the full image: `adb pull /sdcard/FIRST/data/webcam-full.jpg ./`**

Lastly, the algorithm (along with the TensorFlow algorithm) returns `LEFT`, `RIGHT`, or `NONE`. **Do not be alarmed if the program returns `LEFT` or `RIGHT` while the duck/TSE is on the middle barcode. You have to consider the view of the camera in relation to the returned values.** For example, if the camera is viewing the left two tape markers, `LEFT` corresponds to the left barcode, `RIGHT` corresponds to the middle barcode, and `NONE` corresponds to the right barcode (because it sees no duck/TSE).

## TeamCode Module

Welcome!

This module, TeamCode, is the place where you will write the op-modes for the hands-on exercises from FTC 18225 High Definition's programming bootcamp. This module currently only contains several utility classes (for easy use of the topics), but you will add your own op-modes to utilize these classes throughout the bootcamp to see them in action.
