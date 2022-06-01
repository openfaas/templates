#!/bin/sh

echo "Installing PHP extensions"

# docker-php-ext-install is requiring the wrong dependency to install the zip extension, we can use Alpine's apk to circumvent the issue
apk add libzip-dev
docker-php-ext-install zip
