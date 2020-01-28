// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.openfaas.model;

import java.util.Map;

public interface IRequest {
    String getBody();
    Map<String, String> getHeaders();
    String getHeader(String key);
    String getQueryRaw();
    Map<String, String> getQuery();
    String getPathRaw();
    Map<String, String> getPath();
}