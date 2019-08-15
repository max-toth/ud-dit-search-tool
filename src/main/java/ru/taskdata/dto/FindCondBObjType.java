package ru.taskdata.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindCondBObjType {
    private final FindExprBObjType findExprBObj;
}
