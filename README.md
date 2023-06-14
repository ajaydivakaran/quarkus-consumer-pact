# quarkus-consumer-pact Project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

### Software requirements
* JDK 17
* Docker

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.


### To publish generated pacts
#### using Gradle
```shell script
./gradlew pactPublish
```

######  References
* https://github.com/pact-foundation/pact-broker-docker/blob/master/docker-compose.yml
* https://github.com/skattela/pact-workshop-jvm-quarkus
* https://github.com/pact-foundation/pact-jvm/tree/master/provider/gradle


#### using Docker CLI

```shell
export PACT_BROKER_BASE_URL="http://localhost:9292"
export GIT_BRANCH=main
export APP_VERSION=1.0.0
docker run -it --rm --network host -v ${PWD}/build/pacts:/tmp/pacts -e PACT_BROKER_BASE_URL \
pactfoundation/pact-cli:latest publish /tmp/pacts --consumer-app-version $APP_VERSION --branch $GIT_BRANCH
```

#### using standalone executable 

######  References
* [Refer executable documentation](https://github.com/pact-foundation/pact-ruby-standalone#pact-broker-client)
* [Installation](https://github.com/pact-foundation/pact-ruby-standalone/releases)

```shell
export PACT_BROKER_BASE_URL="http://localhost:9292"
export GIT_BRANCH=main
export APP_VERSION=1.0.0
<standalone-exec-folder>/bin/pact-broker publish ./build/pacts --consumer-app-version $APP_VERSION --branch $GIT_BRANCH
```

### Create environment
```shell
export PACT_BROKER_BASE_URL="http://localhost:9292"
docker run -it --rm --network host -e PACT_BROKER_BASE_URL pactfoundation/pact-cli:latest broker \
create-environment --name "e2e" --display-name "E2E" --no-production
```

### Can I deploy consumer
```shell
export PACT_BROKER_BASE_URL="http://localhost:9292"
docker run -it --rm --network host -e PACT_BROKER_BASE_URL pactfoundation/pact-cli:latest broker \
can-i-deploy --pacticipant Consumer --version commit1 --to-environment e2e
```

### Record deployment
```shell
export PACT_BROKER_BASE_URL="http://localhost:9292"
docker run -it --rm --network host -e PACT_BROKER_BASE_URL pactfoundation/pact-cli:latest broker \
record-deployment --pacticipant Consumer --version commit1 --environment e2e 
```

