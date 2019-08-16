package ru.taskdata;

import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import ru.taskdata.data.MappingConfig;

public class CmdProcessorTest {

    private static final String FULL_FILE_PATH =
            "/Users/max_tolstykh/projects/taskdata/ud-dit-search-tool/src/test/resources/sample.config.yaml";
    private static final String RELATED_FILE_PATH = "/sample.config.yaml";


    private final CmdProcessor processor = new CmdProcessor();

    @Rule
    public final ErrorCollector collector = new ErrorCollector();

    @Test
    public void givenSampleYamlRelatedPathFileWhenReadMappingThenReturnMappingConfig() {
        // Act
        Optional<MappingConfig> mappingConfig = processor.readMappingConfig(RELATED_FILE_PATH);
        MappingConfig config = mappingConfig.get();

        // Assert
        collector.checkThat(config.getResultFrom().length, equalTo(2));
        collector.checkThat(config.getResultFrom()[0].getPath(), equalTo("$.uid"));
        collector.checkThat(config.getResultFrom()[1].getPath(), equalTo("$.birth_dt"));

        collector.checkThat(config.getSearchBy().length, equalTo(2));
    }

    @Test
    public void givenSampleYamlFullPathFileWhenReadMappingThenReturnMappingConfig() {
        // Act
        Optional<MappingConfig> mappingConfig = processor.readMappingConfig(FULL_FILE_PATH);
        MappingConfig config = mappingConfig.get();

        // Assert
        collector.checkThat(config.getResultFrom().length, equalTo(2));
        collector.checkThat(config.getResultFrom()[0].getLabel(), equalTo("Уникальный идентификатор"));
        collector.checkThat(config.getResultFrom()[1].getLabel(), equalTo("Дата рождения"));

        collector.checkThat(config.getSearchBy().length, equalTo(2));
    }
}