#!/bin/sh

echo "Skip installing PHP extensions for Core"

# mcrypt is deprecated - openssl is installed
#apk add --no-cache \
#  docker-php-ext-install -j$(getconf _NPROCESSORS_ONLN) mbstring