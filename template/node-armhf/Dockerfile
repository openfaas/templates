FROM arm32v6/node:10.15.0-alpine

RUN apk --no-cache add ca-certificates curl \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.9.14/fwatchdog-armhf > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

WORKDIR /root/

# Turn down the verbosity to default level.
ENV NPM_CONFIG_LOGLEVEL warn

# Wrapper/boot-strapper
COPY package.json       .
RUN npm i --production

# Function
COPY index.js           .
RUN mkdir -p ./root/function

COPY function/*.json    ./function/
WORKDIR /root/function
RUN npm i --production || :
WORKDIR /root/
COPY function           function

WORKDIR /root/

ENV cgi_headers="true"
ENV fprocess="node index.js"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]