import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import com.openfaas.function.OpenFaasFunction;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class OpenFaasFunctionTest {

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    @Test
    public void handlerIsNotNull() {
        OpenFaasFunction handler = new OpenFaasFunction();
        assertTrue("Expected handler not to be null", handler != null);
    }

    @Test
    public void resultIsJson(TestContext should) {
        Async test = should.async();

        OpenFaasFunction handler = new OpenFaasFunction();
        handler.apply(null)
                .onFailure(should::fail)
                .onSuccess(res -> {
                              should.assertTrue(res != null);
                              should.assertEquals("ok", res.getString("status"));
                              test.complete();
                });
    }
}
