package de.schroepf.demoapp;


import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Tobias Schröpf on 23.08.16.
 */
@RunWith(AndroidJUnit4.class)
public class AClass {
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
