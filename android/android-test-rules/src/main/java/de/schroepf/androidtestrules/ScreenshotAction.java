package de.schroepf.androidtestrules;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;

import org.hamcrest.Matcher;

/**
 * An Espresso action for taking screenshots.
 * <p>
 * The app requires the {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE} permission.
 */
public class ScreenshotAction implements ViewAction {
    private final String name;
    private final String subdirectory;

    private ScreenshotAction(String name, String subdirectory) {
        this.name = name;
        this.subdirectory = subdirectory;
    }

    @Override
    public Matcher<View> getConstraints() {
        return ViewMatchers.isDisplayed();
    }

    @Override
    public String getDescription() {
        return subdirectory + "/screenshot '" + name + "'";
    }

    @Override
    public void perform(UiController uiController, View view) {
        uiController.loopMainThreadUntilIdle();

        InstrumentationRegistry.getInstrumentation().getUiAutomation().takeScreenshot();
    }

    /**
     * Create a new screenshot action
     *
     * @param name         screenshot name
     * @param subdirectory subdirectory where the screenshot should be saved
     * @return a new screenshot action
     */
    @NonNull
    public static ViewAction screenshot(final String name, final String subdirectory) {
        return ViewActions.actionWithAssertions(new ScreenshotAction(name, subdirectory));
    }
}
