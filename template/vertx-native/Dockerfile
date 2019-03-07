# Get a build environment
FROM oracle/graalvm-ce:1.0.0-rc13 as build-aot

# Set working dir
RUN mkdir /app
WORKDIR /app

# Add maven wrapper
ADD mvnw /app/
ADD .mvn /app/.mvn/
# build the function
ADD function /app/function
RUN ./mvnw -f /app/function/pom.xml package

# At this point the native image is built

# Create new image from alpine
FROM frolvlad/alpine-glibc:alpine-3.8
RUN apk add --no-cache ca-certificates

RUN addgroup -S app && adduser -S -g app app

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add ca-certificates curl \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSLf https://github.com/openfaas-incubator/of-watchdog/releases/download/0.4.6/of-watchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

WORKDIR /root/

RUN mkdir -p /home/app

# Wrapper/boot-strapper
WORKDIR /home/app
# Copy the SUNEC lib from graal to the final image
COPY --from=build-aot /opt/graalvm-ce-1.0.0-rc13/jre/lib/amd64/libsunec.so /usr/lib/libsunec.so
# Copy generated native executable from build-aot
COPY --from=build-aot /app/function/target/vertx-fn /usr/bin/vertx-fn

USER app

ENV cgi_headers="true"
ENV fprocess="vertx-fn"
ENV mode="http"
ENV upstream_url="http://127.0.0.1:8000"

ENV exec_timeout="20s"
ENV write_timeout="25s"
ENV read_timeout="25s"

HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
