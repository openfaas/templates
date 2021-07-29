import com.openfaas.function.FaaSFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FaaSFunctionTest {
  @Test public void handlerIsNotNull() {
    FaaSFunction function = new FaaSFunction();
    assertTrue("Expected function not to be null", function != null);
  }
}
