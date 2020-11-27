package com.github.marioczpn.kafka.streams.constants;

public class Constants {


    private Constants() {}

    public static final String APPLICATION_ID = "application.id";
    public static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
    public static final String INPUT_TOPIC_NAME = "input.topic.name";
    public static final String STREAMS_OUTPUT_TOPIC_NAME ="streams.output.topic.name";
    public static final String CLIENT_ID = "client.id";
    public static final String INTERNAL_CONFIG_FILE_FROM_RESOURCES = "config.properties";

    /* Environment variables */
     public static final String APPLICATION_ID_ENVVAR = "APPLICATION_ID_ENVVAR";
    public static final String CLIENT_ID_ENVVAR = "CLIENT_ID_ENVVAR";
    public static final String BOOTSTRAP_SERVERS_ENVVAR = "BOOTSTRAP_SERVERS_ENVVAR";
    public static final String INPUT_TOPIC_NAME_ENVVAR = "INPUT_TOPIC_NAME_ENVVAR";
    public static final String STREAMS_OUTPUT_TOPIC_NAME_ENVVAR = "STREAMS_OUTPUT_TOPIC_NAME_ENVVAR";


}
