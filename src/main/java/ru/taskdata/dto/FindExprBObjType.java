package ru.taskdata.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public class FindExprBObjType {

    private final String objectType;
    private final String field;
    private final String binOper;
    @Getter
    private final String value;
}
