package de.schroepf.androidtestrules;

import android.app.Activity;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;

public class ScreenshotActivityRule<T extends Activity> extends ActivityTestRule<T> {

    @Nullable
    private Description currentDescription;

    public ScreenshotActivityRule(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        this.currentDescription = description;
        return super.apply(base, description);
    }

    @Override
    public void finishActivity() {
        try {
            shoot(currentDescription);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, "Taking screenshot failed");
        }
        super.finishActivity();
    }

    private void shoot(@Nullable final Description description) throws IOException {
        if (description == null) {
            return;
        }

        final Screenshot screenshot = description.getAnnotation(Screenshot.class);

        if (screenshot == null) {
            return;
        }

        String subdirectory = !screenshot.subdirectory().isEmpty() ? screenshot.subdirectory() : description.getTestClass().getSimpleName();
        final Set<ScreenCaptureProcessor> processorSet = new HashSet<>();
        processorSet.add(new ScreenshotProcessor(subdirectory));

        try {
            InstrumentationRegistry.getInstrumentation().getUiAutomation().waitForIdle(100, 3000);
        } catch (TimeoutException ignore) {

        } finally {
            androidx.test.runner.screenshot.Screenshot.capture()
                    .setName(screenshot.name().isEmpty() ? description.getMethodName() : screenshot.name())
                    .setFormat(screenshot.format())
                    .process(processorSet);
        }
    }
}
