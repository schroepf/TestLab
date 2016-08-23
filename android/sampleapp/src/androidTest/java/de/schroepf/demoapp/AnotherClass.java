package de.schroepf.demoapp;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Tobias Schröpf on 23.08.16.
 */
@RunWith(AndroidJUnit4.class)
public class AnotherClass {
    @Test
    public void testHelloWorld() throws Exception {
        assertTrue("This is just a failing test", true);
        assertFalse("This is just a failing test", false);
        assertTrue("This is just a failing test", false);
    }

    @Test
    public void testFailingTest() throws Exception {
        assertTrue("This is just a failing test", false);
    }
}
