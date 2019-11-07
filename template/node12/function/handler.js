"use strict"

module.exports = async (event, context) => {
    let err;
    const result =             {
        status: "Received input: " + JSON.stringify(event.body)
    };

    return context
        .status(200)
        .succeed(result);
}
