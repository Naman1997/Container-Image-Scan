/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 Kaiburr
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.cloudply.worker.runner.zap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudply.assessment.*;
import org.cloudply.environment.EnvironmentContributor;
import org.cloudply.worker.runner.WorkerRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.cloudply.service.assessment.ZapAssessmentService.ASSESSMENT_TYPE_ZAP;

public class ZapRunner extends WorkerRunner {
    public static final String CHECK_DESCRIPTION = "Description";
    public static final String CHECK_INSTANCE = "Instance";
    public static final String CHECK_NAME = "Name";
    public static final String CHECK_RISKLEVEL = "Risk Level";
    public static final String CHECK_SOLUTION = "Solution";
    public static final String CHECK_REFERENCE = "Reference";
    public static final String CHECK_CWEID = "Cweid";

/*
    private static final Map<String, AssessmentItemStatus> SEVERITY_STATES = new HashMap() {{
        // These are for result items
        put("None", AssessmentItemStatus.INFO);
        put("MINOR", AssessmentItemStatus.WARNING);
        put("MAJOR", AssessmentItemStatus.ERROR);
        put("BLOCKER", AssessmentItemStatus.ERROR);
        // And these are for summary
        put("MinMI", AssessmentItemStatus.INFO);
        put("MaxHalsteadVolume", AssessmentItemStatus.INFO);
        put("MaxCyclomaticComplexity", AssessmentItemStatus.INFO);
        put("Warnings", AssessmentItemStatus.WARNING);
        put("Errors", AssessmentItemStatus.ERROR);
    }};
*/

    public final static String REPORT_FILE_NAME = "/zap/result.json";

    private Logger log = LogManager.getLogger(ZapWorkerRunner.class);

    @Override
    protected boolean canRun(AssessmentType assessmentType) {
        return assessmentType.equals(ASSESSMENT_TYPE_ZAP);
    }

    static AssessmentReportItem fromCheckResult(ZapCheckResult checkResult) {
        AssessmentReportItem item = new AssessmentReportItem();
        item.getData().put(CHECK_DESCRIPTION, new AssessmentReportValue(checkResult.getDescription()));
        item.getData().put(CHECK_INSTANCE, new AssessmentReportValue(checkResult.getInstance()));
        item.getData().put(CHECK_NAME, new AssessmentReportValue(checkResult.getName()));
        item.getData().put(CHECK_RISKLEVEL, new AssessmentReportValue(checkResult.getRiskLevel()));
        item.getData().put(CHECK_SOLUTION, new AssessmentReportValue(checkResult.getSolution()));
        item.getData().put(CHECK_REFERENCE, new AssessmentReportValue(checkResult.getReference()));
        item.getData().put(CHECK_CWEID, new AssessmentReportValue(checkResult.getCweid()));
        return item;
    }

    @Override
    protected void processRunResults(AssessmentReport report) {
        try {
            report.setItemColumns(ImmutableList.of(CHECK_NAME, CHECK_INSTANCE, CHECK_RISKLEVEL,
                    CHECK_DESCRIPTION,CHECK_SOLUTION,CHECK_REFERENCE,CHECK_CWEID));
            report.setItemColumnsWidthScale(ImmutableMap.of(CHECK_DESCRIPTION, 3,CHECK_SOLUTION, 3));

//            report.setSeverityColumn(CHECK_SEVERITY);

            File resultFile = new File(REPORT_FILE_NAME);
            log.info(FileUtils.readFileToString(resultFile, "utf-8"));
            ZapResults zapResults = jsonMapper.readValue(resultFile, ZapResults.class);
            zapResults.getResults().stream()
                    .map(ZapWorkerRunner::fromCheckResult)
                    .forEach(item -> {
                        item.setReport(report);
                        item.setResourceId(report.getResource().getResourceId());
                        report.getItems().add(item);
                    });

            generateSummary(report, zapResults);
        } catch (IOException e) {
            String message = String.format("Cannot load assessment results\n %s \n %s", ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e));
            report.setFailedStatus(message);
        }
    }

    private void generateSummary(AssessmentReport report, ZapResults zapResults) {
        ZapSummary zapSummary = zapResults.getSummary();

        report.setStatusMessage(zapSummary.getStatusMessage());
//        report.setSeverityStates(SEVERITY_STATES);

        Map<String, Double> summaryValues = new HashMap<>();

        summaryValues.put("HostName", zapSummary.getHostName());
        summaryValues.put("HostPort", zapSummary.getHostPort().doubleValue());
        summaryValues.put("Issues", zapSummary.getIssues().doubleValue());
        report.setSummaryValues(summaryValues);
    }

    @Override
    protected String[] generateCommand(AssessmentReport report) {
        return ArrayUtils.toArray("/zap/run.sh");
    }

    @Override
    protected Map<String, String> generateEnv(AssessmentReport report) {
        SourceCodeAssessmentResource resource = (SourceCodeAssessmentResource) report.getResource();
        Map<String, String> env = new HashMap<>();
        EnvironmentContributor.add(env, resource.getRepositoryConfiguration());
        return env;
    }
}
