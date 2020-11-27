APP_NAME=kafka-streams-convert-base64-app
APP_VERSION=latest
DOCKER_REPO=quay.io/marioczpn
DOCKER_EMAIL=
DOCKER_USERNAME=
DOCKER_PASSWORD=
DOCKER_CMD ?= docker
DOCKER_REGISTRY=$(DOCKER_REPO)/${APP_NAME}:${APP_VERSION}

# HELP
# This will output the help for each task
.PHONY: help build verify push $(CMDS) \
	verify_deps verify_chart

help:
	# This Makefile provides Build an image and push to container registry.
	#
	#
	### Generate targets
	#
	# generate           - regenerate all generated files
	#
	### Build targets
    #
	# all             - builds maven application, docker image, push to container registry.
	#
	#

# Alias targets
###############

# DOCKER TASKS
# Build the container
build: ## Build the container
	${DOCKER_CMD} build -t ${DOCKER_REGISTRY} .

login: ## Login to docker hub
	${DOCKER_CMD} login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}

push: ## push the latest Docker image to DockerHub
	${DOCKER_CMD}  push ${DOCKER_REGISTRY}

run: ## Run container with config/config.properties
	${DOCKER_CMD} run -t $(APP_NAME):$(APP_VERSION) config/config.properties --name="$(APP_NAME)" $(APP_NAME)

shell: ## run an interactive bash session in the container
	${DOCKER_CMD} run -it $(APP_NAME):$(APP_VERSION) config/config.properties --name="$(APP_NAME)" $(APP_NAME) /bin/bash

stop: ## Stop and remove a running container
	${DOCKER_CMD} stop $(APP_NAME); docker rm $(APP_NAME)

deploy: build push

java_build:
	echo "Building JAR file ..."
	mvn $(MVN_ARGS) verify

java_install_root:
	echo "Installing root pom ..."
	mvn $(MVN_ARGS) install -f pom.xml -N

java_clean:
	echo "Cleaning Maven build ..."
	mvn $(MVN_ARGS) clean

all: java_build deploy

clean: java_clean