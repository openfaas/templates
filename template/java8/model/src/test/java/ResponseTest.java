// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

import org.junit.Test;
import static org.junit.Assert.*;

import com.openfaas.model.IHandler;

import com.openfaas.model.Response;

import java.util.Arrays;

public class ResponseTest {
    @Test public void testResponseHeaderSetGetValue() {
       Response r = new Response();

       r.setHeader("X-Key", "value");

       assertEquals("value", r.getHeader("X-Key"));
    }

    @Test public void testResponseHeaderGetValueKeyMissIsNull() {
       Response r = new Response();
       assertEquals(null, r.getHeader("No-Such-Key"));
    }

    @Test public void testResponseHeaderSetGetClearGetValue() {
       Response r = new Response();

       r.setHeader("X-Key", "value");
       r.setHeader("X-Key", null);

       assertEquals(null, r.getHeader("X-Key"));
    }

    @Test public void testResponseGetSetContentType() {
       Response r = new Response();

       r.setContentType("application/json");

       assertEquals("application/json", r.getContentType());
    }

    @Test public void testResponseStatusCodeDefaultValue() {
      Response r = new Response();
      assertEquals(200, r.getStatusCode());
    }

    @Test public void testResponseStatusCodeSetGetValue() {
      Response r = new Response();
      r.setStatusCode(404);
      assertEquals(404, r.getStatusCode());
    }

    @Test public void testResponseSetBodyWithString() {
        Response r = new Response();
        r.setBody("Øl får meg glad");
        assertEquals(r.getBody(), "Øl får meg glad");
    }

    @Test public void testResponseSetBodyWithBytes() {
        Response r = new Response();
        r.setBodyData("Øl får meg glad".getBytes());
        assertEquals(r.getBody(), "Øl får meg glad");

        assertTrue(Arrays.equals(r.getBodyData(), "Øl får meg glad".getBytes()));
    }
}
