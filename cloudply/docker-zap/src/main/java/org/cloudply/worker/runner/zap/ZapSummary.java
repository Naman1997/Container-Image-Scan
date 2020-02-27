package org.cloudply.worker.runner.zap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ZapSummary {
    private String hostName;
    private Integer hostPort;
    private String scanStart;
    private Integer issues;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getHostPort() {
        return hostPort;
    }

    public void setHostPort(Integer hostPort) {
        this.hostPort = hostPort;
    }

    public String getScanStart() {
        return scanStart;
    }

    public void setScanStart(String scanStart) {
        this.scanStart = scanStart;
    }

    public Integer getIssues() {
        return issues;
    }

    public void setIssues(Integer issues) {
        this.issues = issues;
    }

    @JsonIgnore
    public String getStatusMessage() {
        return "STARTED" +"<i>(Started: " + getScanStart()+ ")</i>";
    }
*/
}
