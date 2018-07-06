FROM node:8.9.1-alpine

RUN addgroup -S app && adduser app -S -G app

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add curl \
    && echo "Pulling watchdog binary from Github." \
    && curl -sSL https://github.com/openfaas/faas/releases/download/0.8.0/fwatchdog > /usr/bin/fwatchdog \
    && chmod +x /usr/bin/fwatchdog \
    && apk del curl --no-cache

WORKDIR /root/

# Turn down the verbosity to default level.
ENV NPM_CONFIG_LOGLEVEL warn

RUN mkdir -p /home/app

# Wrapper/boot-strapper
WORKDIR /home/app
COPY package.json ./

# This ordering means the npm installation is cached for the outer function handler.
RUN npm i --production

# Copy outer function handler
COPY index.js ./

# COPY function node packages and install, adding this as a separate
# entry allows caching of npm install runtime dependencies
WORKDIR /home/app/function
COPY function/*.json ./
RUN npm i --production || :

# Copy in additional function files and folders
COPY --chown=app:app function/ .

WORKDIR /home/app/

# chmod for tmp is for a buildkit issue (@alexellis)
RUN chmod +rx -R ./function \
    && chown app:app -R /home/app \
    && chmod 777 /tmp

USER app

ENV cgi_headers="true"
ENV fprocess="node index.js"

HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
