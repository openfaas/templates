FROM php:7-alpine

# Composer requirements
ARG COMPOSER_AUTH='{}'
ENV COMPOSER_AUTH=${COMPOSER_AUTH}
RUN curl -sS https://getcomposer.org/installer | php -- --install-dir=/usr/local/bin --filename=composer
RUN apk add --no-cache git

# Add watchdog
RUN echo "Pulling watchdog binary from Github."
RUN curl -sSL https://github.com/openfaas/faas/releases/download/0.8.0/fwatchdog > /usr/bin/fwatchdog
RUN chmod +x /usr/bin/fwatchdog

# Core
WORKDIR /root/

# Function
COPY index.php php-extension.sh composer.* ./
RUN chmod u+x ./php-extension.sh && ./php-extension.sh
COPY ./function/ ./function/
WORKDIR /root/function/

# Install Dependecies
RUN [[ -f php-extension.sh ]] && sh ./php-extension.sh || \
    echo "php-extension.sh does not exist, skip installing PHP extensions"
RUN [[ -f composer.lock || -f composer.json ]] && composer install --no-dev || \
    echo "composer.lock does not exist, skip installing composer dependencies"

# Cleanup
RUN apk del git
RUN rm -rf /usr/src/php
RUN { find /usr/local/lib -type f -print0 | xargs -0r strip --strip-all -p 2>/dev/null || true; }

# Entry
WORKDIR /root/
ENV fprocess="php index.php"
HEALTHCHECK --interval=1s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]