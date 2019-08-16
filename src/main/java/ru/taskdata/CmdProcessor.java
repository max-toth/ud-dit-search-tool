package ru.taskdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import ru.taskdata.data.InputDataHolder;
import ru.taskdata.data.MappingConfig;
import ru.taskdata.exceptions.MappingConfigParseException;

public class CmdProcessor {

    private static final Logger log = LogManager.getLogger(CmdProcessor.class);

    private static final String CONFIG_OPT = "config";
    private static final String FILES_OPT = "files";
    private static final String THREADS_OPT = "threads";
    private static final String CSV_EXT = "csv";

    private final Options options;

    public CmdProcessor() {
        options = new Options();

        options.addOption(Option.builder().longOpt(CONFIG_OPT)
                .desc("PATH to config file")
                .hasArg()
                .argName("PATH")
                .build());

        options.addOption(Option.builder().longOpt(FILES_OPT)
                .desc("PATH to input files")
                .hasArg()
                .argName("PATH")
                .build());

        options.addOption(Option.builder().longOpt(THREADS_OPT)
                .desc("NUM of runnable threads")
                .hasArg()
                .argName("NUM")
                .build());
    }

    public Optional<InputDataHolder> read(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (!cmd.hasOption(CONFIG_OPT) && !cmd.hasOption(FILES_OPT) && cmd.hasOption(THREADS_OPT)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ud-dit-search-tool", options);
                return Optional.empty();
            } else {
                String configFile = cmd.getOptionValue(CONFIG_OPT);
                String inputFilesPath = cmd.getOptionValue(FILES_OPT);

                Collection<File> files = FileUtils.listFiles(new File(inputFilesPath), new String[]{CSV_EXT}, false);

                MappingConfig mappingConfig = readMappingConfig(configFile)
                        .orElseThrow(() -> new MappingConfigParseException("No mapping configuration found"));

                return Optional.of(InputDataHolder.builder()
                        .files((List<File>) files)
                        .threads((int) cmd.getParsedOptionValue(THREADS_OPT))
                        .mappingConfig(mappingConfig)
                        .build());
            }
        } catch (ParseException | MappingConfigParseException e) {
            log.error("Failed to parse input parameters from " + Arrays.toString(args), e);
            return Optional.empty();
        }
    }

    @VisibleForTesting
    static Optional<MappingConfig> readMappingConfig(String configFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (InputStream resource = CmdProcessor.class.getResourceAsStream(configFile)) {
            if (null == resource) {
                try (FileInputStream fileResource = new FileInputStream(configFile)) {
                    return Optional.of(mapper.readValue(fileResource, MappingConfig.class));
                }
            } else {
                return Optional.of(mapper.readValue(resource, MappingConfig.class));
            }
        } catch (IOException e) {
            log.error("Failed to parse configuration from [" + configFile + "]", e);
            return Optional.empty();
        }
    }
}
