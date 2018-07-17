// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.openfaas.model;

import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

public class Request implements IRequest {

    private Map<String, String> headers;
    private String body;
    private Map<String, String> queryParameters;
    private String queryRaw;

    public Request(String body, Map<String, String> headers) {
        this.body = body;
        this.headers = headers;
    }
    
    public Request(String body, Map<String, String> headers,String queryRaw) {
        this.body = body;
        this.headers = headers;
        this.queryRaw = queryRaw;
        this.queryParameters = this.parseQueryParameters();
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
    
    @Override
	public String getQueryRaw() {
		return queryRaw;
	}

    @Override
	public Map<String, String> getQuery() {
    	return queryParameters;
    }
    
	private Map<String, String> parseQueryParameters() {
		Map<String, String> reqParametersMap = new HashMap<String, String>();
		if (queryRaw != null) {
			String pairs[] = queryRaw.split("[&]");

			for (String pair : pairs) {
				String param[] = pair.split("[=]");

				String key = null;
				String value = null;
				try {
					if (param.length > 0) {
						key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
					}

					if (param.length > 1) {
						value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
					}
					reqParametersMap.put(key, value);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return reqParametersMap;
	}

}
