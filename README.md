# templates

Official OpenFaaS templates.

To find out more about the OpenFaaS templates see the [faas-cli](https://github.com/openfaas/faas-cli).

> Note: The templates are completely customizable - so if you want to alter them please do fork them and use `faas template pull` to make use of your updated versions.

The current version of OpenFaaS templates use the original `watchdog` which `forks` processes - a bit like CGI. The newer watchdog [of-watchdog](https://github.com/openfaas-incubator/of-watchdog) is more similar to fastCGI/HTTP and should be used for any benchmarking or performance testing along with one of the newer templates. Contact the project for more information.

### Templates Developers / Contributors

See [contributing guide](https://github.com/openfaas/templates/blob/master/CONTRIBUTING.md).

### License

This project is part of the OpenFaaS project licensed under the MIT License.

### Templates in store

| Name | Language | Version | Linux base | Watchdog | Link
|:-----|:---------|:--------|:-----------|:---------|:----
| csharp | C# | N/A | Alpine Linux 3.8 | Classic | [C# template](https://github.com/openfaas/templates/tree/master/template/csharp)
| dockerfile | Dockerfile | N/A | Alpine Linux 3.8 | Classic | [Dockerfile template](https://github.com/openfaas/templates/tree/master/template/dockerfile)
| go-armhf | Go | 1.10.4 | Alpine Linux 3.8 | Classic | [Go armhf template](https://github.com/openfaas/templates/tree/master/template/go-armhf)
| go | Go | 1.10.4 | Alpine Linux 3.8 | Classic | [Go template](https://github.com/openfaas/templates/tree/master/template/go)
|java8 | Java | 8 | OpenJDK Alpine Linux | Classic | [Java template](https://github.com/openfaas/templates/tree/master/template/java8)
| node-arm64 | NodeJS | 8.9.1 | N/A | Classic | [NodeJS arm64 template](https://github.com/openfaas/templates/tree/master/template/node-arm64)
| node-armhf | NodeJS | N/A | Alpine Linux 3.6 | Classic | [NodeJS armhf template](https://github.com/openfaas/templates/tree/master/template/node-armhf) 
| node | NodeJS | 8.9.1 | Alpine Linux | Classic | [NodeJS template](https://github.com/openfaas/templates/tree/master/template/node)
| php7 | PHP | 7.2 | Alpine Linux | Classic | [PHP 7 template](https://github.com/openfaas/templates/tree/master/template/php7)
| python-armhf | Python | 2.7 | Alpine Linux | Classic | [Python 2.7 armhf template](https://github.com/openfaas/templates/tree/master/template/python-armhf)
| python | Python | 2.7 | Alpine Linux | Classic | [Python 2.7 template](https://github.com/openfaas/templates/tree/master/template/python)
| python3-armhf | Python | 3.6 | Alpine Linux | Classic | [Python 3.6 armhf template](https://github.com/openfaas/templates/tree/master/template/python3-armhf)
| python3 | Python | 3 | Alpine Linux | Classic | [Python 3 template](https://github.com/openfaas/templates/tree/master/template/python3)
| ruby | Ruby | 2.5.1 | Alpine Linux 3.7 | Classic| [Ruby template](https://github.com/openfaas/templates/tree/master/template/ruby)

For more information on the templates check out the [docs](https://docs.openfaas.com/cli/templates/).
