package de.schroepf.demoapp;

import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import de.schroepf.androidtestrules.DemoModeRule;
import de.schroepf.androidtestrules.NoAnimationsRule;
import de.schroepf.androidtestrules.Screenshot;
import de.schroepf.androidtestrules.ScreenshotActivityRule;
import de.schroepf.androidtestrules.demo.BluetoothState;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

/**
 * @author Tobias Schröpf on 17.08.16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @ClassRule
    public static NoAnimationsRule noAnimationsRule = new NoAnimationsRule();

    @ClassRule
    public static DemoModeRule demoModeRule = new DemoModeRule.Builder()
            .clock("1201")
            .batteryLevel(80)
            .batteryPlugged(false)
            .notifications(false)
            .bluetooth(BluetoothState.CONNECTED)
            .build();

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ScreenshotActivityRule<>(MainActivity.class);

    @Test
    @Ignore
    public void testHelloWorld() {
        onView(withId(R.id.hello_world_text_view)).check(matches(withText("This is a failing test")));
    }

    @Test
    public void testWithFailingAssumption() {
        assumeThat("This is a failing assumption", 0, is(1));
    }

    @Test
    public void testFailingTest() {
        fail("This is just a failing test");
    }

    @Screenshot
    @Test
    public void screenshotTest() {

    }

    @Screenshot
    @Test
    public void fabTest() {
        onView(withId(R.id.fab)).perform(click());
    }

    @Screenshot
    @Test
    public void optionsMenuTest() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
    }
}