package com.openfaas.entrypoint;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.concurrent.TimeUnit;

public final class App {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                vertx.close().toCompletionStage().toCompletableFuture().get(10, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.err.println("Unable to close Vert.x: " + e.getMessage());
            }
        }));

        try {
            vertx.createHttpServer()
                .requestHandler(router(vertx, new com.openfaas.function.Handler(vertx)))
                .listen(8082, "127.0.0.1")
                .toCompletionStage().toCompletableFuture().join();
            System.out.println("Listening on 127.0.0.1:8082");
        } catch (Exception e) {
            System.err.println("Unable to start HTTP server: " + e.getMessage());
            System.exit(1);
        }
    }

    static Router router(Vertx vertx, io.vertx.core.Handler<RoutingContext> handler) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create()
            .setBodyLimit(1024 * 1024)
            .setHandleFileUploads(false));
        router.route().handler(handler);
        return router;
    }
}
