// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.openfaas.model;

import java.util.Map;

public class Request implements IRequest {

    private Map<String, String> headers;
    private String body;

    public Request(String body, Map<String, String> headers) {
        this.body = body;
        this.headers = headers;
    }

    public String getBody() {
        return this.body;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getHeader(String key) {
        if(!this.headers.containsKey(key)) {
            return null;
        }

        return this.headers.get(key);
    }
}
