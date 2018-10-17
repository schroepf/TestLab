package de.schroepf.androidxmlrunlistener;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.xmlpull.v1.XmlSerializer;

/**
 * @author Tobias Schröpf on 17.08.16.
 */
@RunWith(MockitoJUnitRunner.class)
public class XmlRunListenerTest {

    @Mock
    private XmlSerializer mockXmlSerializer;

    @Test
    public void testTestFailure() throws Exception {
        XmlRunListener runListener = new XmlRunListener(mockXmlSerializer);
        runListener.testRunStarted(Description.createSuiteDescription(XmlRunListenerTest.class));

        Description description = Description.createTestDescription(Object.class, "aSimpleTestCase");
        runListener.testStarted(description);

        Throwable throwable = new Throwable();
        Failure failure = new Failure(description, throwable);
        runListener.testFailure(failure);
    }
}