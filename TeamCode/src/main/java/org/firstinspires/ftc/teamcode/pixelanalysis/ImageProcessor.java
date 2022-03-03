package org.firstinspires.ftc.teamcode.pixelanalysis;

import android.graphics.Color;

public class ImageProcessor {
    public static boolean isBlueOrRed(HSV hsv) {
        return hsv.s > 40 && (hsv.h > 200 && hsv.h < 260 || hsv.h < 20 || hsv.h > 340);
    }

    public static boolean isYellow(HSV hsv) {
        return hsv.s > 40 && hsv.h > 20 && hsv.h  < 66;
    }

    public static HSV RGBToHSV(int color) {
        HSV hsv = new HSV();
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
        // We don't use V so we don't calculate it.
        return hsv;
    }

    private static int max(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    private static int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
}
