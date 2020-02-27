package org.cloudply.worker.runner.zap;

public class ZapCheckResult {
    private String description;
    private String instance;
    private String name;
    private String riskLevel;
    private String solution;
    private String reference;
    private String cweid;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getCweid() {
        return cweid;
    }

    public void setCweid(String cweid) {
        this.cweid = cweid;
    }
}
