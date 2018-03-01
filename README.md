# templates

Official OpenFaaS templates.

To find out more about the OpenFaaS templates see the [faas-cli](https://github.com/openfaas/faas-cli).

> Note: The templates are completely customizable - so if you want to alter them please do fork them and use `faas template pull` to make use of your updated versions.

The current version of OpenFaaS templates use the original `watchdog` which `forks` processes - a bit like CGI. The newer watchdog [of-watchdog](https://github.com/openfaas-incubator/of-watchdog) is more similar to fastCGI/HTTP and should be used for any benchmarking or performance testing along with one of the newer templates. Contact the project for more information.
