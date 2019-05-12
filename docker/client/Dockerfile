FROM openjdk:8-jdk-alpine

RUN apk update && \
    apk upgrade && \
    apk add --no-cache bash git openssh gradle

RUN git clone https://github.com/domrde/account-service.git

WORKDIR account-service

RUN ./gradlew client:build -x test --stacktrace

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","client/build/libs/client-1.0.jar"]
