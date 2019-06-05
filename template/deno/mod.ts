/*
Copyright (c) Oath, Inc 2019. All rights reserved.
Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

import { serve, Response } from "./package.ts"
import { handler } from "function/handler.ts"

const s = serve("0.0.0.0:8080");

/** Start handling requests */
async function main() {
  for await (const req of s) {
      req.respond(handler(req));
  }
}

main();
