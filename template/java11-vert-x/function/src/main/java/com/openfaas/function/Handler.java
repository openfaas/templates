package com.openfaas.function;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.core.json.JsonObject;

public class Handler implements BodyHandler {

  @Override
  public void handle(RoutingContext routingContext) {
    routingContext.response()
      .putHeader("content-type", "application/json;charset=UTF-8")
      .end(
        new JsonObject()
          .put("status", "ok")
          .encodePrettily()
      );
  }

  @Override
  public BodyHandler setBodyLimit(long bodyLimit) {
    return null;
  }

  @Override
  public BodyHandler setUploadsDirectory(String uploadsDirectory) {
    return null;
  }

  @Override
  public BodyHandler setMergeFormAttributes(boolean mergeFormAttributes) {
    return null;
  }

  @Override
  public BodyHandler setDeleteUploadedFilesOnEnd(boolean deleteUploadedFilesOnEnd) {
    return null;
  }
}
