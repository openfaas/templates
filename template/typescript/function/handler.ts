
export const handle: Handler<string, any> = (context, callback) => {
  callback(undefined, { input: context });
};
