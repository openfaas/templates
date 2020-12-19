FROM openjdk:11-jdk-slim as builder

ENV MAVEN_VER=3.6.3
RUN apt-get update -qqy \
  && apt-get install -qqy \
   --no-install-recommends \
   curl \
   ca-certificates \
   unzip

RUN mkdir -p /opt/ && cd /opt/ \
    && echo "Downloading maven.." \
    && curl -sSfL "https://mirrors.estointernet.in/apache/maven/maven-3/${MAVEN_VER}/binaries/apache-maven-${MAVEN_VER}-bin.zip" -o apache-maven-$MAVEN_VER-bin.zip \
    && unzip apache-maven-$MAVEN_VER-bin.zip -d /opt/ \
    && rm apache-maven-$MAVEN_VER-bin.zip

# Export some environment variables
ENV MAVEN_HOME=/opt/apache-maven-$MAVEN_VER/
ENV PATH=$PATH:$MAVEN_HOME/bin

WORKDIR /home/app

COPY . /home/app/

RUN mvn clean package
RUN find . 

FROM openfaas/of-watchdog:0.7.6 as watchdog

FROM openjdk:11-jre-slim as ship
RUN apt-get update -qqy \
  && apt-get install -qqy \
   --no-install-recommends \
   unzip
RUN addgroup --system app \
    && adduser --system --ingroup app app

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

WORKDIR /home/app
COPY --from=builder /home/app/function/target/function-0.1.0-bin.zip ./function-0.1.0.zip
user app
RUN unzip ./function-0.1.0.zip

WORKDIR /home/app/

ENV upstream_url="http://127.0.0.1:8082"
ENV mode="http"
ENV CLASSPATH="/home/app/function-0.1.0/function-0.1.0.jar:/home/app/function-0.1.0/lib/*"

ENV fprocess="java -XX:+UseContainerSupport com.openfaas.entrypoint.App"
EXPOSE 8080

HEALTHCHECK --interval=5s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]