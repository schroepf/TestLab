package de.schroepf.androidtestrules;

import android.os.Build;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import androidx.test.platform.app.InstrumentationRegistry;


/**
 * This {@link TestRule} has no effect on devices with api levels < 24.
 */
public class NoAnimationsRule implements TestRule {
    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    InstrumentationTestingUtils.disableAnimations(InstrumentationRegistry.getInstrumentation().getUiAutomation());
                }
                try {
                    base.evaluate();
                } finally {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        InstrumentationTestingUtils.enableAnimations(InstrumentationRegistry.getInstrumentation().getUiAutomation());
                    }
                }
            }
        };
    }
}
