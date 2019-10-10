package com.github.kameshchauhan.openfaas.function;

import com.github.kameshchauhan.openfaas.model.IHandler;
import com.github.kameshchauhan.openfaas.model.IRequest;
import com.github.kameshchauhan.openfaas.model.IResponse;
import com.github.kameshchauhan.openfaas.model.Response;

public class Handler implements IHandler {

    public IResponse Handle(IRequest req) {
        Response res = new Response();
	    res.setBody("Hello, World!");
	    return res;
    }
}
