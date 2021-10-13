# Lenra OpenFaaS Templates

This repository is based on the openfaas' templates [repository](https://github.com/openfaas/templates).

**Description**:  This project aims to provide some OpenFaaS templates to work with Lenra applications. An application can be launched using any of the following templates with the condition that the application has been developed using the same programming language as the template.

| Name   | Language | Version | Link                                                                                 |
| :----- | :------- | :------ | :----------------------------------------------------------------------------------- |
| node12 | NodeJS   | 12      | [NodeJS template](https://github.com/lenra-io/templates/tree/master/template/node12) |

## Dependencies

Docker is required to use the templates. Follow this [link](https://docs.docker.com/get-docker/) for official installation instructions. 

## Installation

You do not need to install anything if you want to use our pre-built docker images on our registry.

In case you still need to use your own images, you first need to clone the repository then follow the [Usage](#usage) part of this README.

    git clone git@github.com:lenra-io/templates.git

## Usage

### Docker Hub

Lenra provides each template on its Github registry so that you do not have to locally build the docker image of the template that you need. Below is an example using the node12 template and our Docker images.

```bash
# Go to your app folder and execute this command
# The host port can be changed to your convenience
docker run -it -p 4000:4000 -v $PWD:/home/app/application lenra/devtools-node12
```

### Build Local Images

In case you still need to build your local image, you should first build the template's image then build the root image. Here is an example with the node12 template: 

```bash
# Go to the node12 template folder
cd templates/template/node12
# Build the template's docker image
docker build -t lenra/template-node12 .
# Go back to the root of the repository
cd ../..
# Build the devtools' template docker image
docker build -t lenra/devtools-node12 -f Dockerfile.devtools --build-arg TEMPLATE_IMAGE=lenra/template-node12 .
```

Then you can run your app locally (your app must use node12) using the following commands:

```bash
# Go to your app folder and execute this command
# The host port can be changed to your convenience
docker run -it --rm -p 4000:4000 -v ${PWD}:/home/app/application lenra/devtools-node12 
```

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

TODO



## License
This project is licensed under the MIT License.
