FROM maven:3.6.0-jdk-8-alpine as builder

RUN mkdir -p /home/app/libs

ENV MAVEN_OPTS=""
RUN mkdir -p /root/.m2/repository/com/openfaas/template/java

WORKDIR /home/app

COPY . /home/app/

RUN cp -v -R  /home/app/function/lib/com/openfaas/* /root/.m2/repository/com/openfaas


WORKDIR /home/app

RUN  mvn clean package

FROM openjdk:8u131-jdk-alpine as ship
RUN apk --no-cache add curl \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas-incubator/of-watchdog/releases/download/0.4.6/of-watchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl

RUN addgroup -S app && adduser -S -g app app
WORKDIR /home/app
COPY --from=builder /home/app/function/target/function-1.0.0-jar-with-dependencies.jar ./function-1.0.0.jar
RUN unzip ./function-1.0.0.jar

RUN chown -R app /home/app
USER app

ENV upstream_url="http://127.0.0.1:8082"
ENV mode="http"
#ENV CLASSPATH="/home/app/function-1.0.0/lib/*"

ENV fprocess="java -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap com.openfaas.entrypoint.App"
EXPOSE 8080

HEALTHCHECK --interval=2s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
