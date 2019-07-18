package com.openfaas.function;

import com.openfaas.model.IHandler;
import com.openfaas.model.IResponse;
import com.openfaas.model.IRequest;
import com.openfaas.model.Response;

/**
 * See https://github.com/openfaas/templates/blob/master/template/java8/model/src/main/java/com/openfaas/model/IRequest.java
 * for the request interface, and 
 * https://github.com/openfaas/templates/blob/master/template/java8/model/src/main/java/com/openfaas/model/IResponse.java
 * for the response interface
 */

public class Handler implements com.openfaas.model.IHandler {

    public IResponse Handle(IRequest req) {
        Response res = new Response();
	    res.setBody("Hello, world!");

	    return res;
    }
}
