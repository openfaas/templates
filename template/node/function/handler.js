"use strict"

// A simple delay function
const delayCallback = cb => {
    const min = 0; // 0 sec
    const max = 2; // 2 sec
    const delay = Math.round((Math.random() * (max - min) + min));
    setTimeout(() => cb(delay),  delay * 1000);
}

// callback version
module.exports = (context, callback) => {
    delayCallback(delay => callback(undefined, {status: "done", delay}))    
}

// Uncomment the following line to use it with the promise or async/await versions
//const delayPromise = () => new Promise(resolve => delayCallback(delay => resolve(delay)) )

/*
module.exports = context => new Promise((resolve, reject) => {
    delayPromise()
    .then(delay => {
        resolve({status: "promise", delay});
    })
})
*/

/*
module.exports = async context => {
    const delay = await delayPromise();
    return {status: "async/await", delay}
}
*/