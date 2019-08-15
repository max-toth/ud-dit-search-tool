package ru.taskdata.dto;

import lombok.Builder;

@Builder
public class FindOptionsBObjType {

    private final String componentID;
    private final String objectReferenceId;
    private final String objectType;
    private final String relation;
}
