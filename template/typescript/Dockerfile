FROM node:8.9.1-alpine AS build

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

# Copy package and tsconfig json
WORKDIR /home/app
COPY *.json ./

# This ordering means the npm installation is cached for the outer function handler.
RUN npm i

# Copy outer function handler
COPY index.ts ./

# COPY function node packages and install, adding this as a separate
# entry allows caching of npm install runtime dependencies
WORKDIR /home/app/function
COPY function/*.json ./

# Create an empty node_modules to ensure the directory is copied
# to the final image even if there are no additional modules
RUN mkdir node_modules \
    && npm i 

# COPY function files and folders
COPY function/ ./

# Compile the .ts files
RUN cd ../; npm run build

# Final function image
FROM node:8.9.1-alpine

# Copy watchdog and the compiled JS and node_modules over
COPY --from=build /usr/bin/fwatchdog /usr/bin/fwatchdog
COPY --from=build /home/app/out /home/app
COPY --from=build /home/app/node_modules /home/app/node_modules
COPY --from=build /home/app/function/node_modules /home/app/function/node_modules

# Set correct permissions to use non root user
RUN addgroup -S app && adduser -S -g app app
WORKDIR /home/app/

# chmod for tmp is for a buildkit issue (@alexellis)
RUN chown app:app -R /home/app \
    && chmod 777 /tmp

USER app

ENV cgi_headers="true"
ENV fprocess="node index.js"

HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1

CMD ["fwatchdog"]
