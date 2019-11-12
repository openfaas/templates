# templates

Official OpenFaaS templates.

[![Build Status](https://travis-ci.org/openfaas/templates.svg?branch=master)](https://travis-ci.org/openfaas/templates)

To find out more about the OpenFaaS templates see the [faas-cli](https://github.com/openfaas/faas-cli).

> Note: The templates are completely customizable - so if you want to alter them please do fork them and use `faas template pull` to make use of your updated versions.

The current version of OpenFaaS templates use the original `watchdog` which `forks` processes - a bit like CGI. The newer watchdog [of-watchdog](https://github.com/openfaas-incubator/of-watchdog) is more similar to fastCGI/HTTP and should be used for any benchmarking or performance testing along with one of the newer templates. Contact the project for more information.

### Submit your own template to the template store

This repository is for templates supported by the project maintainers, if you'd like to submit your own template to the OpenFaaS Template Store then checkout the store:

* [OpenFaaS Store](https://github.com/openfaas/store/)

### Contribute to this repository

See [contributing guide](https://github.com/openfaas/templates/blob/master/CONTRIBUTING.md).

### License

This project is part of the OpenFaaS project licensed under the MIT License.

### Templates in store

| Name | Language | Version | Linux base | Watchdog | Link
|:-----|:---------|:--------|:-----------|:---------|:----
| node | NodeJS | 12.13.0 | Alpine Linux | of-watchdog | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node12)
| csharp | C# | N/A | Debian GNU/Linux 9 | classic | [C# template](https://github.com/openfaas/templates/tree/master/template/csharp)
| dockerfile | Dockerfile | N/A | Alpine Linux | classic | [Dockerfile template](https://github.com/openfaas/templates/tree/master/template/dockerfile)
| dockerfile-armhf | Dockerfile for ARMHF | N/A | Alpine Linux 3.10 | classic | [Dockerfile template](https://github.com/openfaas/templates/tree/master/template/dockerfile)
| go-armhf | Go | 1.11 | Alpine Linux | classic | [Go armhf template](https://github.com/openfaas/templates/tree/master/template/go-armhf)
| go | Go | 1.11 | Alpine Linux | classic | [Go template](https://github.com/openfaas/templates/tree/master/template/go)
|java8 | Java | 8 | OpenJDK Alpine Linux | of-watchdog | [Java template](https://github.com/openfaas/templates/tree/master/template/java8)
| node-arm64 | NodeJS | 12.13.0 | N/A | classic | [NodeJS arm64 template](https://github.com/openfaas/templates/tree/master/template/node-arm64)
| node-armhf | NodeJS | N/A | Alpine Linux | classic | [NodeJS armhf template](https://github.com/openfaas/templates/tree/master/template/node-armhf)
| node | NodeJS | 12.13.0 | Alpine Linux | classic | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node)
| php7 | PHP | 7.2 | Alpine Linux | classic | [PHP 7 template](https://github.com/openfaas/templates/tree/master/template/php7)
| python-armhf | Python | 2.7 | Alpine Linux | classic | [Python 2.7 armhf template](https://github.com/openfaas/templates/tree/master/template/python-armhf)
| python | Python | 2.7 | Alpine Linux | classic | [Python 2.7 template](https://github.com/openfaas/templates/tree/master/template/python)
| python3-armhf | Python | 3.6 | Alpine Linux | classic | [Python 3.6 armhf template](https://github.com/openfaas/templates/tree/master/template/python3-armhf)
| python3 | Python | 3 | Alpine Linux | classic | [Python 3 template](https://github.com/openfaas/templates/tree/master/template/python3)
| ruby | Ruby | 2.5.1 | Alpine Linux 3.7 | classic| [Ruby template](https://github.com/openfaas/templates/tree/master/template/ruby)

For more information on the templates check out the [docs](https://docs.openfaas.com/cli/templates/).

See also: [of-watchdog](https://github.com/openfaas-incubator/of-watchdog).
