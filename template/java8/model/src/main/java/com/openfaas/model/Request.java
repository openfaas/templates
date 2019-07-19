// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package com.openfaas.model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;


public class Request implements IRequest {

    private Map<String, String> headers;
    private String body;
    private Map<String, String> queryParameters;
    private String queryRaw;
    private String pathRaw;
    private Map<String, String> path;
    private InputStream inputStream;

	public Request(String body, Map<String, String> headers) {
		this(body, headers, "", "");
	}

    public Request(String body, Map<String, String> headers, String queryRaw, String path) {
    	this(headers, queryRaw, path, new ByteArrayInputStream(body != null ? body.getBytes(StandardCharsets.UTF_8) : new byte[0]));

    }

    public Request(Map<String, String> headers, String queryRaw, String path, InputStream inputStream) {
        this.headers = headers;
        this.queryRaw = queryRaw;
	    this.inputStream = inputStream;
	    this.queryParameters = this.parseQueryParameters();
        this.pathRaw = path;
        this.path = this.parsePathParameters();
    }

    public String getBody() {
	    if (this.body != null) {
	    	return this.body;
	    }
	    try {
		    ByteArrayOutputStream result = new ByteArrayOutputStream();
		    byte[] buffer = new byte[1024];
		    int length;
		    while ((length = inputStream.read(buffer)) != -1) {
			    result.write(buffer, 0, length);
		    }
		    this.body = result.toString(StandardCharsets.UTF_8.name());
		    return this.body;
	    } catch (IOException e) {
		    // ByteArrayOutputStream.write does not actually throw any IOException,
		    // but OutputStream.write is declared to throw one.
		    // To not break getBody(), throw the exception wrapped in a RuntimeException, but it should never be thrown
		    throw new RuntimeException(e);
	    }
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

    @Override
    public String getPathRaw() {
        return pathRaw;
    }

    @Override
    public Map<String, String> getPath() {
        return path;
    }

    private Map<String, String> parsePathParameters() {
        Map<String, String> res = new HashMap<String, String>();
        if (pathRaw != null && pathRaw.length() > 0) {
            String firstLetter = pathRaw.substring(0,1);
            String[] params = pathRaw.substring(1).split("/");

            String key = "";
            for (String param : params) {
                if (key.length() > 0) {
                    res.put(key, param);
                    key = "";
                } else {
                    key = param;
                }
            }
            if (key.length() > 0 ) {
                res.put(key, "");
            }
        }

        return res;
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

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}
}