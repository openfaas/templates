// Copyright (c) Alex Ellis 2017. All rights reserved.
// Copyright (c) OpenFaaS Author(s) 2020. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

"use strict"

const express = require('express')
const app = express()
const handler = require('./function/handler');
const bodyParser = require('body-parser')

if (process.env.RAW_BODY === 'true') {
    app.use(bodyParser.raw({ type: '*/*' }))
} else {
    var jsonLimit = process.env.MAX_JSON_SIZE || '100kb' //body-parser default
    app.use(bodyParser.json({ limit: jsonLimit}));
    app.use(bodyParser.raw()); // "Content-Type: application/octet-stream"
    app.use(bodyParser.text({ type : "text/*" }));
}

app.disable('x-powered-by');

class FunctionEvent {
    constructor(req) {
        this.body = req.body;
        this.headers = req.headers;
        this.method = req.method;
        this.query = req.query;
        this.path = req.path;
    }
}

class FunctionContext {
    constructor(cb) {
        this.value = 200;
        this.cb = cb;
        this.headerValues = {};
        this.cbCalled = 0;
    }

    status(value) {
        if(!value) {
            return this.value;
        }

        this.value = value;
        return this;
    }

    headers(value) {
        if(!value) {
            return this.headerValues;
        }

        this.headerValues = value;
        return this;    
    }

    succeed(value) {
        let err;
        this.cbCalled++;
        this.cb(err, value);
    }

    fail(value) {
        let message;
        this.cbCalled++;
        this.cb(value, message);
    }
}

var middleware = async (req, res) => {
    let cb = (err, functionResult) => {
        if (err) {
            console.error(err);

            return res.status(500).send(err.toString ? err.toString() : err);
        }

        if(isArray(functionResult) || isObject(functionResult)) {
            res.set(fnContext.headers()).status(fnContext.status()).send(JSON.stringify(functionResult));
        } else {
            res.set(fnContext.headers()).status(fnContext.status()).send(functionResult);
        }
    };

    let fnEvent = new FunctionEvent(req);
    let fnContext = new FunctionContext(cb);

    Promise.resolve(handler(fnEvent, fnContext, cb))
    .then(res => {
        if(!fnContext.cbCalled) {
            fnContext.succeed(res);
        }
    })
    .catch(e => {
        cb(e);
    });
};

app.post('/*', middleware);
app.get('/*', middleware);
app.patch('/*', middleware);
app.put('/*', middleware);
app.delete('/*', middleware);
app.options('/*', middleware);

const port = process.env.http_port || 3000;

app.listen(port, () => {
    console.log(`OpenFaaS Node.js listening on port: ${port}`)
});

let isArray = (a) => {
    return (!!a) && (a.constructor === Array);
};

let isObject = (a) => {
    return (!!a) && (a.constructor === Object);
};
