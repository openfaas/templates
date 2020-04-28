// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.openfaas.model;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Response implements IResponse {

    private int statusCode = 200;
    private byte[] body;
    private String contentType;
    private Map<String, String> headers;

    public Response() {
        this.body = null;
        this.contentType = "";
        this.headers = new HashMap<String, String>();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeader(String key, String value) {
        if(value == null) {
            if(this.headers.containsKey(key)) {
                this.headers.remove(key);
                return;
            }
        }
        this.headers.put(key, value);
    }

    public String getHeader(String key) {
        if(!this.headers.containsKey(key)) {
            return null;
        }

        return this.headers.get(key);
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setBody(String body) {
        try {
            this.body = body.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            this.body = null;
        }
    }

    public void setBodyBytes(byte[] body) {
        this.body = body;
    }

    public String getBody() {
        try {
            return new String(this.body, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(this.body);
        }
    }

    public byte[] getBodyBytes() {
        return this.body;
    }
}
