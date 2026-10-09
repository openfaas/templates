package com.openfaas.entrypoint;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.RoutingContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    private Vertx vertx;
    private HttpClient client;

    @BeforeEach
    void setUp() {
        vertx = Vertx.vertx();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() throws Exception {
        client.close();
        vertx.close().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
    }

    private URI start(io.vertx.core.Handler<RoutingContext> handler) throws Exception {
        HttpServer server = vertx.createHttpServer()
            .requestHandler(App.router(vertx, handler))
            .listen(0, "127.0.0.1")
            .toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
        return URI.create("http://127.0.0.1:" + server.actualPort());
    }

    private HttpResponse<String> send(HttpRequest.Builder request) throws Exception {
        return client.send(request.timeout(Duration.ofSeconds(10)).build(),
            HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void passesRequestAndResponseThrough() throws Exception {
        URI uri = start(context -> context.response().setStatusCode(201)
            .putHeader("X-Result", context.request().getHeader("X-Input"))
            .end(context.request().method() + " " + context.request().path() + " "
                + context.request().getParam("q") + " " + context.body().asString()));
        var response = send(HttpRequest.newBuilder(uri.resolve("/nested/path?q=value"))
            .header("X-Input", "passed")
            .PUT(HttpRequest.BodyPublishers.ofString("hello λ")));
        assertEquals(201, response.statusCode());
        assertEquals("passed", response.headers().firstValue("X-Result").orElseThrow());
        assertEquals("PUT /nested/path value hello λ", response.body());
    }

    @Test
    void rejectsOversizedBody() throws Exception {
        URI uri = start(context -> context.response().end("unexpected"));
        var response = send(HttpRequest.newBuilder(uri)
            .POST(HttpRequest.BodyPublishers.ofString("x".repeat(1024 * 1024 + 1))));
        assertEquals(413, response.statusCode());
    }

    @Test
    void handlerExceptionReturnsServerError() throws Exception {
        URI uri = start(context -> { throw new IllegalStateException("test failure"); });
        assertEquals(500, send(HttpRequest.newBuilder(uri)).statusCode());
    }
}
