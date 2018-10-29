package de.schroepf.androidtestrules;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.screenshot.ScreenCaptureProcessor;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private void shoot(@Nullable Description description) throws IOException {
        if (description == null) {
            return;
        }

        Screenshot screenshot = description.getAnnotation(Screenshot.class);

        if (screenshot == null) {
            return;
        }

        String subdirectory = !screenshot.subdirectory().isEmpty() ? screenshot.subdirectory() : description.getTestClass().getSimpleName();
        Set<ScreenCaptureProcessor> processorSet = new HashSet<>();
        processorSet.add(new ScreenshotProcessor(subdirectory));

        android.support.test.runner.screenshot.Screenshot.capture()
                .setName(screenshot.name().isEmpty() ? description.getMethodName() : screenshot.name())
                .setFormat(screenshot.format())
                .process(processorSet);
    }
}
