package com.hvs.diploma.enums;

public enum TaskStatus {
    ACTIVE(""),
    EXPIRED("failed-bg"),
    DONE("done-bg"),
    RESTARTED_ACTIVE(""),
    RESTARTED_DONE("done-bg");
    //defines task card`s background in the template
    private String cssClassToAppend;

    TaskStatus(String cssClassToAppend) {
        this.cssClassToAppend = cssClassToAppend;
    }

    public String getCssClass() {
        return cssClassToAppend;
    }
}
