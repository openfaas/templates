// Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

import express = require('express');
import { Request, Response, Application } from 'express';
import { json, raw, text } from 'body-parser';

import * as fn from './function/handler';
import { FunctionCallBack, FunctionContext, FunctionEvent } from './function/types';

const app: Application = express();

app.use(json());
app.use(raw());
app.use(text({type: 'text/*'}));

app.disable('x-powered-by');

function middleware (req: Request, res: Response): void {
    let cb: FunctionCallBack = (err: Error, fnResult: any) => {
        if (err) {
            return res.status(500).send(err);
        }

        if (Array.isArray(fnResult) || isObject(fnResult)) {
            res.set(fnContext.headers()).status(fnContext.status()).send(JSON.stringify(fnResult));
        } else {
            res.set(fnContext.headers()).status(fnContext.status()).send(fnResult);
        }
    };

    let fnEvent = new FunctionEvent(req);
    let fnContext = new FunctionContext(cb);

    fn.handler(fnEvent, fnContext, cb);
}

app.post('/', middleware);
app.get('/', middleware);

const port: number = parseInt(process.env.http_port) || 3000;

app.listen(port, () => {
    console.log(`OpenFaas Typescript listening on port: ${port}`);
});

const isObject = (obj: any) => {
    return obj === Object(obj);
};
