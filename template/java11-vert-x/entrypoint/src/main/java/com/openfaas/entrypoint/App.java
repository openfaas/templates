// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.
package com.openfaas.entrypoint;

import io.vertx.core.http.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.BodyHandler;

import com.openfaas.function.Handler;

import java.util.Optional;

public class App {
  public static void main(String[] args) throws Exception {
    Vertx vertx = Vertx.vertx();
    Integer httpPort = Integer.parseInt(Optional.ofNullable(System.getenv("PORT")).orElse("8082"));
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    if (Boolean.parseBoolean(Optional.ofNullable(System.getenv("FRONTAPP")).orElse("false"))) {
      // serve static assets, see /resources/webroot directory
      router.route().handler(StaticHandler.create());
    } else {
      // enable body parsing (i.e.: POST, multipart, etc...)
      router.route().handler(BodyHandler.create());
      // allow usage of any verb for the function
      router.route().handler(new Handler());
    }

    server.requestHandler(router).listen(httpPort, result -> {
      if(result.succeeded()) {
        System.out.println("Listening on port " + httpPort);
      } else {
        System.out.println("Unable to start server: " + result.cause().getMessage());
      }
    });
  }
}
