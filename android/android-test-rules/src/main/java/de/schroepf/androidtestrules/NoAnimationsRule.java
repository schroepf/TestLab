package de.schroepf.androidtestrules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import androidx.test.platform.app.InstrumentationRegistry;


public class NoAnimationsRule implements TestRule {
    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                ScreenshotUtils.disableAnimations(InstrumentationRegistry.getInstrumentation().getUiAutomation());
                try {
                    base.evaluate();
                } finally {
                    ScreenshotUtils.enableAnimations(InstrumentationRegistry.getInstrumentation().getUiAutomation());
                }
            }
        };
    }
}
