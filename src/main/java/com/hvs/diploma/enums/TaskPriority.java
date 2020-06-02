package com.hvs.diploma.enums;

public enum TaskPriority {
    HIGH("High priority", "red", 3),
    MEDIUM("Medium priority", "forestgreen", 2),
    LOW("Low priority", "blue", 1);
    private String valueToTemplate;
    private String cssClassToAppend;
    private int priorityValue;

    TaskPriority(String displayValueToTemplate, String cssClassToAppend, int priorityValue) {
        this.valueToTemplate = displayValueToTemplate;
        this.cssClassToAppend = cssClassToAppend;
        this.priorityValue = priorityValue;
    }

    public String getValueToTemplate() {
        return valueToTemplate;
    }

    public String getCssClass() {
        return cssClassToAppend;
    }

    public int getPriorityValue() {
        return priorityValue;
    }


}
