import { FunctionEvent, FunctionContext, FunctionCallBack } from "./types";

export function handler(event: FunctionEvent, context: FunctionContext, callback: FunctionCallBack) {
  const result = {
    status: 'You said: ' + JSON.stringify(event.body)
  }

  context.status(200);
  context.succeed(result);
}