package ru.taskdata;

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.taskdata.data.InputDataHolder;
import ru.taskdata.data.ResultData;
import ru.taskdata.data.SearchObjectWrapper;

public class App {

    public static void main(String[] args) {
        Optional<InputDataHolder> inputDataHolder = new CmdProcessor().read(args);

        InputDataHolder inputDataHolderValue = inputDataHolder
                .orElseThrow(() -> new IllegalStateException("Wrong input data format"));

        SearchService searchService = new SearchService();

        inputDataHolderValue.getFiles()
                .forEach(file -> {
                    List<SearchObjectWrapper> requestBodies =
                            CsvProcessor.read(file, inputDataHolderValue.getMappingConfig().getSearchBy());

                    List<ResultData> data = getData(inputDataHolderValue, searchService, requestBodies);

                    CsvProcessor.write(file, data);
                });
    }

    private static List<ResultData> getData(InputDataHolder inputDataHolderValue, SearchService searchService, List<SearchObjectWrapper> requestBodies) {
        return requestBodies.stream()
                .map(requestBody -> {
                    String json = searchService.doPost(requestBody.getObject());
                    return extractJsonFieldValue(inputDataHolderValue, requestBody, json);
                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private static List<ResultData> extractJsonFieldValue(InputDataHolder inputDataHolderValue,
                                                          SearchObjectWrapper requestBody, String json) {
        return Stream.of(inputDataHolderValue.getMappingConfig().getResultFrom())
                .map(field -> {
                    Object value = JsonPath.parse(json).read(field.getPath());

                    return ResultData.builder()
                            .row(requestBody.getRow())
                            .value(value)
                            .label(field.getLabel())
                            .build();
                })
                .collect(Collectors.toList());
    }

}
