"use strict"

module.exports = async (event, context) => {
    const result =             {
        status: "Input received: " + JSON.stringify(event.body)
    };

    return result;
}
