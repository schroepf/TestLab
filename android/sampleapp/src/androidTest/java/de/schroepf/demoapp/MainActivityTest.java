package de.schroepf.demoapp;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;

/**
 * @author Tobias Schröpf on 17.08.16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Test
    @Ignore
    public void testHelloWorld() throws Exception {
        onView(withId(R.id.hello_world_text_view)).check(matches(withText("This is a failing test")));
    }

    @Test
    public void testWithFailingAssumption() throws Exception {
        assumeThat("This is a failing assumption", 0, is(1));
    }

    @Test
    public void testFailingTest() throws Exception {
        fail("This is just a failing test");
    }
}
