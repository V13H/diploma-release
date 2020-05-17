package com.hvs.diploma.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserTasksSortParams {
    private String updatedBy;
}
