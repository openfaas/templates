# OpenFaaS Classic templates

[![Build Status](https://travis-ci.org/openfaas/templates.svg?branch=master)](https://travis-ci.org/openfaas/templates)

To find out more about the OpenFaaS templates see the [faas-cli](https://github.com/openfaas/faas-cli).

> Note: The templates are completely customizable - so if you want to alter them please do fork them and use `faas template pull` to make use of your updated versions.

### Classic Templates

This repository contains the Classic OpenFaaS templates, but many more are available in the Template Store. Read above for more information.

| Name | Language | Version | Linux base | Watchdog | Link
|:-----|:---------|:--------|:-----------|:---------|:----
| node12 | NodeJS | 12.13.0 | Alpine Linux | of-watchdog | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node12)
| node | NodeJS | 12.13.0 | Alpine Linux | classic | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node)
| python3 | Python | 3 | Alpine Linux | classic | [Python 3 template](https://github.com/openfaas/templates/tree/master/template/python3)
| python | Python | 2.7 | Alpine Linux | classic | [Python 2.7 template](https://github.com/openfaas/templates/tree/master/template/python)
| go | Go | 1.11 | Alpine Linux | classic | [Go template](https://github.com/openfaas/templates/tree/master/template/go)
| csharp | C# | N/A | Debian GNU/Linux 9 | classic | [C# template](https://github.com/openfaas/templates/tree/master/template/csharp)
| java12 | Java | 12 | Oracle Linux | of-watchdog | [Java template](https://github.com/openfaas/templates/tree/master/template/java12)
| java8 | Java | 8 | OpenJDK Alpine Linux | of-watchdog | [Java template](https://github.com/openfaas/templates/tree/master/template/java8)
| ruby | Ruby | 2.5.1 | Alpine Linux 3.7 | classic| [Ruby template](https://github.com/openfaas/templates/tree/master/template/ruby)
| php7 | PHP | 7.2 | Alpine Linux | classic | [PHP 7 template](https://github.com/openfaas/templates/tree/master/template/php7)
| dockerfile | Dockerfile | N/A | Alpine Linux | classic | [Dockerfile template](https://github.com/openfaas/templates/tree/master/template/dockerfile)

For more information on the templates check out the [docs](https://docs.openfaas.com/cli/templates/).

### Classic vs of-watchdog templates

The current version of OpenFaaS templates use the original `watchdog` which `forks` processes - a bit like CGI. The newer watchdog [of-watchdog](https://github.com/openfaas-incubator/of-watchdog) is more similar to fastCGI/HTTP and should be used for any benchmarking or performance testing along with one of the newer templates. Contact the project for more information.

### Submit your own template to the template store

This repository is for templates supported by the project maintainers, if you'd like to submit your own template to the OpenFaaS Template Store then checkout the store:

* [OpenFaaS Store](https://github.com/openfaas/store/)

### Contribute to this repository

See [contributing guide](https://github.com/openfaas/templates/blob/master/CONTRIBUTING.md).

### Templates for ARM and alternative architectures

| Name | Language | Version | Linux base | Watchdog | Link
|:-----|:---------|:--------|:-----------|:---------|:----
| node-armhf | NodeJS | N/A | Alpine Linux | classic | [NodeJS armhf template](https://github.com/openfaas/templates/tree/master/template/node-armhf)
| go-armhf | Go | 1.11 | Alpine Linux | classic | [Go armhf template](https://github.com/openfaas/templates/tree/master/template/go-armhf)
| python-armhf | Python | 2.7 | Alpine Linux | classic | [Python 2.7 armhf template](https://github.com/openfaas/templates/tree/master/template/python-armhf)
| python3-armhf | Python | 3.6 | Alpine Linux | classic | [Python 3.6 armhf template](https://github.com/openfaas/templates/tree/master/template/python3-armhf)
| node-arm64 | NodeJS | 12.13.0 | N/A | classic | [NodeJS arm64 template](https://github.com/openfaas/templates/tree/master/template/node-arm64)

Additional templates for ARMHF/ARM64 may be available within the store. Some regular templates are also usable on ARM devices.

### License

This project is part of the OpenFaaS project licensed under the MIT License.
