// Copyright (c) Alex Ellis 2017. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

import * as getStdin from 'get-stdin';

import * as handler from './function/handler';

getStdin().then(val => {
    handler.handle(val, (err, res: any) => {
        if (err) {
            return console.error(err);
        }
        if(Array.isArray(res) || isObject(res)) {
            console.log(JSON.stringify(res));
        } else {
            process.stdout.write(res);
        }
    });
}).catch(e => {
    console.error(e.stack);
});

const isObject = (obj: any) => {
    return obj === Object(obj);
};
