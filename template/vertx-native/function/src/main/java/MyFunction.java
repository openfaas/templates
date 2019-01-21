/*
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
 import io.vertx.core.Handler;
 import io.vertx.ext.web.RoutingContext;

/**
 * This is your main function, implement the handle method with your function.
 *
 * If function composition is required, add more functions and register them
 * in the META-INF/services/io.vertx.core.Handler
 */
public class MyFunction implements Handler<RoutingContext> {

  @Override
  public void handle(RoutingContext ctx) {
		ctx.response().end("Hello OpenFaaS!");
	}

}
