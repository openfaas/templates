// Some portions of this code are Copyright 2019 Oath Inc. Licensed under the terms of the project license.
// Copyright (c) Alex Ellis 2017. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

"use strict"

const function_name = process.env.function_name || "test"
const cid  = process.env.cid || "0"
const pop  = process.env.pop || "aaa"
const port = process.env.http_port || 8080
const node = process.env.node_name || 'node-1'
const prometheus = require('prom-client')
const express    = require('express')
const http       = require('http')
const handler    = require('./function/handler')
const bodyParser = require('body-parser')
const wasm       = require('./function/function')

const { createTerminus } = require('@godaddy/terminus')

const labels = {
    'customer': cid,
    'pop': pop,
    'node': node,
    'fx': function_name,
    'pid': process.pid
}

prometheus.register.setDefaultLabels(labels)
const default_metrics = prometheus.collectDefaultMetrics({
    prefix: function_name+'_',
    timeout: 5000
})

const app = express()
app.use(bodyParser.json())
app.use(bodyParser.raw())
app.use(bodyParser.text({ type : "text/*" }))
app.use(bodyParser.urlencoded({ extended: true }))
app.disable('x-powered-by')

// controller
async function run(gen){

    let res = await gen.next()

    // schedule to run again
    if (!res.done) {
	setImmediate(run, gen)
    }
}

const handle_function = (req, res) => {
    // start control loop with generator
    try {
	run(handler(req, res, wasm))
    } catch(e) {
	res.status(500).send(e.message)
    }
}

const handle_metrics = (req, res) => {
    res.set('Content-Type', prometheus.register.contentType)
    res.end(prometheus.register.metrics())
}

app.get('/_/metrics', handle_metrics)
app.all('/*', handle_function)

const server = http.createServer(app)

function onShutdown () {
    console.log('Server starting shutdown')
    server.close()
    clearInterval(default_metrics())
}

function healthCheck () {
    return Promise.resolve('OK')
}

const options = {
    // healtcheck options
    healthChecks: {
	'/_/health': healthCheck    // a promise returning function indicating service health
    },

    // cleanup options
    timeout: 10000,                   // [optional = 1000] number of milliseconds before forcefull exiting
    signals: ['SIGINT', 'SIGTERM'],  // [optional = []] array of signals to listen for relative to shutdown
    onShutdown: onShutdown,          // [optional] called right before exiting
};

createTerminus(server, options)

server.listen(port, () => {
    console.log(`Server listening on port: ${port}`)
})
