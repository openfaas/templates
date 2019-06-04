FROM node:11-alpine

# Allows you to add additional packages via build-arg
ARG ADDITIONAL_PACKAGE

# add non-root user
RUN addgroup -S app && adduser -S -g app app

WORKDIR /home/app/

# Turn down the verbosity to default level.
ENV NPM_CONFIG_LOGLEVEL warn

# Wrapper/boot-strapper
COPY package.json ./

# This ordering means the npm installation is cached for the outer function handler.
RUN npm i

# copy patched prometheus client
COPY node_modules/prom-client ./node_modules/prom-client

# Copy outer function handler
COPY index.js ./

# COPY function node packages and install, adding this as a separate
# entry allows caching of npm install
WORKDIR /home/app/function
COPY function/*.json ./
RUN npm i || :

# COPY function files and folders
COPY function/ ./

# Set correct permissions to use non root user
WORKDIR /home/app/

# workaround for faas-netes
RUN touch /tmp/.lock

# chmod for tmp is for a buildkit issue (@alexellis)
RUN chown app:app -R /home/app \
    && chmod 777 /tmp

USER app

EXPOSE 8080

HEALTHCHECK --interval=3s --timeout=3s \
   CMD node /healthcheck.js

# try to make it harder to exploit spectre/meltdown
CMD ["node", "--untrusted-code-mitigations", "index.js"]

