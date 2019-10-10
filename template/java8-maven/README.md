## Template: java8-maven

The Java8 template uses Maven as a build system.

Maven version: 3.6.0

### Structure

There are three components to make up a single OpenFaaS function using Maven:

- model - (Library) classes for parsing request/response - This is available as dependency from Maven Central.
- entrypoint - (Library) HTTP server for re-using the JVM between requests - This is available as dependency from Maven Central.
- function - (App) your function code as a developer, you will only ever see this folder

### Handler

The handler is written in the `./src/main/com/openfaas/function/Handler.java` folder

Tests are supported with junit via files in `./src/test`

### External dependencies

External dependencies can be specified in ./pom.xml in the normal way, a local JAR or some other remote repository.

