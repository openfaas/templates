// Copyright (c) Alex Ellis 2021. All rights reserved.
// Copyright (c) OpenFaaS Author(s) 2021. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

"use strict"

const fs = require('fs');
const express = require('express')
const app = express()

let listenerHandlers = null;
let widgetHandlers = null;
const manifestHandler = require('./function/index');

const defaultMaxSize = '100kb' // body-parser default

app.disable('x-powered-by');

const rawLimit = process.env.MAX_RAW_SIZE || defaultMaxSize
const jsonLimit = process.env.MAX_JSON_SIZE || defaultMaxSize

app.use(function addDefaultContentType(req, res, next) {
    // When no content-type is given, the body element is set to
    // nil, and has been a source of contention for new users.

    if (!req.headers['content-type']) {
        req.headers['content-type'] = "text/plain"
    }
    next()
})

if (process.env.RAW_BODY === 'true') {
    app.use(express.raw({ type: '*/*', limit: rawLimit }))
} else {
    app.use(express.text({ type: "text/*" }));
    app.use(express.json({ limit: jsonLimit }));
    app.use(express.urlencoded({ extended: true }));
}

const middleware = async (req, res) => {
    // Checking whether middleware received a Resource or Action request
    if (req.body.resource) {
        handleAppResource(req, res);
    } else if (req.body.action) {
        handleAppListener(req, res);
    } else if (req.body.widget) {
        handleAppWidget(req, res);
    } else {
        handleAppManifest(req, res);
    }
};

function handleAppResource(req, res) {
    const resources_path = "./function/resources/";

    // Checking file extensions according to which ones Flutter can handle
    if (req.body.resource.match(/.*(\.jpeg|\.jpg|\.png|\.gif|\.webp|\.bmp|\.wbmp)$/)) {
        res.sendFile(req.body.resource, { root: resources_path });
    } else {
        res.sendStatus(404);
    }
}

async function handleAppManifest(req, res) {

    let uiStartTime = process.hrtime.bigint();

    let possibleFutureRes = manifestHandler();

    return Promise.resolve(possibleFutureRes)
        .then(manifest => {
            let uiStopTime = process.hrtime.bigint();

            widgetHandlers = manifest.widgets;
            listenerHandlers = manifest.listeners || {};
            manifest.widgets = Object.keys(widgetHandlers);
            manifest.listeners = Object.keys(listenerHandlers);

            res.status(200).json({ manifest: manifest, stats: { ui: Number(uiStopTime - uiStartTime) } });
        })
        .catch(err => {
            const err_string = err.toString ? err.toString() : err;
            console.error("handleAppManifest:", err_string);
            res.status(500).send(err_string);
        });
}

async function handleAppWidget(req, res) {


    let { widget, data, props } = req.body;

    let uiStartTime = process.hrtime.bigint();

    let possibleFutureRes = widgetHandlers[widget](data, props);

    return Promise.resolve(possibleFutureRes)
        .then(widget => {
            let uiStopTime = process.hrtime.bigint();

            res.status(200).json({ widget: widget, stats: { ui: Number(uiStopTime - uiStartTime) } });
        })
        .catch(err => {
            const err_string = err.toString ? err.toString() : err;
            console.error('handleAppWidget:', err_string);
            res.status(500).send(err_string);
        });
}

/**
 * This is the main entry point for the OpenFaaS function.
 *
 * This function will call the index.js file of the application
 * when the page change.
 * If an event is triggered, the matched event function provided by the app is triggered.
 * The event can be a listener or a widget update.
 */
async function handleAppListener(req, res) {

    let newData = {};

    let { action, data, props, event } = req.body;

    let listenersStartTime = process.hrtime.bigint();

    /*
        listeners file need to exactly math with action name
    */
    if (Object.keys(listenerHandlers).includes(action)) {
        let possibleFutureRes = listenerHandlers[action](data, props, event);

        return Promise.resolve(possibleFutureRes)
            .then(data => {
                let listenersStopTime = process.hrtime.bigint();
                newData = data;

                res.status(200).json({ data: newData, stats: { listeners: Number(listenersStopTime - listenersStartTime) } });
            })
            .catch(err => {
                const err_string = err.toString ? err.toString() : err;
                console.error('handleAppAction:', err_string);
                res.status(500).send(err_string);
            });
    } else {
        console.error(`No listener found for action ${action} in app manifest.`);
    }
}

//middleware to catch ressource
app.post('/*', middleware);

const port = process.env.http_port || 3000;

app.listen(port, () => {
    console.log(`node12 listening on port: ${port}`)
});
