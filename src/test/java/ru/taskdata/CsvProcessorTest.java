package ru.taskdata;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import ru.taskdata.data.CsvColumnInfo;
import ru.taskdata.data.SearchObjectWrapper;

public class CsvProcessorTest {

    @Test
    public void read() {
        CsvColumnInfo[] columnInfos = {
                new CsvColumnInfo("doc_ser", 2),
                new CsvColumnInfo("doc_num", 3)
        };

        List<File> files = new ArrayList<>(FileUtils.listFiles(
                new File("/Users/max_tolstykh/projects/taskdata/ud-dit-search-tool/src/test/resources/input"),
                new String[]{"csv", "xls"},
                false));

        List<SearchObjectWrapper> read = CsvProcessor.read(files.get(0), columnInfos);

        assertEquals(3, files.size());
    }

    @Test
    public void write() {
//        CsvProcessor.write();
    }
}