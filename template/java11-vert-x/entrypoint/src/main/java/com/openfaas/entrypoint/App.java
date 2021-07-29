// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package com.openfaas.entrypoint;

import com.openfaas.function.FaaSFunction;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.Optional;

public class App {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Router router = Router.router(vertx);
    try {
      final FaaSFunction fn = new FaaSFunction();
      fn.setUp(router)
              .onFailure(App::fail)
              .onSuccess(ready -> {
                Object ref = fn;
                if (ref instanceof Handler) {
                  router.route()
                          .handler((Handler<RoutingContext>) ref);
                }
                listen(vertx, router);
              });
    } catch (RuntimeException e) {
      fail(e);
    }
  }

  private static void listen(Vertx vertx, Router router) {
    int httpPort = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8082"));
    HttpServer server = vertx.createHttpServer();

    server.requestHandler(router)
            .listen(httpPort)
            .onFailure(App::fail)
            .onSuccess(res -> System.out.println("Listening on port " + httpPort));
  }

  private static void fail(Throwable err) {
    err.printStackTrace();
    System.exit(1);
  }
}
