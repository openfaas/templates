import org.junit.Test;
import static org.junit.Assert.*;

import com.openfaas.function.*;

public class HandlerTest {

    @Test public void handlerIsNotNull() {
        HandlerTest handler = new HandlerTest();
        assertTrue("Expected handler not to be null", handler != null);
    }

}
