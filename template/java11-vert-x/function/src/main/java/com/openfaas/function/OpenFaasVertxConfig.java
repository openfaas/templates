package com.openfaas.function;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.util.Optional;

/**
 * Use this class to configure your vert.x function server instance.
 */
public final class OpenFaasVertxConfig {

    /**
     * Factory to create a vert.x instance, by default users should not need to change the default implementation of
     * this method, however, when certain configuration is needed, for example, clustering support, custom event loop
     * sizes, DNS resolvers, etc... This method can be used to return the desired Vertx instance.
     */
    public static Future<Vertx> newVertx() {
        return Future.succeededFuture(Vertx.vertx());
    }

    /**
     * Allow configuration of the route used to handle the function requests.
     */
    public static Future<Void> configureRoute(Route route) {
        // allow this function to serve static files can be achieved with:
        if (Boolean.parseBoolean(Optional.ofNullable(System.getenv("FRONTAPP")).orElse("false"))) {
            // serve static assets, see /resources/webroot directory
            route.handler(StaticHandler.create());
        }
        // allow this function to safely process the request body can be achieved with:
        route.handler(BodyHandler.create());

        return Future.succeededFuture();
    }

    /**
     * When the function is to be shutdown by the watchdog, if there are any clean up actions to be performed, they can
     * be invoked from this function.
     */
    public static Future<Void> shutdown() {
        return Future.succeededFuture();
    }
}
