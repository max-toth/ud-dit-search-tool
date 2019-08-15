package ru.taskdata.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResultData {
    private final long row;
    private final Object value;
    private final String label;
}
