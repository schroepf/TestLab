package de.schroepf.androidtestrules;

import android.app.UiAutomation;
import android.os.Build;
import android.provider.Settings;

import java.io.IOException;

import androidx.annotation.RequiresApi;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

public class InstrumentationTestingUtils {

    private InstrumentationTestingUtils() {
        // static helper class
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public static void disableAnimations(UiAutomation automation) {
        executeShellCommand(automation, "settings put global " + Settings.Global.TRANSITION_ANIMATION_SCALE + " 0.0");
        executeShellCommand(automation, "settings put global " + Settings.Global.WINDOW_ANIMATION_SCALE + " 0.0");
        executeShellCommand(automation, "settings put global " + Settings.Global.ANIMATOR_DURATION_SCALE + "0.0");

    }

    @RequiresApi(Build.VERSION_CODES.N)
    public static void enableAnimations(UiAutomation automation) {
        executeShellCommand(automation, "settings put global " + Settings.Global.TRANSITION_ANIMATION_SCALE + " 1.0");
        executeShellCommand(automation, "settings put global " + Settings.Global.WINDOW_ANIMATION_SCALE + " 1.0");
        executeShellCommand(automation, "settings put global " + Settings.Global.ANIMATOR_DURATION_SCALE + " 1.0");
    }

    public static void executeShellCommand(UiAutomation automation, String command) {
        try {
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).executeShellCommand(command);
        } catch (IOException ignore) {

        }
    }
}
