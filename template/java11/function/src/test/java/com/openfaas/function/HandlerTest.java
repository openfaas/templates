package com.openfaas.function;

import org.junit.Test;
import static org.junit.Assert.*;

import com.openfaas.function.Handler;

public class HandlerTest {

    @Test
    public void handlerIsNotNull() {
        Handler handler = new Handler();
        assertNotNull("Expected handler not to be null", handler);
    }
}
