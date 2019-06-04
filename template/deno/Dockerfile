FROM frolvlad/alpine-glibc:alpine-3.9

# Allows you to add additional packages via build-arg
ARG ADDITIONAL_PACKAGE
ARG DENO_VERSION=0.3.6

RUN addgroup -S app && adduser -S -g app app

# Alternatively use ADD https:// (which will not be cached by Docker builder)
RUN apk --no-cache add curl ca-certificates ${ADDITIONAL_PACKAGE} \
    && echo "Pulling deno binary from Github." \
    && curl -fsSL https://github.com/denoland/deno/releases/download/v${DENO_VERSION}/deno_linux_x64.gz -o - | gzip -d -c - > /usr/bin/deno \
    && chmod +x /usr/bin/deno \
    && apk del curl

USER app

WORKDIR /home/app/

# Copy outer function handler
COPY mod.ts ./
COPY package.ts ./

# download/cache base packages
RUN deno --prefetch package.ts

# COPY function node packages and install, adding this as a separate
# entry allows caching of npm install
WORKDIR /home/app/function

# COPY function files and folders
COPY function/ ./

# Set correct permissions to use non root user
WORKDIR /home/app/

# download/cache packages for mod.ts and handler.ts
RUN deno --prefetch mod.ts

ENV exec_timeout="10s"
ENV write_timeout="15s"
ENV read_timeout="15s"
EXPOSE 8080

HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["deno", "mod.ts", "--allow-net", "--allow-write"]

