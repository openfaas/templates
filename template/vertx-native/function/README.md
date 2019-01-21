## Vert.x SVM Function

Implement your functions [MyFunction](src/main/java/MyFunction.java):

```java
public class MyFunction implements Handler<RoutingContext> {
  @Override
  public void handle(io.vertx.ext.web.RoutingContext ctx) {
		ctx.response().end("Hello OpenFaaS!");
	}
}
```

If you rename the function file you will need to update the `META-INF/services`.

### Advanced setup

If your function requires custom GraalVM configuration, update the [native-image.properties](./src/main/resouces/META-INF/native-image/openfaas/vertx-native-fn/native-image.properties).

To whitelist any missing classes that are being excluded by SVM, list them on the `reflection.json` file.

For more information please consult the GraalVM [native image](http://www.graalvm.org/docs/reference-manual/aot-compilation/) documentation.