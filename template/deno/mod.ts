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
