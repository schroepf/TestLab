package de.schroepf.androidxmlrunlistener;

import android.app.Instrumentation;
import android.os.Build;
import android.os.Environment;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class XmlRunListener extends InstrumentationRunListener {
    private static final String TAG = XmlRunListener.class.getSimpleName();

    private static final String ENCODING_UTF_8 = "utf-8";
    private static final String NAMESPACE = "";

    public static final String TOKEN_SUITE = "__suite__";
    public static final String TOKEN_EXTERNAL = "__external__";

    //    private static final String TAG_SUITES = "testsuites";
    private static final String TAG_SUITE = "testsuite";
    private static final String TAG_PROPERTIES = "properties";
    private static final String TAG_CASE = "testcase";
    private static final String TAG_ERROR = "error";
    private static final String TAG_FAILURE = "failure";
    private static final String TAG_SKIPPED = "skipped";

    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_CLASS = "classname";
    private static final String ATTRIBUTE_TYPE = "type";
    private static final String ATTRIBUTE_MESSAGE = "message";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_TIMESTAMP = "timestamp";
    private static final String ATTRIBUTE_TESTS = "tests";

    private FileOutputStream outputStream;

    private final XmlSerializer xmlSerializer;

    private final Map<Description, Long> testStarts = new HashMap<>();

    private Description currentRun;

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
//            instr.getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//            instr.getTargetContext().getFilesDir().getAbsolutePath()
            File outputFile = new File(instr.getTargetContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

            // Seems like we need to put this into the tart application's context as for the instrumentation app's
            // context we can never be sure if we have the correct permissions - and getFilesDir() seems to return null
            //            File directory = instr.getTargetContext().getFilesDir();
//            File outputFile = new File(directory, fileName);
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
        Log.d(TAG, "testRunStarted() called with: " + "description = [" + description + "]");

        currentRun = description;

        xmlSerializer.startTag(NAMESPACE, TAG_SUITE);

        String name = description.getDisplayName();
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_NAME, name == null ? "Test Results" : name);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String nowAsISO = df.format(new Date());
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_TIMESTAMP, nowAsISO);

        xmlSerializer.startTag(NAMESPACE, TAG_PROPERTIES);
        xmlSerializer.attribute(NAMESPACE, "device.manufacturer", Build.MANUFACTURER);
        xmlSerializer.attribute(NAMESPACE, "device.model", Build.MODEL);
        xmlSerializer.endTag(NAMESPACE, TAG_PROPERTIES);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        Log.d(TAG, "testRunFinished: failureCount: " + result.getFailureCount() + " ignoreCount: " + result.getIgnoreCount() + " runCount: " + result.getRunCount() + " runTime: " + result.getRunTime());
        xmlSerializer.endTag(NAMESPACE, TAG_SUITE);
        xmlSerializer.endDocument();
        xmlSerializer.flush();

        currentRun = null;
    }

    @Override
    public void testStarted(Description description) throws Exception {
        Log.d(TAG, "testStarted() called with: " + "description = [" + description + "]");
        startTestCase(description);
    }

    @Override
    public void testFinished(Description description) throws Exception {
        Log.d(TAG, "testFinished() called with: " + "description = [" + description + "]");
        endTestCase(description);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        addFailure(TAG_FAILURE, failure);
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        addFailure(TAG_FAILURE, failure);
    }

    @Override
    public void testIgnored(Description description) throws Exception {
        Log.d(TAG, "testIgnored() called with: " + "description = [" + description + "]");
        startTestCase(description);
        xmlSerializer.startTag(NAMESPACE, TAG_SKIPPED);
        xmlSerializer.endTag(NAMESPACE, TAG_SKIPPED);
        endTestCase(description);
    }

    private void addFailure(String tag, Failure failure) {
        try {
            xmlSerializer.startTag(NAMESPACE, tag);
            String message = failure.getMessage();

            if (message != null && message.length() > 0) {
                xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_MESSAGE, message);
            }

            xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_TYPE, failure.getException().getClass().getName());
            xmlSerializer.text(failure.getTrace());
            xmlSerializer.endTag(NAMESPACE, tag);
            xmlSerializer.flush();
        } catch (IOException e) {
            Log.e(TAG, safeMessage(e));
        }
    }

    private String safeMessage(Throwable error) {
        String message = error.getMessage();
        return error.getClass().getName() + ": " + (message == null ? "<null>" : message);
    }

    private void startTestCase(Description description) throws IOException {
        xmlSerializer.startTag(NAMESPACE, TAG_CASE);
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_CLASS, description.getClassName());
        xmlSerializer.attribute(NAMESPACE, ATTRIBUTE_NAME, description.getDisplayName());
    }

    private void endTestCase(Description description) throws IOException {
        xmlSerializer.endTag(NAMESPACE, TAG_CASE);
    }
}