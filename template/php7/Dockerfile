# Start with the official Composer image and name it
FROM composer:1.7 AS composer

# Continue with the official PHP image
FROM php:7.2-alpine

# Copy the Composer PHAR from the Composer image into the PHP image
COPY --from=composer /usr/bin/composer /usr/bin/composer

# Composer requirements
ARG COMPOSER_AUTH='{}'
ENV COMPOSER_AUTH=${COMPOSER_AUTH}

# Add watchdog and packages
RUN apk add --no-cache git && \
    echo "Pulling watchdog binary from Github." && \
    curl -sSL https://github.com/openfaas/faas/releases/download/0.9.6/fwatchdog > /usr/bin/fwatchdog && \
    chmod +x /usr/bin/fwatchdog

# Create non-root user
RUN addgroup -S app && adduser -S -g app app && \
    mkdir -p /home/app

# Import function
WORKDIR /home/app
COPY index.php ./
WORKDIR /home/app/function

# Install PHP extensions
COPY ./function/php-extension.sh .
RUN [[ -f php-extension.sh ]] && sh ./php-extension.sh && \
    chown -R app /home/app

# Install Composer dependencies
COPY ./function/composer* ./
USER app
RUN [[ -f composer.lock || -f composer.json ]] && composer install --no-dev
USER root

# Cleanup
RUN apk del git && \
    rm -rf /usr/src/php && \
    { find /usr/local/lib -type f -print0 | xargs -0r strip --strip-all -p 2>/dev/null || true; }

# Copy the function source as very end to optimise building caches
COPY ./function/ .
RUN chown -R app /home/app

USER app
ENV fprocess="php index.php"
HEALTHCHECK --interval=3s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]
