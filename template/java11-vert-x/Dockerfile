FROM openjdk:11-jdk-slim as builder

RUN apt-get update -qqy \
  && apt-get install -qqy \
   --no-install-recommends \
   curl \
   ca-certificates \
   unzip

ENV GRADLE_VER=7.3.3
RUN mkdir -p /opt/ && cd /opt/ \
    && echo "Downloading gradle.." \
    && curl -sSfL "https://services.gradle.org/distributions/gradle-${GRADLE_VER}-bin.zip" -o gradle-$GRADLE_VER-bin.zip \
    && unzip gradle-$GRADLE_VER-bin.zip -d /opt/ \
    && rm gradle-$GRADLE_VER-bin.zip

# Export some environment variables
ENV GRADLE_HOME=/opt/gradle-$GRADLE_VER/
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN mkdir -p /home/app/libs

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"
WORKDIR /home/app

COPY . /home/app/

RUN gradle build

FROM ghcr.io/openfaas/of-watchdog:0.9.10 as watchdog
FROM openjdk:11-jre-slim as ship

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

WORKDIR /home/app
COPY --from=builder /home/app/entrypoint/build/libs/app.jar ./app.jar

RUN addgroup --system app \
    && adduser --system --ingroup app app

WORKDIR /home/app/
RUN chown -R app /home/app

USER app

ENV upstream_url="http://127.0.0.1:8082"
ENV mode="http"
# ENV CLASSPATH="/home/app/entrypoint-1.0/lib/*"

ENV fprocess="java -XX:+UseContainerSupport -Dvertx.cacheDirBase=/tmp/.vertx-cache -jar app.jar"
EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
