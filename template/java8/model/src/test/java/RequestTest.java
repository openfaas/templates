import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import com.openfaas.model.Request;
public class RequestTest {

	@Test
	public void testSingleRequestParameterGetSet()
	{
		Request request = new Request(null,null,"testParam=testParamValue");
		assertEquals("testParamValue", request.getQuery().get("testParam"));
	}
	
	@Test
	public void testMultipleRequestParametersGetSet()
	{
		Request request = new Request(null,null,"testParam1=testParamValue1&testParam2=testParamValue2");
		assertEquals("testParamValue1", request.getQuery().get("testParam1"));
		assertEquals("testParamValue2", request.getQuery().get("testParam2"));
	}
	
	@Test
	public void testNullRequestParameterGetSet()
	{
		Request request = new Request(null,null,null);
		assertEquals(null, request.getQuery().get("testParam"));
	}
	
	@Test
	public void testEmptyRequestParameterGetSet()
	{
		Request request = new Request(null,null,"");
		assertEquals(null, request.getQuery().get("testParam"));
	}
	
	@Test
	public void testRequestRawGetSet()
	{
		Request request = new Request(null,null,"testRaw=testRawValue");
		assertEquals("testRaw=testRawValue", request.getQueryRaw());
	}
}
