## NOTICE

This repository contains the public FTC SDK for the Freight Frenzy (2021-2022) competition season along with several utility classes needed for FTC 18225 High Definition's programming bootcamp.

In the [TensorFlowDuckDetection.java](/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/tensorflow/TensorFlowDuckDetection.java) class, there is a constant called `VUFORIA_KEY` on line 22. You will need your own Vuforia key to make the TensorFlow work.

In order for your pixel analysis code to work, you must provide a webcam name. This is the name of the webcam in your robot configuration. If your webcam is not in your robot configuration, make sure you load it in by hitting the "scan" button in the configuration after putting the camera in.

In the [BarcodeDetection.java](/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/pixelanalysis/BarcodeDetection.java) class, there is a constant called `THRESHOLD` on line 7. Since the pixel count of a tape marker depends on the angle of the camera to the ground, you *may* need to update this variable if the code is not working. Note that if you completely cover up the tape markers, the current thresholds should work (our camera to which the threshold was made to match is already very low of an angle).

If you are not using the official field tape, often times the tape surface will be reflective, meaning if you have too low of an angle, the color will appear close to white, making it impossible for the program to pick out. In this case, consider manually moving the webcam upwards for the purpose of this exercise.

You need to provide a range for the left and right bounds of the image used in the pixel detection. The total width of the image is 640 pixels. To determine what range is good, take a picture with an op-mode **with debug mode enabled**, then pull the image from the control hub to your computer to look at the full webcam image. Then, you can just estimate the left and right bounds judging by how far across the image you want to place the bounds. **Use the following command to retrieve the full image: `adb pull /sdcard/FIRST/data/webcam-frame-0.jpg ./`**

Lastly, the algorithm (along with the TensorFlow algorithm) returns `LEFT`, `RIGHT`, or `NONE`. **Do not be alarmed if the program returns `LEFT` or `RIGHT` while the duck/TSE is on the middle barcode. You have to consider the view of the camera in relation to the returned values.** For example, if the camera is viewing the left two tape markers, `LEFT` corresponds to the left barcode, `RIGHT` corresponds to the middle barcode, and `NONE` corresponds to the right barcode (because it sees no duck/TSE).

## Welcome!
This GitHub repository contains the source code that is used to build an Android app to control a *FIRST* Tech Challenge competition robot, with a template to follow along easily with FTC 18225 High Definition's programming bootcamp.  To use this SDK, download/clone the entire project to your local computer.

## Downloading the Project
There are several ways to download this repo.

* If you are a git user, you can clone the most current version of the repository:

<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;git clone https://github.com/FIRST-Tech-Challenge/FtcRobotController.git</p>

* Or, if you prefer, you can use the "Download Zip" button available through the main repository page.  Downloading the project as a .ZIP file will keep the size of the download manageable.

Once you have downloaded and uncompressed (if needed) your folder, you can use Android Studio to import the folder  ("Import project (Eclipse ADT, Gradle, etc.)").

## Getting Help
### User Documentation and Tutorials
*FIRST* maintains online documentation with information and tutorials on how to use the *FIRST* Tech Challenge software and robot control system.  You can access this documentation using the following link:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[FtcRobotController Online Documentation](https://github.com/FIRST-Tech-Challenge/FtcRobotController/wiki)

Note that the online documentation is an "evergreen" document that is constantly being updated and edited.  It contains the most current information about the *FIRST* Tech Challenge software and control system.

### Javadoc Reference Material
The Javadoc reference documentation for the FTC SDK is now available online.  Click on the following link to view the FTC SDK Javadoc documentation as a live website:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[FTC Javadoc Documentation](https://javadoc.io/doc/org.firstinspires.ftc)

### Online User Forum
For technical questions regarding the Control System or the FTC SDK, please visit the FTC Technology forum:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[FTC Technology Forum](https://ftcforum.firstinspires.org/forum/ftc-technology)

### Sample OpModes
This project contains a large selection of Sample OpModes (robot code examples) which can be cut and pasted into your /teamcode folder to be used as-is, or modified to suit your team's needs.

Samples Folder: &nbsp;&nbsp; [/FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples](FtcRobotController/src/main/java/org/firstinspires/ftc/robotcontroller/external/samples)

The readme.md file located in the [/TeamCode/src/main/java/org/firstinspires/ftc/teamcode](TeamCode/src/main/java/org/firstinspires/ftc/teamcode) folder contains an explanation of the sample naming convention, and instructions on how to copy them to your own project space.
