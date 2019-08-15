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
        collector.checkThat(config.getResultFrom().length, equalTo(3));
        collector.checkThat(config.getResultFrom()[0], equalTo("uid"));
        collector.checkThat(config.getResultFrom()[1], equalTo("СНИЛС"));
        collector.checkThat(config.getResultFrom()[2], equalTo("Дата выдачи СНИЛС"));
        collector.checkThat(config.getSearchBy().length, equalTo(2));
        collector.checkThat(config.getSearchBy()[0].getColumn(), equalTo("Дата рождения"));
        collector.checkThat(config.getSearchBy()[1].getColumn(), equalTo("Фамилия"));
    }

    @Test
    public void givenSampleYamlFullPathFileWhenReadMappingThenReturnMappingConfig() {
        // Act
        Optional<MappingConfig> mappingConfig = processor.readMappingConfig(FULL_FILE_PATH);
        MappingConfig config = mappingConfig.get();

        // Assert
        collector.checkThat(config.getResultFrom().length, equalTo(3));
        collector.checkThat(config.getResultFrom()[0], equalTo("uid"));
        collector.checkThat(config.getResultFrom()[1], equalTo("СНИЛС"));
        collector.checkThat(config.getResultFrom()[2], equalTo("Дата выдачи СНИЛС"));
        collector.checkThat(config.getSearchBy().length, equalTo(2));
        collector.checkThat(config.getSearchBy()[0].getColumn(), equalTo("Дата рождения"));
        collector.checkThat(config.getSearchBy()[1].getColumn(), equalTo("Фамилия"));
    }
}