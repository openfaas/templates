## Template: java11-vert-x

The Java11-Vert.x template uses gradle as a build system.

Gradle version: 7.4

### Structure

There are two projects which make up a single gradle build:

- function - (Library) your function code as a developer, you will only ever see this folder
- entrypoint - (App) Vert.x HTTP server

### Function

The function is written in the `./src/main/java/com/openfaas/function/Handler.java` file

Tests are supported with junit via files in `./src/test`

### Body Parsing

HTTP Request body is disabled by default. This means that the environment variable `RAW_BODY` is set either missing or
set to `true`. When the variable is explicitly set to `false`, then vert.x internal BodyHandler is used.

#### Limiting Body Length

By default, vert.x allows any request body length. For security reasons or resource constraints, you might want to limit
this value. In order to do this set the `BODY_MAX_SIZE` environment variable to the allowed number of bytes, or `-1` for
unlimited.

### External dependencies

External dependencies can be specified in ./build.gradle in the normal way using mavenCentral, a local JAR or some other
remote repository.

### Serve a "pure" static html web application

This template allow you to serve static html assets (eg: single page application)

#### First, update your yaml deployment file:

You only need to add to the `environment` key, a `FRONTAPP` variable (with a value set to `true`)

```yaml
environment:
  FRONTAPP: true
```

#### Then add assets in the webroot directory

Put your static assets in this directory: `/src/main/resources/webroot`

> If `FRONTAPP` is set to `false` (or does not exist), it's the `Handler` instance that serves the data.


#### Deployment yaml file sample:

```yaml
provider:
  name: faas
  gateway: http://openfaas.test:8080
functions:
  hello-vert-x:
    lang: java11-vert-x
    environment:
      FRONTAPP: true
      RAW_BODY: false
      MAX_BODY_SIZE: 102400
    handler: ./function
    image: registry.test:5000/hello-vert-x:latest
```

