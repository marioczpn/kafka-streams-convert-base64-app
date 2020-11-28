package com.github.marioczpn.kafka.streams.configuration;

import com.github.marioczpn.kafka.streams.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A Configuration object is responsible for specifying the settings for application.
 *
 * @author Mario Cezar Ponciano
 */
public class ConfigurationFile {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFile.class.getName());

    /**
     * Method is building a Kafka properties based on Key, Value information.
     *
     * @param envProps
     * @return Properties.
     */
    public Properties buildStreamsProperties(Properties envProps) {
        if (envProps == null) {
            final String errorMsg = "Environment properties is empty and this step won't be executed.";
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        logger.info("Building the streams properties... ");
        Properties props = new Properties();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, envProps.getProperty(Constants.APPLICATION_ID));
        props.put(StreamsConfig.CLIENT_ID_CONFIG, envProps.getProperty(Constants.CLIENT_ID));
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, envProps.getProperty(Constants.BOOTSTRAP_SERVERS));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.ByteArray().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        logger.info("Ends. ");
        return props;
    }

    /**
     * Reading a configuration file and setting a properties object.
     *
     * @param configFile
     * @return Properties
     * @throws IOException
     */
    public Properties loadEnvProperties(String configFile) throws IOException {
        logger.info("Loading the config.properties information... ");
        final Properties envProps = new Properties();

        if ( this.fromEnv() != null) {
            logger.info("Looking into the environment variables... ");
            return this.fromEnv();
        } else {
            logger.info("Looking into the config file... ");
            InputStream input = readConfigFile(configFile);
            envProps.load(input);
            input.close();
        }

        logger.info("Ends.");
        return envProps;
    }

    /**
     * Read a config file and if the isResourcesFile flag be enabled, it will use the resourcesFile instead of the
     * arguments file
     *
     * @param fileName
     * @return InputStream
     * @throws FileNotFoundException
     */
    private InputStream readConfigFile(String fileName) throws FileNotFoundException {
        InputStream input = null;
        if (StringUtils.isBlank(fileName)) {
            logger.info("Setting INTERNAL resource file.");
            input = getClass().getClassLoader().getResourceAsStream(Constants.INTERNAL_CONFIG_FILE_FROM_RESOURCES);
        } else {
            logger.info("Setting EXTERNAL config file sent by argument: " + fileName);
            input = new FileInputStream(fileName);
        }

        return input;
    }

    /**
     * Getting configuration from environment variable.
     *
     * @return Properties
     */
    public Properties fromEnv() {
        logger.info("Setting environment variables");

        final String appId = System.getenv(Constants.APPLICATION_ID_ENVVAR);
        final String clientId = System.getenv(Constants.CLIENT_ID_ENVVAR);
        final String bootStrapServer = System.getenv(Constants.BOOTSTRAP_SERVERS_ENVVAR);
        final String inputTopic =  System.getenv(Constants.INPUT_TOPIC_NAME_ENVVAR);
        final String streamsOutputTopicName = System.getenv(Constants.STREAMS_OUTPUT_TOPIC_NAME_ENVVAR);
        if(StringUtils.isBlank(appId) || StringUtils.isBlank(clientId) || StringUtils.isBlank(bootStrapServer) || StringUtils.isBlank(inputTopic) || StringUtils.isBlank(streamsOutputTopicName)) {
            logger.error("Environment variables are *EMPTY*. You can set an environment variable.");
            return null;
        }

        Properties envProps = new Properties();
        envProps.put(Constants.APPLICATION_ID, appId);
        envProps.put(Constants.CLIENT_ID, clientId);
        envProps.put(Constants.BOOTSTRAP_SERVERS, bootStrapServer);
        envProps.put(Constants.INPUT_TOPIC_NAME, inputTopic);
        envProps.put(Constants.STREAMS_OUTPUT_TOPIC_NAME, streamsOutputTopicName);

        logger.info("App is using the environment variable: " + envProps.toString());
        return envProps;
    }
}
