package com.github.marioczpn.kafka.streams.configuration;

import com.github.marioczpn.kafka.streams.constants.Constants;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationFileTest {
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFileTest.class.getName());

    /**
     * This method is setting envinroment variable programatically.
     *
     * @param mapEnvVariable
     * @throws Exception
     */
    private static void setEnvironmentVariable(Map<String, String> mapEnvVariable) throws Exception {
        try {

            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);

            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(mapEnvVariable);

            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);

            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(mapEnvVariable);

        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(mapEnvVariable);
                }
            }
        }
    }

    @Test
    void shouldNotAllowNullThrowsIllegalArgumentException() {
        ConfigurationFile configFile = new ConfigurationFile();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            configFile.buildStreamsProperties(null);
        });
    }

    @Test
    void shouldBuildStreamsProperties() {
        Properties props = new Properties();
        props.put(Constants.INPUT_TOPIC_NAME, "test");
        props.put(Constants.STREAMS_OUTPUT_TOPIC_NAME, "dummy:1234");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "appId");
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "clientId");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        ConfigurationFile configFile = new ConfigurationFile();
        Properties streamsProperties = configFile.buildStreamsProperties(props);

        Assertions.assertNotNull(streamsProperties);
    }

    @Test
    void shouldLoadEnvPropertiesFromResourceFile() throws Exception {
        Map<String, String> envVariablesMap = new HashMap<>();
        envVariablesMap.put(Constants.APPLICATION_ID_ENVVAR, "");
        setEnvironmentVariable(envVariablesMap);

        ConfigurationFile configFile = new ConfigurationFile();
        Properties streamsProperties = configFile.loadEnvProperties(null);

        Assertions.assertNotNull(streamsProperties);
    }
    

    @Test
    void shouldReturnNullNoEnvironmentVariableDefined() throws Exception {
        Map<String, String> envVariablesMap = new HashMap<>();
        envVariablesMap.put(Constants.APPLICATION_ID_ENVVAR, "");
        setEnvironmentVariable(envVariablesMap);

        ConfigurationFile configFile = new ConfigurationFile();
        Properties envVariables = configFile.fromEnv();

        Assertions.assertNull(envVariables);
    }

    @Test
    void shouldLoadEnvPropertiesFromEnvironmentVariable() throws Exception {
        Map<String, String> envVariablesMap = new HashMap<>();
        envVariablesMap.put(Constants.APPLICATION_ID_ENVVAR, "appId");
        envVariablesMap.put(Constants.CLIENT_ID_ENVVAR, "clientId");
        envVariablesMap.put(Constants.BOOTSTRAP_SERVERS_ENVVAR, "bootStrapServer");
        envVariablesMap.put(Constants.INPUT_TOPIC_NAME_ENVVAR, "inputTopic");
        envVariablesMap.put(Constants.STREAMS_OUTPUT_TOPIC_NAME_ENVVAR, "streamsOutputTopicName");
        setEnvironmentVariable(envVariablesMap);

        ConfigurationFile configFile = new ConfigurationFile();
        Properties streamsProperties = configFile.loadEnvProperties(null);

        Assertions.assertNotNull(streamsProperties);
    }
}
