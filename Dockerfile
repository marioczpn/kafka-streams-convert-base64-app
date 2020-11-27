FROM adoptopenjdk/openjdk11:alpine-slim

ARG JAR_FILE=target/kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar
ARG JAR_LIB_FILE=target/lib/
ARG EXTERNAL_CONFIG_FILE=target/config/config.properties

# cd /usr/local/runme
WORKDIR /usr/src/app

# copy target/kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar /usr/src/app/kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar
COPY .target/kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar /usr/src/app/kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar

# copy project dependencies
# cp -rf target/lib/  /usr/src/app/lib
ADD ${JAR_LIB_FILE} /usr/src/app/lib/

# copy target/config/config.properties /usr/src/app/
COPY ${EXTERNAL_CONFIG_FILE} /usr/src/app/config/config.properties

# java -jar /usr/src/app/kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar config/config.properties
ENTRYPOINT ["java","-jar","kafka-streams-convert-base64-app-1.0-SNAPSHOT.jar"]