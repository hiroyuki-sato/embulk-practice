package embulk.practice.spi;

//import org.embulk.EmbulkTestRuntime;
//import org.embulk.config.ConfigException;
import org.embulk.config.ConfigLoader;
import org.embulk.config.ConfigSource;
import org.embulk.spi.ExecInternal;
import org.embulk.test.TestingEmbulk;
import org.embulk.util.config.Config;
import org.embulk.util.config.ConfigDefault;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.embulk.util.config.Task;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class SpiTest
{
    @Rule
    //public EmbulkTestRuntime runtime = new EmbulkTestRuntime();
    public TestingEmbulk runtime = TestingEmbulk.builder().build();

    private static final ConfigMapperFactory CONFIG_MAPPER_FACTORY = ConfigMapperFactory
            .builder()
            .addDefaultModules()
            .build();
    private static final ConfigMapper CONFIG_MAPPER = CONFIG_MAPPER_FACTORY.createConfigMapper();

    public interface PluginTask extends Task
    {
        @Config("option1")
        @ConfigDefault("\"AND\"")
        @Deprecated
        public String getOption1();
    }
/*
    @Test
    public void basicSPITest() {

        ConfigSource config = configFromYamlString(
//                "type: csv",
                "columns:",
                "- {name: a}",
                "drop_columns:",
                "- {name: a}");
        PluginTask task = CONFIG_MAPPER.map(config,PluginTask.class);

//        assertThrows("ConfigExceptin", ConfigException.class, () -> CONFIG_MAPPER.map(config,PluginTask.class));
System.out.println("****************");
        assertTrue("hogehoge", true);
    }
*/

    public ConfigSource configFromYamlString(String... lines)
    {
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append("\n");
        }
        String yamlString = builder.toString();

        ConfigLoader loader = new ConfigLoader(ExecInternal.getModelManager());
        return loader.fromYamlString(yamlString);
    }

}
