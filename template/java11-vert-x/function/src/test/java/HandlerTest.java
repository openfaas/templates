import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import com.openfaas.function.Handler;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class HandlerTest {

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    @Test
    public void handlerIsNotNull(TestContext should) {
        Handler handler = new Handler();
        assertTrue("Expected handler not to be null", handler != null);
    }
}
