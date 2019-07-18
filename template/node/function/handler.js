"use strict"

/**
 * Event object contains the following fields:
 * - body
 * - headers
 * - method
 * - query
 * - path
 */

module.exports = (event, context) => {
    let err;
    const result = {
        status: "You said: " + JSON.stringify(event.body)
    };

    context
        .headers({Authorization:'Bearer ...'})
        .status(200)
        .succeed(result);

    /**
     * If you wanna return an error
     * context
     *  .headers({})
     *  .status(<StatusCode>)
     *  .fail(<Error>)
     */
}