function handle(context: FaasHandlerContext, callback: FaaSHandlerCallback) {
    return callback(undefined, {status: "done"});
}

export = handle