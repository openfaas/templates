// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package com.openfaas.entrypoint;

import com.openfaas.function.OpenFaasFunction;
import com.openfaas.function.OpenFaasVertxConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.util.Optional;

public class App extends AbstractVerticle {

    private static void fatal(Throwable err) {
        err.printStackTrace();
        System.exit(-1);
    }

    public static void main(String[] args) {
        OpenFaasVertxConfig.newVertx()
                .onFailure(App::fatal)
                .onSuccess(vertx ->
                        vertx
                                .deployVerticle(new App())
                                .onFailure(App::fatal));
    }

    @Override
    public void start(Promise<Void> start) {
        final Router router = Router.router(vertx);
        final Route route = router.route();

        OpenFaasVertxConfig.configureRoute(route)
                .onFailure(App::fatal)
                .onSuccess(v -> {
                    route.respond(new OpenFaasFunction());
                    vertx
                            .createHttpServer()
                            .requestHandler(router)
                            .listen(Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8082")))
                            .onFailure(App::fatal)
                            .onSuccess(server -> {
                                System.out.println("Listening on port " + server.actualPort());
                            });
                    });
    }

    @Override
    public void stop(Promise<Void> stop) {
        OpenFaasVertxConfig
                .shutdown()
                .onComplete(stop);
    }
}
