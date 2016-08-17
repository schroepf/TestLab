package de.schroepf.demoapp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.schroepf.androidxmlrunlistener.XmlRunListener;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

/**
 * @author Tobias Schröpf on 17.08.16.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Ignore
    public void testHelloWorld() throws Exception {
        XmlRunListener runListener = new XmlRunListener();
        onView(withId(R.id.hello_world_text_view)).check(matches(withText("This is a failing test")));
    }

    @Test
    public void testFailingTest() throws Exception {
        assertTrue("This is just a failing test", false);
    }
}