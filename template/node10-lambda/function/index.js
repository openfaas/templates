// Test that global requires work
const aws4 = require('aws4')

// event == body
// context == runtime info
exports.handler = async(event, context) => {
    console.log(process.version)
    console.log(process.execPath)
    console.log(process.execArgv)
    console.log(process.argv)
    console.log(process.cwd())
    console.log(process.env)
    console.log(event)
    console.log(context)
    console.log(context.getRemainingTimeInMillis())
    console.log(aws4)
    return { message: 'hello world' }
}
