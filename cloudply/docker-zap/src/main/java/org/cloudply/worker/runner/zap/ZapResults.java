package org.cloudply.worker.runner.zap;

import java.util.ArrayList;
import java.util.List;

public class ZapResults {
    private ZapSummary summary = new ZapSummary();
    private List<ZapCheckResult> results = new ArrayList<>();

    public List<ZapCheckResult> getResults() {
        return results;
    }

    public void setResults(List<ZapCheckResult> results) {
        this.results = results;
    }

    public ZapSummary getSummary() {
        return summary;
    }

    public void setSummary(ZapSummary summary) {
        this.summary = summary;
    }
}
