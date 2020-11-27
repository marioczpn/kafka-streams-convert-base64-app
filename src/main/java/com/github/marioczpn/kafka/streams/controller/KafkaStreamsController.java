package com.github.marioczpn.kafka.streams.controller;

import com.github.marioczpn.kafka.streams.configuration.ConfigurationFile;
import com.github.marioczpn.kafka.streams.processor.Base64Processor;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class KafkaStreamsController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamsController.class.getName());

    public void executeKafkaStreams(final String configFile) throws IOException, InterruptedException {
        KafkaStreams kstreams = this.prepareKafkaStreams(configFile);
        this.startKafkaStreamsSynch(kstreams);
    }

    /**
     * It's preparing the Kafka Streams loading the environment configuration file and invoking the processor to convert
     * the data to base64's format
     *
     * @param configFile
     * @return KafkaStreams
     * @throws IOException
     */
    private KafkaStreams prepareKafkaStreams(final String configFile) throws IOException {
        logger.info("Preparing the Kafka Streams object... ");

        /* Loading properties information */
        final ConfigurationFile configuration = new ConfigurationFile();
        final Properties envProps = configuration.loadEnvProperties(configFile);
        final Properties streamProps = configuration.buildStreamsProperties(envProps);
        logger.info("Properties: " + streamProps.toString());

        /* Invoke the processor to convert the data to base64's format*/
        Base64Processor base64Processor = new Base64Processor();
        Topology topology = base64Processor.buildBase64Topology(envProps);

        KafkaStreams streams = new KafkaStreams(topology, streamProps);

        logger.info("Ends. ");
        return streams;
    }

    private void startKafkaStreamsSynch(final KafkaStreams streams) throws InterruptedException {
        logger.info("Starting Kafka Streams synchronously... ");

        final CountDownLatch latch = new CountDownLatch(1);
        streams.setStateListener((newState, oldState) -> {
            if (oldState == KafkaStreams.State.REBALANCING && newState == KafkaStreams.State.RUNNING) {
                latch.countDown();
            }
        });

        streams.start();

        logger.info("Latch waiting... ");
        latch.await();

    }

}
