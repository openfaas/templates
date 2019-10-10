import org.junit.Test;
import static org.junit.Assert.*;

import com.github.kameshchauhan.openfaas.function.Handler;

public class HandlerTest {

    @Test public void handlerIsNotNull() {
        Handler handler = new Handler();
        assertTrue("Expected handler not to be null", handler != null);
    }

}
