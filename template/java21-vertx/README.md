## Template: java21-vertx

The Java 21 template gives your function control over the HTTP request and response, including methods, paths, query parameters, headers, request bodies and status codes. Your function implements a Vert.x `Handler<RoutingContext>` to read the request and write the response.

The JVM stays running between invocations. OpenFaaS of-watchdog forwards HTTP requests to the Vert.x server, reusing the same application process and handler instance.

The template uses Vert.x 5.2.1 and Gradle 9.8.1, with Eclipse Temurin on Ubuntu Noble as the container base.

### Create a function

From your function project directory, pull the templates and create a function:

```bash
faas-cli template pull https://github.com/openfaas/templates
faas-cli new hello-java --lang java21-vertx --prefix ghcr.io/your-user
```

Replace `ghcr.io/your-user` with your container registry prefix. Docker and `faas-cli` are required; Java and Gradle are provided by the build container.

To build and try the function locally:

```bash
faas-cli local-run hello-java --build --port 8085
```

Invoke it from another terminal:

```bash
curl http://127.0.0.1:8085 --data-binary 'OpenFaaS'
# Hello, OpenFaaS!
```

### Structure

The Gradle build contains two projects:

- `function` — your handler, dependencies, resources and tests. Its contents are copied into `hello-java/` when you create a function.
- The root project — the HTTP server and application entrypoint, supplied by the template.

Paths below are relative to the generated function directory (`hello-java/`).

### Handler

Edit `src/main/java/com/openfaas/function/Handler.java`. Keep the `com.openfaas.function.Handler` class and its constructor accepting a `Vertx` instance. The handler implements `io.vertx.core.Handler<RoutingContext>`; see the [default handler](function/src/main/java/com/openfaas/function/Handler.java).

Use the routing context to access the request and write the response:

- `context.request()` provides the HTTP method, path, query parameters and headers.
- `context.body().asString()` reads the buffered request body; use `context.body().buffer()` for binary input.
- `context.response()` sets the status, headers and response body. Call `end()` to complete the response, either directly or in an asynchronous callback.

Request bodies are limited to 1 MiB; larger bodies receive HTTP 413. Automatic file uploads are disabled.

The handler runs on the Vert.x event loop. Use asynchronous clients for network and database calls, and avoid blocking the event loop. Clients and connection pools can be kept in handler fields for reuse; keep request-specific data in local variables or the routing context.

### Example usage

For the first three examples, replace only the generated handler's `handle` method, keeping its class and constructor. Rebuild with `faas-cli local-run hello-java --build --port 8085` after each change.

#### Read the body and return text

```java
@Override
public void handle(RoutingContext context) {
    String body = context.body().asString();
    String name = body == null || body.isBlank() ? "World" : body;
    context.response()
        .putHeader("Content-Type", "text/plain; charset=utf-8")
        .end("Hello, " + name + "!");
}
```

```bash
curl http://127.0.0.1:8085 --data-binary 'OpenFaaS'
# Hello, OpenFaaS!
```

#### Read request details and return JSON

Add `import io.vertx.core.json.JsonObject;` alongside the existing imports. This example reads the method, path, query parameter and request header, and sets a custom response header:

```java
@Override
public void handle(RoutingContext context) {
    String name = context.queryParam("name").stream().findFirst().orElse("World");
    JsonObject result = new JsonObject()
        .put("message", "Hello, " + name + "!")
        .put("method", context.request().method().name())
        .put("path", context.request().path())
        .put("requestId", context.request().getHeader("X-Request-Id"));

    context.response()
        .putHeader("Content-Type", "application/json")
        .putHeader("X-Served-By", "java21-vertx")
        .end(result.encode());
}
```

```bash
curl -i 'http://127.0.0.1:8085/greet?name=OpenFaaS' -H 'X-Request-Id: example-123'
# HTTP 200, Content-Type: application/json, X-Served-By: java21-vertx
# {"message":"Hello, OpenFaaS!","method":"GET","path":"/greet","requestId":"example-123"}
```

#### Set a status code or return an error

Return HTTP 400 for an empty body and HTTP 201 for a valid request. End the response and return from the method on the error path:

```java
@Override
public void handle(RoutingContext context) {
    String body = context.body().asString();
    context.response().putHeader("Content-Type", "text/plain; charset=utf-8");
    if (body == null || body.isBlank()) {
        context.response().setStatusCode(400).end("A request body is required");
        return;
    }

    context.response().setStatusCode(201).end("Created: " + body);
}
```

```bash
curl -i http://127.0.0.1:8085 --data-binary ''
# HTTP 400: A request body is required
curl -i http://127.0.0.1:8085 --data-binary 'example'
# HTTP 201: Created: example
```

#### Reuse an asynchronous HTTP client

Add `implementation 'io.vertx:vertx-web-client'` to the `dependencies` block in the function's `build.gradle`, then replace the entire `Handler.java` with the following. The client is created once per handler instance and reused between invocations. Requests complete asynchronously without blocking the event loop:

```java
package com.openfaas.function;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

public class Handler implements io.vertx.core.Handler<RoutingContext> {
    private final WebClient client;
    private final String upstreamUrl;

    public Handler(Vertx vertx) {
        client = WebClient.create(vertx);

        upstreamUrl = System.getenv().getOrDefault(
            "UPSTREAM_URL",
            "http://gateway.openfaas:8080/function/nodeinfo"
        );
    }

    @Override
    public void handle(RoutingContext context) {
        if (!context.request().method().name().equals("GET")) {
            context.response().putHeader("Allow", "GET");
            respond(context, 405, new JsonObject().put("error", "Use GET"));
            return;
        }

        client.getAbs(upstreamUrl)
            .followRedirects(false)
            .timeout(3000)
            .send()
            .onSuccess(response -> {
                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    JsonObject body = new JsonObject()
                        .put("error", "Upstream rejected the request")
                        .put("upstreamStatus", response.statusCode());
                    respond(context, 502, body);
                    return;
                }

                JsonObject body = new JsonObject()
                    .put("upstreamStatus", response.statusCode())
                    .put("body", response.bodyAsString());
                respond(context, 200, body);
            })
            .onFailure(error -> {
                JsonObject record = new JsonObject()
                    .put("event", "upstream_request_failed")
                    .put("errorType", error.getClass().getSimpleName());
                System.err.println(record.encode());
                respond(context, 502, new JsonObject().put("error", "Upstream unavailable or timed out"));
            });
    }

    private static void respond(RoutingContext context, int status, JsonObject body) {
        context.response()
            .setStatusCode(status)
            .putHeader("Content-Type", "application/json; charset=utf-8")
            .end(body.encode());
    }
}
```

This example calls the `nodeinfo` store function through the OpenFaaS gateway. Deploy it with `faas-cli store deploy nodeinfo`, or set `UPSTREAM_URL` in the Java function's environment to an endpoint reachable from its container. For local testing, pass `-e UPSTREAM_URL=http://your-upstream:8080/` to `faas-cli local-run`.

Only GET requests are accepted. Successful upstream responses are wrapped in JSON; other upstream statuses, connection failures and the three-second timeout return HTTP 502. Redirects are disabled so requests stay at the configured destination.

#### Serve static files

Place your files under `src/main/resources/webroot/`, for example `index.html` and `style.css`. Replace the entire `Handler.java` with the following; no additional dependencies are needed:

```java
package com.openfaas.function;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class Handler implements io.vertx.core.Handler<RoutingContext> {
    private final Router router;

    public Handler(Vertx vertx) {
        router = Router.router(vertx);

        StaticHandler files = StaticHandler.create("webroot")
            .setIncludeHidden(false)
            .setDirectoryListing(false)
            .setAlwaysAsyncFS(true)
            .setMaxAgeSeconds(60);

        router.route().handler(files);

        router.route().handler(context -> context.response()
            .setStatusCode(404)
            .putHeader("Content-Type", "text/plain; charset=utf-8")
            .end("Not found\n"));
    }

    @Override
    public void handle(RoutingContext context) {
        router.handleContext(context);
    }
}
```

Gradle packages these resources in the function JAR. `StaticHandler` serves `/` as `index.html` and handles content types, HEAD requests and caching. Missing files return HTTP 404. The router uses the existing request context passed in by the template.

After rebuilding, open `http://127.0.0.1:8085/`. When accessing the deployed function in a browser, use a trailing slash on its URL so relative asset links resolve beneath the function path.

### External dependencies and resources

Add dependencies to `build.gradle`. Maven Central is configured, and the Vert.x platform supplies versions for Vert.x modules. For example, add this to the existing `dependencies` block:

```groovy
implementation 'io.vertx:vertx-web-client'
```

Specify a version for libraries outside the Vert.x platform. Files under `src/main/resources/` are packaged in the function JAR and available on the classpath.

### Tests

JUnit Jupiter is configured in the function's `build.gradle`. Add tests under `src/test/java/`. Tests run during container builds, and a failing test fails the build:

```bash
# Run from the directory containing stack.yaml.
faas-cli build -f stack.yaml
```

With a local JDK 21, run the template's build and HTTP tests from this repository's root:

```bash
cd template/java21-vertx
./gradlew build
```

### Runtime configuration

The runtime image contains a Java 21 JRE and runs as UID/GID `10001:10001`. Of-watchdog listens on port `8080` and forwards requests to Vert.x at `127.0.0.1:8082`.

A read-only root filesystem is supported when `/tmp` is writable. The JVM heap defaults to 75% of available memory; allow additional memory for thread stacks, direct buffers and other native allocations.

To override JVM options, set `JAVA_TOOL_OPTIONS` in the function's `environment` section in `stack.yaml`. This replaces all default options. For example, to reduce the maximum heap to 60% while retaining out-of-memory handling and the writable cache location:

```yaml
environment:
  JAVA_TOOL_OPTIONS: "-XX:MaxRAMPercentage=60.0 -XX:+ExitOnOutOfMemoryError -Dvertx.cacheDirBase=/tmp/.vertx"
```
