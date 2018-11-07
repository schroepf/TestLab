package de.schroepf.demoapp;

import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Tobias Schröpf on 23.08.16.
 */
@RunWith(AndroidJUnit4.class)
public class AnotherClass {
    @Test
    public void testHelloWorld() {
        assertTrue("This is just a failing test", true);
        assertFalse("This is just a failing test", false);
        fail("This is just a failing test");
    }

    @Test
    public void testFailingTest() {
        fail("This is just a failing test");
    }
}
