# start with the official Composer image and name it
FROM composer:1.7.1 AS composer

# continue with the official PHP image
FROM php:7.2-alpine

# copy the Composer PHAR from the Composer image into the PHP image
COPY --from=composer /usr/bin/composer /usr/bin/composer

# Composer requirements
ARG COMPOSER_AUTH='{}'
ENV COMPOSER_AUTH=${COMPOSER_AUTH}

# Add git
RUN apk add --no-cache git

# Add watchdog
RUN echo "Pulling watchdog binary from Github."
RUN curl -sSL https://github.com/openfaas/faas/releases/download/0.9.4/fwatchdog > /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# create non-root user
RUN addgroup -S app && adduser -S -g app app
RUN mkdir -p /home/app

# Core
WORKDIR /home/app

# Function
COPY index.php php-extension.sh composer.* ./
RUN chmod u+x ./php-extension.sh && ./php-extension.sh
COPY ./function ./function
WORKDIR /home/app/function

# Install Dependecies
RUN [[ -f php-extension.sh ]] && sh ./php-extension.sh || \
    echo "php-extension.sh does not exist, skip installing PHP extensions"
RUN [[ -f composer.lock || -f composer.json ]] && composer install --no-dev || \
    echo "composer.lock does not exist, skip installing composer dependencies"

# Cleanup
RUN apk del git
RUN rm -rf /usr/src/php
RUN { find /usr/local/lib -type f -print0 | xargs -0r strip --strip-all -p 2>/dev/null || true; }
RUN chown -R app /home/app

# Entry
USER app
WORKDIR /home/app
ENV fprocess="php index.php"
HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]
