## Template: java11-vert-x

The Java11-Vert.x template uses gradle as a build system.

Gradle version: 4.8.1

### Structure

There are two projects which make up a single gradle build:

- function - (Library) your function code as a developer, you will only ever see this folder
- entrypoint - (App) Vert.x HTTP server

### FaaSFunction

The function is written in the `./src/main/java/com/openfaas/function/FaaSFunction.java` folder

The function can optionally implement the interface `Handler<RoutingContext>`. When this is true, the entrypoint will mount the function on any HTTP verb at the end of the router.

Helper handlers can be added to the router in the `setUp()` method. This is useful for example to enable the `BodyHandler` which will parse request body of requests.

#### Serve a "pure" static html web application

This template can serve static html assets (eg: single page application). This is an example of using the `setUp()` example.

You only need to add to the `environment` key, a `FRONTAPP` variable (with a value set to `true`)

```yaml
environment:
  FRONTAPP: true
```

Put your static assets in this directory: `/src/main/resources/webroot`

> If `FRONTAPP` is set to `false` (or does not exist), it's the `FaaSFunction` instance that serves the data.

#### Testing the function

Tests are supported with junit via files in `./src/test`

### External dependencies

External dependencies can be specified in ./build.gradle in the normal way using jcenter, a local JAR or some other remote repository.

### Deployment yaml file sample:

```yaml
provider:
  name: faas
  gateway: http://openfaas.test:8080
functions:
  hello-vert-x:
    lang: java11-vert-x
    environment:
      FRONTAPP: false
    handler: ./function
    image: registry.test:5000/hello-vert-x:latest
```

