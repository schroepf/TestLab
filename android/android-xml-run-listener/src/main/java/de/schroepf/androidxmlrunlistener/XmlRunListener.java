package de.schroepf.androidxmlrunlistener;

import android.app.Instrumentation;
import android.os.Build;
import android.support.test.internal.runner.listener.InstrumentationRunListener;
import android.util.Log;
import android.util.Xml;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class XmlRunListener extends InstrumentationRunListener {
    private static final String TAG = XmlRunListener.class.getSimpleName();

    private static final String ENCODING_UTF_8 = "utf-8";
    private static final String NAMESPACE = null;

    public static final String TOKEN_SUITE = "__suite__";
    public static final String TOKEN_EXTERNAL = "__external__";

    //    private static final String TAG_SUITES = "testsuites";
    private static final String TAG_SUITE = "testsuite";
    private static final String TAG_PROPERTIES = "properties";
    private static final String TAG_PROPERTY = "property";
    private static final String TAG_CASE = "testcase";
    private static final String TAG_ERROR = "error";
    private static final String TAG_FAILURE = "failure";
    private static final String TAG_SKIPPED = "skipped";

    private static final String ATTRIBUTE_ASSERTIONS = "assertions";
    private static final String ATTRIBUTE_CLASS = "classname";
    private static final String ATTRIBUTE_ERRORS = "errors";
    private static final String ATTRIBUTE_FAILURES = "failures";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_SKIPPED = "skipped";
    private static final String ATTRIBUTE_TESTS = "tests";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_TIMESTAMP = "timestamp";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_VALUE = "value";

    private FileOutputStream outputStream;

    private final XmlSerializer xmlSerializer;

    private TestRunResult runResult;


    public XmlRunListener() {
        this(Xml.newSerializer());
    }

    public XmlRunListener(XmlSerializer xmlSerializer) {
        this.xmlSerializer = xmlSerializer;
    }

    @Override
    public void setInstrumentation(Instrumentation instr) {
        super.setInstrumentation(instr);

        final String fileName = "report.xml";

        try {
            // Seems like we need to put this into the target application's context as for the instrumentation app's
            // context we can never be sure if we have the correct permissions - and getFilesDir() seems to return null
            File outputFile = new File(instr.getTargetContext().getExternalFilesDir(null), fileName);

            Log.d(TAG, "setInstrumentation: outputFile: " + outputFile);
            outputStream = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Unable to open report file: " + fileName, e);
            throw new RuntimeException("Unable to open report file: " + e.getMessage(), e);
        }

        try {
            xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            xmlSerializer.setOutput(outputStream, ENCODING_UTF_8);
            xmlSerializer.startDocument(ENCODING_UTF_8, true);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open serializer", e);
            throw new RuntimeException("Unable to open serializer: " + e.getMessage(), e);
        }
    }

    @Override
    public void testRunStarted(Description description) throws Exception {
        runResult = new TestRunResult();
        runResult.runStarted(description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        runResult.runFinished(result);

        printTestResults();
    }

    @Override
    public void testStarted(Description description) throws Exception {
        runResult.testStarted(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        runResult.testFinished(description);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        runResult.testFailure(failure);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        runResult.testAssumptionFailure(failure);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        runResult.testIgnored(description);
    }

    private void printTestResults() throws IOException {

        xmlSerializer.startTag(NAMESPACE, TAG_SUITE);
        String name = runResult.getTestSuiteName();
        if (name != null && name.isEmpty()) {
            xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_NAME, name);
        }
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_TESTS, Integer.toString(runResult.getAllTests().size()));
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_FAILURES, Integer.toString(runResult.getFailedTests().size() + runResult.getAssumptionFailedTests().size()));

        // legacy - there are no errors in JUnit4
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_ERRORS, "0");
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_SKIPPED, Integer.toString(runResult.getIgnoredTests().size()));

        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_TIME, Double.toString((double) runResult.getElapsedTime() / 1000.f));
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_TIMESTAMP, runResult.startTimeAsIso());
//        xmlSerializer.attribute(NAMESPACE, HOSTNAME, mHostName);

        xmlSerializer.startTag(NAMESPACE, TAG_PROPERTIES);
        printProperty("device.manufacturer", Build.MANUFACTURER);
        printProperty("device.model", Build.MODEL);
        printProperty("device.apiLevel", String.valueOf(Build.VERSION.SDK_INT));
        xmlSerializer.endTag(NAMESPACE, TAG_PROPERTIES);

        Map<Description, TestResult> testResults = runResult.getAllTests();
        for (Map.Entry<Description, TestResult> testEntry : testResults.entrySet()) {
            xmlSerializer.startTag(NAMESPACE, TAG_CASE);
            xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_NAME, testEntry.getKey().getDisplayName());
            xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_CLASS, testEntry.getKey().getClassName());
            long elapsedTimeMs = testEntry.getValue().getElapsedTime();
            xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_TIME, Double.toString((double)elapsedTimeMs / 1000.f));

            switch (testEntry.getValue().getStatus()) {
                case FAILURE:
                case ASSUMPTION_FAILURE:
                    Failure failure = testEntry.getValue().getFailure();
                    xmlSerializer.startTag(NAMESPACE, TAG_FAILURE);

                    String type = failure.getException().getClass().getName();
                    if (type != null && !type.isEmpty()) {
                        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_TYPE, type);
                    }

                    String message = failure.getMessage();
                    if (message != null && !message.isEmpty()) {
                        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_MESSAGE, message);
                    }

                    xmlSerializer.text(sanitize(testEntry.getValue().getFailure().getTrace()));
                    xmlSerializer.endTag(NAMESPACE, TAG_FAILURE);
                    break;

                case IGNORED:
                    xmlSerializer.startTag(NAMESPACE, TAG_SKIPPED);
                    xmlSerializer.endTag(NAMESPACE, TAG_SKIPPED);
                    break;
            }

            xmlSerializer.endTag(NAMESPACE, TAG_CASE);
        }

        xmlSerializer.endTag(NAMESPACE, TAG_SUITE);
        xmlSerializer.endDocument();
        xmlSerializer.flush();
    }

    private void printProperty(String name, String value) throws IOException {
        xmlSerializer.startTag(NAMESPACE, TAG_PROPERTY);
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_NAME, name);
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_VALUE, value);
        xmlSerializer.endTag(NAMESPACE, TAG_PROPERTY);
    }

    /**
     * Returns the text in a format that is safe for use in an XML document.
     */
    private String sanitize(String text) {
        return text.replace("\0", "<\\0>");
    }

    private String safeMessage(Throwable error) {
        String message = error.getMessage();
        return error.getClass().getName() + ": " + (message == null ? "<null>" : message);
    }
}