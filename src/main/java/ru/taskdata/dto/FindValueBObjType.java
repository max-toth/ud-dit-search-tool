package ru.taskdata.dto;

import lombok.Builder;

@Builder
class FindValueBObjType {

    private final String componentID;
    private final String objectReferenceId;
    private final String value;
}
