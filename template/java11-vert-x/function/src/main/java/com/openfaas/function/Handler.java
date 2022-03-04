package com.openfaas.function;

import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;

public class Handler {

    public void handle(RoutingContext ctx) {
        ctx.json(
                new JsonObject()
                        .put("status", "ok"));
    }
}
