# Lenra OpenFaaS Templates

This repository is based on the openfaas' templates [repository](https://github.com/openfaas/templates).

**Description**:  This project aims to provide some OpenFaaS templates to work with Lenra applications. An application can be launched using any of the following templates with the condition that the application should have been developed using the same programming language as the template.

| Name | Language | Version | Link
|:-----|:---------|:--------|:----
| node | NodeJS | 12 | [NodeJS template](https://github.com/lenra-io/templates/tree/master/template/node12)

## Dependencies

Docker is required to use the templates. Follow this [link](https://docs.docker.com/get-docker/) for official installation instructions. 

## Installation

You do not need to install anything if you want to use our pre-built docker images on our registry.

In case you still need to use your own images, you first need to clone the repository then follow the [Usage](#usage) part of this README.

    git clone git@github.com:lenra-io/templates.git

## Usage

Lenra provides each template on its Github registry so that you do not have to locally build the docker image of the template that you need.

In case you still need to build your local image, you should first build the template's image then build the root image. Here is an example with the node template: 

    templates/template/node: docker build -t node-template .
    templates/: docker build -t devtools-node -f Dockerfile.devtool --build-arg TEMPLATE_IMAGE=node-template .

Then you can run the devtools-node image by going to your Lenra application folder and using this command:

    app/: docker run -v $PWD:/home/app/application devtools-node

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

TODO



## License
This project is licensed under the MIT License.
