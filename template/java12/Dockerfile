FROM openjdk:12-oraclelinux7 as builder

RUN yum install -y -q unzip

RUN mkdir -p /opt/ && cd /opt/ \
    && echo "Downloading gradle.." \
    && curl -sSfL https://services.gradle.org/distributions/gradle-5.5.1-bin.zip -o gradle-bin.zip \
    && unzip gradle-bin.zip -d /opt/ \
    && rm gradle-bin.zip

# Export some environment variables
ENV GRADLE_HOME=/opt/gradle-5.5.1/
ENV PATH=$PATH:$GRADLE_HOME/bin

RUN mkdir -p /home/app/libs

ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"
WORKDIR /home/app

COPY . /home/app/

RUN gradle build

FROM openfaas/of-watchdog:0.5.4 as watchdog

FROM openjdk:12-oraclelinux7 as ship

RUN yum install -y -q unzip

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

RUN groupadd --system app \
    && adduser app --system -g app

WORKDIR /home/app
COPY --from=builder /home/app/entrypoint/build/distributions/entrypoint-1.0.zip ./entrypoint-1.0.zip
RUN unzip ./entrypoint-1.0.zip

RUN chown -R app /home/app
USER app

ENV upstream_url="http://127.0.0.1:8082"
ENV mode="http"
ENV CLASSPATH="/home/app/entrypoint-1.0/lib/*"

ENV fprocess="java -XX:+UseContainerSupport com.openfaas.entrypoint.App"
EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
