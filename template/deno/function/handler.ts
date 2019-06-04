// import { xxx } from "/home/app/function/package.ts"

const encoder = new TextEncoder();

export function handler(req) {
       let headers = new Headers();
       headers.set("X-Test-Header", "awesome");
       return {status: 200, body: encoder.encode("hello world"), headers};
};
