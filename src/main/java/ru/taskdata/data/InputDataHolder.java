package ru.taskdata.data;

import java.io.File;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InputDataHolder {
    private final int threads;
    private final List<File> files;
    private final MappingConfig mappingConfig;
}
