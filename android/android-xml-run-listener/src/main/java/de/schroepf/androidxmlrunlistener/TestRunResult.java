package de.schroepf.androidxmlrunlistener;

import android.util.Log;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author Tobias Schröpf on 22.08.16.
 */
public class TestRunResult {

    private static final String TAG = TestRunResult.class.getSimpleName();

    private Description testRun;

    private Map<Description, TestResult> allTests = new HashMap<>();

    private List<TestResult> failedTests = new ArrayList<>();
    private List<TestResult> assumptionFailedTests = new ArrayList<>();
    private List<TestResult> ignoredTests = new ArrayList<>();
    private List<TestResult> passedTests = new ArrayList<>();

    private long startTime = 0;
    private long endTime = 0;

    public void runStarted(Description description) {
        startTime = System.currentTimeMillis();
        testRun = description;
    }

    public void testStarted(Description description) throws Exception {
        allTests.put(description, new TestResult(description));
    }

    public void testFailure(Failure failure) throws Exception {
        TestResult testResult = allTests.get(failure.getDescription());
        testResult.recordFailure(failure);
        failedTests.add(testResult);
    }

    public void testAssumptionFailure(Failure failure) {
        TestResult testResult = allTests.get(failure.getDescription());
        testResult.recordAssumptionFailure(failure);
        assumptionFailedTests.add(testResult);
    }

    public void testIgnored(Description description) throws Exception {
        TestResult testResult = allTests.get(description);

        if (testResult == null) {
            testResult = new TestResult(description);
            allTests.put(description, testResult);
        }

        testResult.recordTestIgnored();
        ignoredTests.add(testResult);
    }

    public void testFinished(Description description) throws Exception {
        endTime = System.currentTimeMillis();

        TestResult testResult = allTests.get(description);

        // will be null for ignored tests!
        if (testResult == null) {
            return;
        }

        testResult.recordFinished();

        if (testResult.getStatus() == TestResult.Status.STARTED) {
            passedTests.add(testResult);
        }
    }

    public void runFinished(Result result) {
        Log.d(TAG, "testRunFinished() called with: " + "result = [" + result + "]");
    }

    public String getTestSuiteName() {
        return testRun.getDisplayName();
    }

    public Map<Description, TestResult> getAllTests() {
        return allTests;
    }

    public List<TestResult> getPassedTests() {
        return passedTests;
    }

    public List<TestResult> getIgnoredTests() {
        return ignoredTests;
    }

    public List<TestResult> getAssumptionFailedTests() {
        return assumptionFailedTests;
    }

    public List<TestResult> getFailedTests() {
        return failedTests;
    }

    public long getElapsedTime() {
        long endTime = this.endTime;

        if (endTime == 0) {
            endTime = System.currentTimeMillis();
        }

        return endTime - startTime;
    }

    public String startTimeAsIso() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date(startTime));
    }
}
