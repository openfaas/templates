package com.openfaas.function;

import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * The OpenFaas function. The function must implement the interface {@code Function<RoutingContext, Future<?>>}.
 *
 * Note that there are 3 optional return types:
 *
 * <ul>
 *     <li>{@code Future<JsonObject>}</li>
 *     <li>{@code Future<Buffer>}</li>
 *     <li>{@code Future<String>}</li>
 * </ul>
 *
 * When any of these types is returned, the corresponding mime type header will be set:
 *
 * <ul>
 *     <li>{@code application/json}</li>
 *     <li>{@code application/octet-stream}</li>
 *     <li>{@code text/plain}</li>
 * </ul>
 *
 * To customize the mime type, call the setter on {@link RoutingContext} and your choice is respected.
 */
public class OpenFaasFunction implements Function<RoutingContext, Future<JsonObject>> {

    public Future<JsonObject> apply(RoutingContext routingContext) {
        return Future.succeededFuture(
                new JsonObject()
                        .put("status", "ok")
        );
    }
}
