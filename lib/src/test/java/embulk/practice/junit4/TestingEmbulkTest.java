package embulk.practice.junit4;

import com.google.common.io.Resources;
import org.embulk.EmbulkSystemProperties;
import org.embulk.config.ConfigSource;
import org.embulk.formatter.csv.CsvFormatterPlugin;
import org.embulk.input.file.LocalFileInputPlugin;
import org.embulk.output.file.LocalFileOutputPlugin;
import org.embulk.parser.csv.CsvParserPlugin;
import org.embulk.spi.FileInputPlugin;
import org.embulk.spi.FileOutputPlugin;
import org.embulk.spi.FormatterPlugin;
import org.embulk.spi.ParserPlugin;
import org.embulk.test.TestingEmbulk;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.junit.Rule;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TestingEmbulkTest
{
    private static final EmbulkSystemProperties EMBULK_SYSTEM_PROPERTIES;

    private static final ConfigMapperFactory CONFIG_MAPPER_FACTORY = ConfigMapperFactory.builder().build();
    private static final ConfigMapper CONFIG_MAPPER = CONFIG_MAPPER_FACTORY.createConfigMapper();


    static {
        final Properties properties = new Properties();
        properties.setProperty("default_guess_plugins", "gzip,bzip2,json,csv");
        String jrubyPath = "file://" + System.getenv("HOME")  + "/.embulk/jars/jruby-complete-9.2.19.0.jar";
System.out.println(jrubyPath);
        String embulkHome = Resources.getResource("embulk").getPath();
        properties.setProperty("embulk_home","/tmp/embulk");
        System.out.println(embulkHome);
        properties.setProperty("jruby",jrubyPath);
        EMBULK_SYSTEM_PROPERTIES = EmbulkSystemProperties.of(properties);
    }



    @Rule
    public TestingEmbulk embulk = TestingEmbulk
            .builder()
            .setEmbulkSystemProperties(EMBULK_SYSTEM_PROPERTIES)
            .registerPlugin(FileInputPlugin.class,"file", LocalFileInputPlugin.class)
            .registerPlugin(FileOutputPlugin.class,"file", LocalFileOutputPlugin.class)
            .registerPlugin(ParserPlugin.class, "csv", CsvParserPlugin.class)
            .registerPlugin(FormatterPlugin.class, "csv", CsvFormatterPlugin.class)
            .build();

    @Test
    public void basicEmbulkTest()
    {
        ConfigSource testConfig = testConfig();
        System.out.println(testConfig);
        System.out.println("tet");
        final Path out = embulk.createTempFile("csv");
        try {
            embulk.runInput(testConfig,out);
            System.out.println(Files.readString(out));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private ConfigSource testConfig() {
        return CONFIG_MAPPER_FACTORY.newConfigSource()
                .set("type","file")
                .set("path_prefix", Resources.getResource("sample_01.csv").getPath())
                .set("parser",parserConfig());

/*
                .set("in",inputSourceConfig())
                .set("out",outputSourceConfig());
*/
    }

    private Map<String,Object> inputSourceConfig(){
        Map<String,Object> map = new HashMap<>();
        map.put("type","file");
        map.put("path_prefix", Resources.getResource("sample_01.csv").getPath());
        map.put("parser",parserConfig());
        return Collections.unmodifiableMap(map);
    };
    private Map<String,Object> parserConfig() {
        Map<String,Object> map = new HashMap<String,Object>() {
        {
            put("charset","UTF-8");
            put("newline","LF");
            put("type","csv");
            put("delimiter",",");
            put("quote","\"");
            put("escape","\"");
            put("null_string","NULL");
            put("trim_if_not_quoted",false);
            put("skip_header_lines",1);
            put("allow_extra_columns",false);
            put("allow_optional_columns",false);
            put("columns",csvParserColumns());
        }};
        return Collections.unmodifiableMap(map);
    }
    private List<Object> csvParserColumns()
    {
        List<Map<String, Object>> columns = new ArrayList<>();
        columns.add(
                new HashMap<String, Object>()
                {{
                    put("name", "id");
                    put("type", "string");
                }}
        );
        columns.add(
            new HashMap<String, Object>()
            {{
                put("name", "account");
                put("type", "long");
            }}
        );
        columns.add(
            new HashMap<String, Object>()
            {{
                put("name", "time");
                put("type", "timestamp");
                put("format", "%Y-%m-%d %H:%M:%S");
            }}
        );
        columns.add(
                new HashMap<String, Object>()
                    {{
                        put("name", "purchase");
                        put("type", "timestamp");
                        put("format", "%Y%m%d");
                    }}
        );
        columns.add(
                new HashMap<String, Object>()
                {{
                    put("name", "comment");
                    put("type", "string");
                }}
        );
        return Collections.unmodifiableList(columns);
    }
    private Map<String,Object> outputSourceConfig() {
        Map<String,Object>map = new HashMap<String,Object>(){{
           put("type","stdout");
        }};
        return Collections.unmodifiableMap(map);
    }
}
