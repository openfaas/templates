package com.openfaas.function;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Optional;

public class FaaSFunction implements Handler<RoutingContext> {

  public Future<Void> setUp(Vertx vertx, Router router) {
    // allow customization of the router
    // for example, mount extra handlers like BodyHandler, CORS, etc...

    if (Boolean.parseBoolean(Optional.ofNullable(System.getenv("FRONTAPP")).orElse("false"))) {
      // serve static assets, see /resources/webroot directory
      router.route()
              .handler(StaticHandler.create());
    }

    return Future.succeededFuture();
  }

  public void handle(RoutingContext ctx) {
    ctx.json(
        new JsonObject()
          .put("status", "ok"));
  }
}
