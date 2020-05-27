package com.hvs.diploma.util;

import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class SortAndFilterParams {
    private List<TaskPriority> priorities;
    private List<TaskStatus> statuses;
    private List<Timestamp> deadlines;
    private String sortBy = "deadline";
    private String sortOrder = "Ascending";

    public SortAndFilterParams(List<TaskPriority> priorities, List<TaskStatus> statuses, List<Timestamp> deadlines) {
        this.priorities = priorities;
        this.statuses = statuses;
        this.deadlines = deadlines;
    }

    private void removeElementFromEnumList(List enumList, String elementToRemove) {
        Object objectToRemove = null;
        for (Object o : enumList) {
            if (o.toString().equals(elementToRemove)) {
                objectToRemove = o;
            }
            enumList.remove(objectToRemove);
        }
    }

    public void removeFilterParameter(String paramValueToRemove) {
        switch (paramValueToRemove) {
            case "priority":
                removeElementFromEnumList(priorities, paramValueToRemove);
                break;
            case "status":
                removeElementFromEnumList(statuses, paramValueToRemove);
                break;
            case "deadline":
                removeElementFromEnumList(deadlines, paramValueToRemove);
                break;
        }
    }
}
