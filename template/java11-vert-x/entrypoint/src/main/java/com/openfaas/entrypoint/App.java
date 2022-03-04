// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package com.openfaas.entrypoint;

import com.openfaas.function.Handler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Map;
import java.util.Optional;

public class App extends AbstractVerticle {

    private void fatal(Throwable err) {
        err.printStackTrace();
        vertx.close()
                .onComplete(ar -> System.exit(-1));
    }

    @Override
    public void start(Promise<Void> start) {
        final Router router = Router.router(vertx);
        final Route route = router.route();

        final Map<String, String> env = System.getenv();
        // FRONTAPP
        if (Boolean.parseBoolean(env.getOrDefault("FRONTAPP", "false"))) {
            // serve static assets, see /resources/webroot directory
            route.handler(StaticHandler.create());
        } else {
            // RAW_BODY
            final boolean rawBody = Boolean.parseBoolean(env.getOrDefault("RAW_BODY", "true"));
            if (!rawBody) {
                final int maxBodySize = Integer.parseInt(env.getOrDefault("RAW_BODY_SIZE", "-1"));
                route.handler(BodyHandler.create().setBodyLimit(maxBodySize));
            }
            final Handler fn = new Handler();
            route
                    .handler(fn::handle);
        }

        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8082")))
                .onFailure(this::fatal)
                .onSuccess(server -> System.out.println("Listening on port " + server.actualPort()));
    }
}
