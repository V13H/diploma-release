package com.hvs.diploma.pojo;

import com.hvs.diploma.enums.TaskDeadlines;
import com.hvs.diploma.enums.TaskPriority;
import com.hvs.diploma.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SortAndFilterParams {
    private List<TaskPriority> priorities;
    private List<TaskStatus> statuses;
    private List<TaskDeadlines> deadlines;
    private String sortBy = "deadline";
    private String sortOrder = "Ascending";
    private static final TaskStatus[] DEFAULT_STATUS_FILTER_PARAMS = new TaskStatus[]{TaskStatus.ACTIVE, TaskStatus.EXPIRED};
    private static final TaskPriority[] DEFAULT_PRIORITY_FILTER_PARAMS = new TaskPriority[]
            {TaskPriority.HIGH, TaskPriority.MEDIUM, TaskPriority.LOW};

    public SortAndFilterParams(List<TaskPriority> priorities, List<TaskStatus> statuses, List<TaskDeadlines> deadlines) {
        this.priorities = priorities;
        this.statuses = statuses;
        this.deadlines = deadlines;
    }

    private void removeElementFromEnumList(List enumList, String elementToRemove) {
        Object objectToRemove = null;
        if (enumList.size() > 1) {
            for (Object o : enumList) {
                if (o.toString().equals(elementToRemove)) {
                    objectToRemove = o;
                }
            }
            enumList.remove(objectToRemove);
        } else {
            enumList.clear();
        }
    }

    public void removeFilterParameter(String paramName, String value) {
        switch (paramName) {
            case "priority":
                removeElementFromEnumList(priorities, value);
                break;
            case "status":
                removeElementFromEnumList(statuses, value);
                break;
            case "deadline":
                removeElementFromEnumList(deadlines, value);
                break;
        }
    }

    public boolean isFilterParamsSpecified() {
        return exists(priorities) || exists(statuses) || exists(deadlines);
    }

    public String getSortByToDisplay() {
        return sortBy.equalsIgnoreCase("priorityValue") ? "Priority" : "Deadline";
    }

    public String getEmptyListMessage(long tasksCount) {
        return tasksCount == 0 ? "You don`t have tasks yet" : "Tasks not found";
    }

    public void resetFilterParams() {
        priorities = null;
        statuses = null;
        deadlines = null;
    }

    private boolean exists(List paramList) {
        return paramList != null && !paramList.isEmpty();
    }

    public static List setDefaultFilterParams(Object[] defaultParams) {
        ArrayList list = new ArrayList();
        Collections.addAll(list, defaultParams);
        return list;
    }

    @Override
    public String toString() {
        return "SortAndFilterParams{" +
                "priorities=" + priorities +
                ", statuses=" + statuses +
                ", deadlines=" + deadlines +
                ", sortBy='" + sortBy + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}
