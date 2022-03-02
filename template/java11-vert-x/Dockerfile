FROM openjdk:11-slim as builder

ENV GRADLE_VER=7.4
RUN apt-get update -qqy \
  && apt-get install -qqy \
   --no-install-recommends \
   curl \
   ca-certificates \
   unzip

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

RUN gradle shadowJar
RUN find . 

FROM ghcr.io/openfaas/of-watchdog:0.9.3 as watchdog
FROM openjdk:11-slim as ship

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

WORKDIR /home/app
COPY --from=builder /home/app/entrypoint/build/libs/entrypoint-1.0-all.jar ./entrypoint-1.0.jar

RUN addgroup --system app \
    && adduser --system --ingroup app app

WORKDIR /home/app/
RUN chown -R app /home/app

USER app

ENV upstream_url="http://127.0.0.1:8082"
ENV mode="http"

ENV fprocess="java -XX:+UseContainerSupport -jar /home/app/entrypoint-1.0.jar"
EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
