# OpenFaaS Classic templates

[![Build Status](https://github.com/openfaas/templates/workflows/ci-only/badge.svg?branch=master)](https://github.com/openfaas/templates/actions)

To find out more about the OpenFaaS templates see the [faas-cli](https://github.com/openfaas/faas-cli).

> Note: The templates are completely customizable - so if you want to alter them please do fork them and use `faas template pull` to make use of your updated versions.

### Classic Templates

This repository contains the Classic OpenFaaS templates, but many more are available in the Template Store.

Most of the original Classic Templates are retained or backwards compatibility, but are deprecated.

See: `faas-cli template store list` to see which templates are both: recommended and official for the language you wish to use.

| Name | Language | Version | Linux base | Watchdog | Link
|:-----|:---------|:--------|:-----------|:---------|:----
| dockerfile | Dockerfile | N/A | Alpine Linux | classic | [Dockerfile template](https://github.com/openfaas/templates/tree/master/template/dockerfile)
| node18 | NodeJS | 18 | Alpine Linux | of-watchdog | [NodeJS template (deprecated)](https://github.com/openfaas/templates/tree/master/template/node18)
| node20 | NodeJS | 20 | Alpine Linux | of-watchdog | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node20)
| node22 | NodeJS | 22 | Alpine Linux | of-watchdog | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node22)
| java11-vert-x | Java and [Vert.x](https://vertx.io/) | 11 | Debian GNU/Linux | of-watchdog | [Java LTS template](https://github.com/openfaas/templates/tree/master/template/java11-vert-x)
| java11 | Java | 11 | Debian GNU/Linux | of-watchdog | [Deprecated Java template](https://github.com/openfaas/templates/tree/master/template/java11)
| java17 | Java | 11 | Debian GNU/Linux | of-watchdog | [Java LTS template](https://github.com/openfaas/templates/tree/master/template/java17)
| php7 | PHP | 7.4 | Alpine Linux | classic | [PHP 7 template](https://github.com/openfaas/templates/tree/master/template/php7)
| php8 | PHP | 8.2 | Alpine Linux | classic | [PHP 8 template](https://github.com/openfaas/templates/tree/master/template/php8)

For more information on the templates check out the [docs](https://docs.openfaas.com/cli/templates/).

### Moved Classic Templates

A number of long deprecated templates have been moved out of this repository into a new repository to indicate their End Of Life (EOL) status.

In most cases, alternatives have already been provided and are listed int the Function Store, or the [Languages section of the OpenFaaS documentation](https://docs.openfaas.com/languages/overview/).

Moved templates:

* go
* python3
* python3-debian
* python27
* ruby
* csharp
* bun-express
* node

### Classic vs of-watchdog templates

The current version of OpenFaaS templates use the original `watchdog` which `forks` processes - a bit like CGI. The newer watchdog [of-watchdog](https://github.com/openfaas/of-watchdog) is more similar to fastCGI/HTTP and should be used for any benchmarking or performance testing along with one of the newer templates. Contact the project for more information.

### Submit your own template to the template store

This repository is for templates supported by the project maintainers, if you'd like to submit your own template to the OpenFaaS Template Store then checkout the store:

* [OpenFaaS Store](https://github.com/openfaas/store/)

### Contribute to this repository

See [contributing guide](https://github.com/openfaas/templates/blob/master/CONTRIBUTING.md).

### License

This project is part of the OpenFaaS project licensed under the MIT License.
