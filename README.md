# OpenFaaS Classic templates

[![Build Status](https://github.com/openfaas/templates/workflows/build/badge.svg?branch=master)](https://github.com/openfaas/templates/actions)

To find out more about the OpenFaaS templates see the [faas-cli](https://github.com/openfaas/faas-cli).

> Note: The templates are completely customizable - so if you want to alter them please do fork them and use `faas template pull` to make use of your updated versions.

### Classic Templates

This repository contains the Classic OpenFaaS templates, but many more are available in the Template Store. Read above for more information.

| Name | Language | Version | Linux base | Watchdog | Link
|:-----|:---------|:--------|:-----------|:---------|:----
| dockerfile | Dockerfile | N/A | Alpine Linux | classic | [Dockerfile template](https://github.com/openfaas/templates/tree/master/template/dockerfile)
| go | Go | 1.13 | Alpine Linux | classic | [Go template](https://github.com/openfaas/templates/tree/master/template/go)
| node12 | NodeJS | 12.13.0 | Alpine Linux | of-watchdog | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node12)
| node | NodeJS | 12.13.0 | Alpine Linux | classic | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node)
| python3 | Python | 3 | Alpine Linux | classic | [Python 3 template](https://github.com/openfaas/templates/tree/master/template/python3)
| python3-debian | Python | 3 | Debian Linux | classic | [Python 3 Debian template](https://github.com/openfaas/templates/tree/master/template/python3-debian)
| python | Python | 2.7 | Alpine Linux | classic | [Python 2.7 template](https://github.com/openfaas/templates/tree/master/template/python)
| java11-vert-x | Java and [Vert.x](https://vertx.io/) | 11 | Debian GNU/Linux | of-watchdog | [Java LTS template](https://github.com/openfaas/templates/tree/master/template/java11)
| java11 | Java | 11 | Debian GNU/Linux | of-watchdog | [Java LTS template](https://github.com/openfaas/templates/tree/master/template/java11)
| ruby | Ruby | 2.7 | Alpine Linux 3.11 | classic| [Ruby template](https://github.com/openfaas/templates/tree/master/template/ruby)
| php7 | PHP | 7.2 | Alpine Linux | classic | [PHP 7 template](https://github.com/openfaas/templates/tree/master/template/php7)
| php7.4 | PHP | 7.4 | Alpine Linux | classic | [PHP 7.4 template](https://github.com/openfaas/templates/tree/master/template/php7.4)
| php8.0 | PHP | 8.0 | Alpine Linux | classic | [PHP 8.0 template](https://github.com/openfaas/templates/tree/master/template/php8.0)
| csharp | C# | N/A | Debian GNU/Linux 9 | classic | [C# template](https://github.com/openfaas/templates/tree/master/template/csharp)

For more information on the templates check out the [docs](https://docs.openfaas.com/cli/templates/).

### Classic vs of-watchdog templates

The current version of OpenFaaS templates use the original `watchdog` which `forks` processes - a bit like CGI. The newer watchdog [of-watchdog](https://github.com/openfaas-incubator/of-watchdog) is more similar to fastCGI/HTTP and should be used for any benchmarking or performance testing along with one of the newer templates. Contact the project for more information.

### Submit your own template to the template store

This repository is for templates supported by the project maintainers, if you'd like to submit your own template to the OpenFaaS Template Store then checkout the store:

* [OpenFaaS Store](https://github.com/openfaas/store/)

### Contribute to this repository

See [contributing guide](https://github.com/openfaas/templates/blob/master/CONTRIBUTING.md).

### License

This project is part of the OpenFaaS project licensed under the MIT License.
