FROM ghcr.io/openfaas/of-watchdog:0.8.4 as watchdog
FROM node:12-alpine as ship

ARG TARGETPLATFORM
ARG BUILDPLATFORM

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

RUN apk --no-cache add curl ca-certificates \
    && addgroup -S app && adduser -S -g app app

# Turn down the verbosity to default level.
ENV NPM_CONFIG_LOGLEVEL warn

RUN chmod 777 /tmp

USER app

RUN mkdir -p /home/app/server

# Wrapper/boot-strapper
WORKDIR /home/app/server
COPY package.json ./

# This ordering means the npm installation is cached for the outer function handler.
RUN npm i

# Copy outer function handler
COPY index.js ./

# COPY function node packages and install, adding this as a separate
# entry allows caching of npm install

WORKDIR /home/app/application

COPY function/*.json ./

RUN npm i

# COPY function files and folders
COPY function/ ./

# Run any tests that may be available
RUN npm test

# Set correct permissions to use non root user
WORKDIR /home/app/server

ENV cgi_headers="true"
ENV fprocess="node index.js"
ENV fprocess_debug="npm start"
ENV mode="http"
ENV upstream_url="http://127.0.0.1:3000"

ENV exec_timeout="10s"
ENV write_timeout="15s"
ENV read_timeout="15s"

ENV prefix_logs="false"

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]