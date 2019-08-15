package ru.taskdata;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVFormat.EXCEL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.taskdata.data.CsvColumnInfo;
import ru.taskdata.data.ResultData;
import ru.taskdata.data.SearchObjectWrapper;
import ru.taskdata.dto.FindCondBObjType;
import ru.taskdata.dto.FindExprBObjType;
import ru.taskdata.dto.FindGroupBObjType;
import ru.taskdata.dto.ProfileDataSearchRequestBObjType;

@UtilityClass
public class CsvProcessor {

    private static final Logger log = LogManager.getLogger(CsvProcessor.class);
    private static final String CITIZEN_OBJECT_TYPE = "Citizen";
    private static final String EQ_BINARY_OPERATOR = "=";

    public static List<SearchObjectWrapper> read(File file, CsvColumnInfo[] columnInfos) {
        try (CSVParser parser = CSVParser.parse(file, UTF_8, EXCEL)) {
            return parser.getRecords().stream()
                    .map(record ->
                    {
                        ProfileDataSearchRequestBObjType object = ProfileDataSearchRequestBObjType.builder().findGroupBObj(FindGroupBObjType.builder()
                                .logicOper("AND")
                                .findCondBObj(buildConditionObject(columnInfos, parser, record))
                                .build())
                                .build();

                        return SearchObjectWrapper.builder()
                                .row(record.getRecordNumber())
                                .object(object)
                                .build();
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error on csv data read", e);
            return Collections.emptyList();
        }
    }

    public static void write(File file, List<ResultData> data) {
        try (CSVParser parser = CSVParser.parse(file, UTF_8, EXCEL);
             CSVPrinter printer = new CSVPrinter(new FileWriter(file.getAbsolutePath(), true),
                     DEFAULT.withHeader(parser.getHeaderNames().toArray(new String[]{})))) {

            parser.getRecords().forEach(record -> {
                List<String> values = new ArrayList<>();
                for (String value : record) {
                    values.add(value);
                }

                data.stream()
                        .filter(row -> row.getRow() == record.getRecordNumber())
                        .forEach(row -> values.add(String.valueOf(row.getValue())));

                try {
                    printer.printRecord(values.toArray(new String[values.size()]));
                } catch (IOException e) {
                    log.error("Error on record printing", e);
                }
            });

        } catch (IOException e) {
            log.error("Error on csv data write", e);
        }
    }

    private static List<FindCondBObjType> buildConditionObject(CsvColumnInfo[] columnInfos, CSVParser parser,
                                                               CSVRecord record) {
        return Stream.of(columnInfos)
                .map(column -> {
                    String value = readValue(parser, record, column);
                    return buildConditionExpression(column, value);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static String readValue(CSVParser parser, CSVRecord record, CsvColumnInfo column) {
        return parser.getHeaderNames().contains(column.getColumn())
                ? record.get(column.getColumn())
                : record.get(column.getIndex());
    }

    private static FindCondBObjType buildConditionExpression(CsvColumnInfo column, String value) {
        return StringUtils.isNotEmpty(value)
                ? FindCondBObjType.builder().findExprBObj(
                FindExprBObjType.builder()
                        .binOper(EQ_BINARY_OPERATOR)
                        .objectType(CITIZEN_OBJECT_TYPE)
                        .field(column.getField())
                        .value(value.trim().toUpperCase())
                        .build())
                .build() : null;
    }
}
