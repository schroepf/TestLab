package de.schroepf.androidtestrules;

import android.app.UiAutomation;

import java.io.IOException;

public class ScreenshotUtils {

    private ScreenshotUtils() {
        // static helper class
    }

    public static void disableAnimations(UiAutomation automation) {
        executeShellCommand(automation, "settings put global transition_animation_scale 0");
        executeShellCommand(automation, "settings put global window_animation_scale 0");
        executeShellCommand(automation, "settings put global animator_duration_scale 0");

    }

    public static void enableAnimations(UiAutomation automation) {
        executeShellCommand(automation, "settings put global transition_animation_scale 1");
        executeShellCommand(automation, "settings put global window_animation_scale 1");
        executeShellCommand(automation, "settings put global animator_duration_scale 1");
    }

    public static void executeShellCommand(UiAutomation automation, String command) {
        try {
            automation.executeShellCommand(command).close();
        } catch (IOException ignore) {

        }
    }
}
