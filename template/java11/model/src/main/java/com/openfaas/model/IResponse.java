// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.openfaas.model;

import java.util.Map;

public interface IResponse {
    String getBody();
    void setBody(String body);

    String getHeader(String key);
    void setHeader(String key, String value);
    Map<String, String> getHeaders();

    void setContentType(String contentType);
    String getContentType();

    void setStatusCode(int code);
    int getStatusCode();
}
