package ru.taskdata.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CsvColumnInfo {
    /**
     * Поле в структуре Citizen по которому нужно искать
     */
    private String field;

    /**
     * Название колонки в csv файле
     */
    private String column;

    private int index;
}
