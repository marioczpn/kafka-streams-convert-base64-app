![CI/CD Pipelines](https://github.com/marioczpn/kafka-streams-convert-base64-app/workflows/CI/CD%20Pipelines/badge.svg)

# Kafka Streams APP convert messages from topic to base64 format 

 This application will consume all messages from topic X, convert them to base64 format and produce them to topic Y.
 

##Requirements
- Java 11
- Junit 5
- Maven:
    - It is a maven project and all dependencies are available in [pom.xml](https://github.com/marioczpn/kafka-streams-convert-base64-app/blob/master/pom.xml)
 
* This project has being developed using the IntelliJ IDEA Community

# How to build this application?

This application has a Makefile available, you can build the application using:

- `make all` -> It will generate the jar file, docker image and push to your repository.


- You can define your repository into Makefile variables: 

Example: `DOCKER_REPO=quay.io/marioczpn`

If you only want to generate a:

- jar file: `make java_build`
- Docker Image: `make build` (Behind the scenes it's using the [dev.Dockerfile](https://github.com/marioczpn/kafka-streams-convert-base64-app/blob/master/dev.Dockerfile)

For more information please open the [Makefile](https://github.com/marioczpn/kafka-streams-convert-base64-app/blob/master/Makefile)

## CI/CD Pipelines

The application is using the github actions and it's using a different [Dockerfile](https://github.com/marioczpn/kafka-streams-convert-base64-app/blob/master/Dockerfile) to build/push.

# How to run this application?

To run the application locally you can use the jar generated into target folder:


- If you set the environment variables:


    export BOOTSTRAP_SERVERS_ENVVAR=127.0.0.1:9092\
    export APPLICATION_ID_ENVVAR=streams-convert-base64-project\
    export CLIENT_ID_ENVVAR=streams-convert-base64-project-client\
    export INPUT_TOPIC_NAME_ENVVAR=input-topic\
    export STREAMS_OUTPUT_TOPIC_NAME_ENVVAR=streams-output-topic\


you can run: `java -jar kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar`


- If you prefer, you can run using the [config/config.properties](https://github.com/marioczpn/kafka-streams-convert-base64-app/blob/master/config/config.properties)
`java -jar kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar config/config.properties`

    - Logs Information:
    
        
    [main] INFO com.github.marioczpn.kafka.streams.configuration.ConfigurationFile - Looking into the config file... 
    [main] INFO com.github.marioczpn.kafka.streams.configuration.ConfigurationFile - Setting EXTERNAL config file sent by argument: config/config.properties
    [main] INFO com.github.marioczpn.kafka.streams.configuration.ConfigurationFile - Ends.


- In the case if you forget to add a config and environment variable, it will pick up the [resources/config.properties](https://github.com/marioczpn/kafka-streams-convert-base64-app/blob/master/src/main/resources/config.properties)

## Deploy to Kubernetes
Please open the deployment automation to this project available [here](https://github.com/marioczpn/strimzi-kafka-cluster-deployment-automation)
