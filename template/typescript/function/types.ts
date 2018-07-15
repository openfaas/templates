import {Request} from 'express';
import { IncomingHttpHeaders } from 'http2';

export class FunctionEvent {
  public body: any;
  public headers: {};
  public method: string;
  public query: any;

  constructor(req: Request) {
    this.body = req.body;
    this.headers = req.headers;
    this.method = req.method;
    this.query = req.query;
  }
}

export type FunctionCallBack = (err: Error, result: any) => {}

export class FunctionContext {
  public value: number;
  public callBack: FunctionCallBack;
  public headerValues: IncomingHttpHeaders;

  constructor(callBack: FunctionCallBack) {
    this.value = 200;
    this.callBack = callBack;
    this.headerValues = {};
  }

  status(value?: number): number {
    if (!value || value == 0) {
      return this.value;
    }
    this.value = value;
    return this.value;
  }
    
  headers(value?: IncomingHttpHeaders): object {
    if (!value) {
      return this.headerValues;
    }

    this.headerValues = value;
    return this.headerValues;
  }

  succeed(value: any) {
    this.callBack(null, value);
  }

  fail(value: Error) {
    this.callBack(value, null);
  }
}