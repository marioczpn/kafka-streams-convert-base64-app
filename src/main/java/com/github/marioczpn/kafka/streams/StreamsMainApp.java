/*
 * Create a simple application which will use Kafka Streams API. This application will
 * consume all messages from topic X, convert them to base64 format and produce them
 * to topic Y.
 * Java is preferable for Streams application. Also create a makefile and script,
 * which will build the application and build an image with the application inside and push it
 * to the internal Kubernetes registry or to dockerhub/quay.
 */
package com.github.marioczpn.kafka.streams;


import com.github.marioczpn.kafka.streams.controller.KafkaStreamsController;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Demonstrates how to perform an input data to  base64 output format using KafkaStreams
 *
 * @author Mario Cezar Ponciano
 */
public class StreamsMainApp {

    private static final Logger logger = LoggerFactory.getLogger(StreamsMainApp.class.getName());

    public static void main(final String[] args) {
        logger.info("Starting application... ");
        String configName = args.length < 1 ? "" : args[0];
        if (configName.isBlank()) {
            logger.info("This program is missing the path to an environment configuration file. Setting the internal config file.");
        }

        KafkaStreamsController controller = new KafkaStreamsController();

        try {
            controller.executeKafkaStreams(configName);
        } catch (IOException | InterruptedException e) {
            logger.info("Got an error:  " + e);
            throw new RuntimeException(e);
        }

        logger.info("End. ");
    }
}

