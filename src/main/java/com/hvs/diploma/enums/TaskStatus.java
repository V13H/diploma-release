package com.hvs.diploma.enums;

public enum TaskStatus {
    ACTIVE(""),
    EXPIRED("failed-bg"),
    DONE("done-bg");
    private String cssClassToAppend;

    TaskStatus(String cssClassToAppend) {
        this.cssClassToAppend = cssClassToAppend;
    }

    public String getCssClass() {
        return cssClassToAppend;
    }
}
