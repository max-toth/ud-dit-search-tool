package ru.taskdata.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MappingConfig {
    private CsvColumnInfo[] searchBy;
    private JsonFieldInfo[] resultFrom;
}
