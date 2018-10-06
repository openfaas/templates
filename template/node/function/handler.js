"use strict"

//module.exports = (context, callback) => {
//    callback(undefined, {status: "done"});
//}

module.exports = context => new Promise((resolve, reject) => {
    resolve({status: "promise"})
})
