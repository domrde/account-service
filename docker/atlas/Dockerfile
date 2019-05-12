# See not updated https://github.com/Netflix-Skunkworks/zerotodocker/blob/master/atlas/1.4.3/Dockerfile

FROM netflixoss/java:8

RUN apt-get -qq update && \
    apt-get -qq -y install ca-certificates && \
    mkdir -p /apps/atlas && \
    wget -q -P /apps/atlas 'https://github.com/Netflix/atlas/releases/download/v1.5.3/atlas-1.5.3-standalone.jar' && \
    wget -q -P /apps/atlas 'https://raw.githubusercontent.com/Netflix/atlas/v1.5.x/conf/memory.conf'

EXPOSE 7101

ENTRYPOINT ["/usr/bin/java", "-jar", "/apps/atlas/atlas-1.5.3-standalone.jar", "/apps/atlas/memory.conf"]

CMD ["run"]
