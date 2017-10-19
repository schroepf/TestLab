package de.schroepf.androidxmlrunlistener;

import android.app.Instrumentation;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;

/**
 * This listener creates reports with unique file names (report-0.xml, report-1.xml ...), which
 * is useful for running within a orchestrated setup where each test runs in a separate process.
 *
 * Note: It is necessary to clean up the report directory manually before running the orchestrator or
 * previous files will persist.
 *
 * @see https://developer.android.com/training/testing/junit-runner.html#using-android-test-orchestrator
 */
public class XmlMultiReportRunListener extends XmlRunListener {

    public XmlMultiReportRunListener() {
        super();
    }

    public XmlMultiReportRunListener(XmlSerializer xmlSerializer) {
        super(xmlSerializer);
    }

    @Override
    protected String getFileName(Instrumentation instrumentation) {
        return findFile("report", 0, ".xml", instrumentation);
    }

    private String findFile(String fileNamePrefix, int iterator, String fileNamePostfix, Instrumentation instr) {
        String fileName = fileNamePrefix + "-" + iterator + fileNamePostfix;
        File file = new File(instr.getTargetContext().getExternalFilesDir(null), fileName);

        if (file.exists()) {
            return findFile(fileNamePrefix, iterator + 1, fileNamePostfix, instr);
        } else {
            return file.getName();
        }
    }


}
