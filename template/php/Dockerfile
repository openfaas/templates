FROM openfaas/of-watchdog:0.5.3 as watchdog

# start with the official Composer image and name it
FROM composer:1.7 AS composer

FROM php:7-alpine

RUN echo http://nl.alpinelinux.org/alpine/edge/testing >> /etc/apk/repositories && apk update && \
    apk add --no-cache bash \
    openssh-client \
    wget \
    nginx \
    supervisor \
    curl \
    git \
    nodejs \
    nodejs-npm

COPY --from=watchdog /fwatchdog /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# copy the Composer PHAR from the Composer image into the PHP image
COPY --from=composer /usr/bin/composer /usr/bin/composer

# Composer requirements
ARG COMPOSER_AUTH='{}'
ENV COMPOSER_AUTH=${COMPOSER_AUTH}

# create non-root user
RUN addgroup -S app && adduser -S -g app app && \
    mkdir -p /home/app

# Import function
WORKDIR /home/app
COPY index.php ./
COPY Response.php ./
COPY composer.json ./
COPY ./function ./function
COPY ./script.sh ./script.sh

USER app

RUN composer install --no-dev


# NODE

COPY index.js ./index.js
COPY package.json ./package.json
RUN npm install

USER root

WORKDIR /home/app/function

# Install php extensions
RUN [[ -f php-extension.sh ]] && sh ./php-extension.sh && \
    chown -R app /home/app

# Entry
USER app

# Install Composer Dependecies
RUN [[ -f composer.lock || -f composer.json ]] && composer install --no-dev

USER root

# Cleanup
RUN apk del git && \
    rm -rf /usr/src/php && \
    { find /usr/local/lib -type f -print0 | xargs -0r strip --strip-all -p 2>/dev/null || true; }

# Set correct permissions to use non root user
WORKDIR /home/app/

# chmod for tmp is for a buildkit issue (@alexellis)
RUN chown app:app -R /home/app \
    && chmod 777 /tmp


USER app

WORKDIR /home/app

ENV cgi_headers="true"
ENV fprocess="node index.js"
ENV mode="http"
ENV upstream_url="http://127.0.0.1:3000"

ENV exec_timeout="10s"
ENV write_timeout="15s"
ENV read_timeout="15s"
EXPOSE 8080
HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1

CMD ["/bin/sh"]