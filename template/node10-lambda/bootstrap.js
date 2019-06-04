const http = require('http')
const { promisify } = require('util')

const RUNTIME_PATH = '/2018-06-01/runtime'

const {
  AWS_LAMBDA_FUNCTION_NAME,
  AWS_LAMBDA_FUNCTION_VERSION,
  AWS_LAMBDA_FUNCTION_MEMORY_SIZE,
  AWS_LAMBDA_LOG_GROUP_NAME,
  AWS_LAMBDA_LOG_STREAM_NAME,
  LAMBDA_TASK_ROOT,
  _HANDLER,
  AWS_LAMBDA_RUNTIME_API,
} = process.env

const [HOST, PORT] = AWS_LAMBDA_RUNTIME_API.split(':')

start()

async function start() {
  let handler
  try {
    handler = getHandler()
  } catch (e) {
    await initError(e)
    return process.exit(1)
  }
  try {
    await processEvents(handler)
  } catch (e) {
    console.error(e)
    return process.exit(1)
  }
}

async function processEvents(handler) {
  while (true) {
    const { event, context } = await nextInvocation()
    let result
    try {
      result = await handler(event, context)
    } catch (e) {
      await invokeError(e, context)
      continue
    }
    await invokeResponse(result, context)
  }
}

function initError(err) {
  return postError(`${RUNTIME_PATH}/init/error`, err)
}

async function nextInvocation() {
  const res = await request({ path: `${RUNTIME_PATH}/invocation/next` })

  if (res.statusCode !== 200) {
    throw new Error(`Unexpected /invocation/next response: ${JSON.stringify(res)}`)
  }

  if (res.headers['lambda-runtime-trace-id']) {
    process.env._X_AMZN_TRACE_ID = res.headers['lambda-runtime-trace-id']
  } else {
    delete process.env._X_AMZN_TRACE_ID
  }

  const deadlineMs = +res.headers['lambda-runtime-deadline-ms']

  let context = {
    awsRequestId: res.headers['lambda-runtime-aws-request-id'],
    invokedFunctionArn: res.headers['lambda-runtime-invoked-function-arn'],
    logGroupName: AWS_LAMBDA_LOG_GROUP_NAME,
    logStreamName: AWS_LAMBDA_LOG_STREAM_NAME,
    functionName: AWS_LAMBDA_FUNCTION_NAME,
    functionVersion: AWS_LAMBDA_FUNCTION_VERSION,
    memoryLimitInMB: AWS_LAMBDA_FUNCTION_MEMORY_SIZE,
    getRemainingTimeInMillis: () => deadlineMs - Date.now(),
  }

  if (res.headers['lambda-runtime-client-context']) {
    context.clientContext = JSON.parse(res.headers['lambda-runtime-client-context'])
  }

  if (res.headers['lambda-runtime-cognito-identity']) {
    context.identity = JSON.parse(res.headers['lambda-runtime-cognito-identity'])
  }

  const event = JSON.parse(res.body)

  return { event, context }
}

async function invokeResponse(result, context) {
  const res = await request({
    method: 'POST',
    path: `${RUNTIME_PATH}/invocation/${context.awsRequestId}/response`,
    body: JSON.stringify(result),
  })
  if (res.statusCode !== 202) {
    throw new Error(`Unexpected /invocation/response response: ${JSON.stringify(res)}`)
  }
}

function invokeError(err, context) {
  return postError(`${RUNTIME_PATH}/invocation/${context.awsRequestId}/error`, err)
}

async function postError(path, err) {
  const lambdaErr = toLambdaErr(err)
  const res = await request({
    method: 'POST',
    path,
    headers: {
      'Content-Type': 'application/json',
      'Lambda-Runtime-Function-Error-Type': lambdaErr.errorType,
    },
    body: JSON.stringify(lambdaErr),
  })
  if (res.statusCode !== 202) {
    throw new Error(`Unexpected ${path} response: ${JSON.stringify(res)}`)
  }
}

function getHandler() {
  const appParts = _HANDLER.split('.')

  if (appParts.length !== 2) {
    throw new Error(`Bad handler ${_HANDLER}`)
  }

  const [modulePath, handlerName] = appParts

  // Let any errors here be thrown as-is to aid debugging
  const app = require(LAMBDA_TASK_ROOT + '/' + modulePath)

  const userHandler = app[handlerName]

  if (userHandler == null) {
    throw new Error(`Handler '${handlerName}' missing on module '${modulePath}'`)
  } else if (typeof userHandler !== 'function') {
    throw new Error(`Handler '${handlerName}' from '${modulePath}' is not a function`)
  }

  return userHandler.length >= 3 ? promisify(userHandler) : userHandler
}

function request(options) {
  options.host = HOST
  options.port = PORT

  return new Promise((resolve, reject) => {
    let req = http.request(options, res => {
      let bufs = []
      res.on('data', data => bufs.push(data))
      res.on('end', () => resolve({
        statusCode: res.statusCode,
        headers: res.headers,
        body: Buffer.concat(bufs).toString(),
      }))
      res.on('error', reject)
    })
    req.on('error', reject)
    req.end(options.body)
  })
}

function toLambdaErr({ name, message, stack }) {
  return {
    errorType: name,
    errorMessage: message,
    stackTrace: (stack || '').split('\n').slice(1),
  }
}
