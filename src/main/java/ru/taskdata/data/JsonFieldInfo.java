package ru.taskdata.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JsonFieldInfo {
    private final String path;
    private final String label;
}
