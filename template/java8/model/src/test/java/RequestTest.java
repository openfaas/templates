import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.openfaas.model.Request;
public class RequestTest {

	@Test
	public void testSingleRequestParameterGetSet()
	{
		Request request = new Request((String)null, null, "testParam=testParamValue", null);
		assertEquals("testParamValue", request.getQuery().get("testParam"));
	}

	@Test
	public void testMultipleRequestParametersGetSet()
	{
		Request request = new Request("", null,"testParam1=testParamValue1&testParam2=testParamValue2", null);
		assertEquals("testParamValue1", request.getQuery().get("testParam1"));
		assertEquals("testParamValue2", request.getQuery().get("testParam2"));
	}

	@Test
	public void testNullRequestParameterGetSet()
	{
		Request request = new Request("",null,null, null);
		assertEquals(null, request.getQuery().get("testParam"));
	}

	@Test
	public void testEmptyRequestParameterGetSet()
	{
		Request request = new Request("", null, "", null);
		assertEquals(null, request.getQuery().get("testParam"));
	}

	@Test
	public void testRequestRawGetSet()
	{
		Request request = new Request("", null,"testRaw=testRawValue", null);
		assertEquals("testRaw=testRawValue", request.getQueryRaw());
	}

	@Test
	public void testGetPath()
	{
		Request request = new Request("", null, null, "/test/path");
		try {
			assertEquals("/test/path", request.getPathRaw());
		} catch (AssertionError e) {
			System.out.format("Expected: /test/path Got: %s", request.getPathRaw());
			throw e;
		}
	}

	@Test
	public void testGetPathWithNullPath()
	{
		Request request = new Request("", null,null, null);
		try {
			assertNull(request.getPathRaw());
		} catch (AssertionError e) {
			System.out.format("Expected: null Got: %s", request.getPathRaw());
			throw e;
		}
	}

	@Test
	public void testParseParametersWithoutAnyParameters()
	{
		Request request = new Request("", null, null, "/");
		try {
			assertEquals(0, request.getPath().size());
		} catch (AssertionError e) {
			System.out.format("Expected: 0 Got: %s", request.getPath().size());
			throw e;
		}

		Request emptyRequest = new Request(null, null, null, "");
		try {
			assertEquals(0, emptyRequest.getPath().size());
		} catch (AssertionError e) {
			System.out.format("Expected: 0 Got: %s", emptyRequest.getPath().size());
			throw e;
		}
	}

	@Test
	public void testParseParametersWithEvenParameters()
	{
		Request request = new Request("", null, null, "/param1/value1/param2/value2");
		Map<String, String> params = request.getPath();

		try {
			assertEquals("value1", params.get("param1"));
		} catch (AssertionError e) {
			System.out.format("Expected: value1 Got: %s", params.get("param1"));
			throw e;
		}
		try {
			assertEquals("value2", params.get("param2"));
		} catch (AssertionError e) {
			System.out.format("Expected: value2 Got: %s", params.get("param2"));
			throw e;
		}
	}

	@Test
	public void testParseParametersWithOddParameters()
	{
		Request request = new Request("", null, null, "/param1/value1/param2");
		Map<String, String> params = request.getPath();

		try {
			assertEquals("value1", params.get("param1"));
		} catch (AssertionError e) {
			System.out.format("Expected: value1 Got: %s", params.get("param1"));
			throw e;
		}
		try {
			assertEquals("", params.get("param2"));
		} catch (AssertionError e) {
			System.out.format("Expected: value2 Got: %s", params.get("param2"));
			throw e;
		}
	}

	@Test
	public void testRequestGetBodyWithString() {
		Request request = new Request(null, null, null, new ByteArrayInputStream("Øl får meg glad".getBytes()));
		assertEquals("Øl får meg glad", request.getBody());
	}

	@Test
	public void testRequestInputStream() {
		try {
			String requestBody = "Øl får meg glad";
			byte[] originalData = requestBody.getBytes();
			Request request = new Request(null, null, null, new ByteArrayInputStream(requestBody.getBytes()));
			byte[] buffer = new byte[64];
			int read = request.getInputStream().read(buffer);
			byte[] requestBodyData = new byte[read];
			System.arraycopy(buffer, 0, requestBodyData, 0, read);
			assertTrue(Arrays.equals(originalData, requestBodyData));
		} catch (IOException e) {
			throw new AssertionError(e);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	@Test
	public void testAlternateSetBodyGetBodyData() {
		try {
			String requestBody = "Øl får meg glad";
			byte[] originalData = requestBody.getBytes();
			Request request = new Request(requestBody, null, null, null);
			byte[] buffer = new byte[64];
			int read = request.getInputStream().read(buffer);
			byte[] requestBodyData = new byte[read];
			System.arraycopy(buffer, 0, requestBodyData, 0, read);
			assertTrue(Arrays.equals(originalData, requestBodyData));
		} catch (IOException e) {
			throw new AssertionError(e);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}

	@Test
	public void testAlternateSetBodyDataGetBody() {
		try {
			String originalRequestBody = "Øl får meg glad";
			Request request = new Request(null, null, null, new ByteArrayInputStream(originalRequestBody.getBytes()));
			String requestBody = request.getBody();
			assertEquals(originalRequestBody, requestBody);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
			throw e;
		}
	}
}