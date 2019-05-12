FROM openjdk:8-jdk-alpine

RUN apk update && \
    apk upgrade && \
    apk add --no-cache bash git openssh gradle postgresql-client busybox-extras

RUN git clone https://github.com/domrde/account-service.git

WORKDIR account-service

RUN ./gradlew server:build -x test --stacktrace

ADD wait-for-others.sh /
RUN chmod +x /wait-for-others.sh

EXPOSE 8080

ENTRYPOINT ["/wait-for-others.sh","java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","server/build/libs/server-1.0.jar"]
