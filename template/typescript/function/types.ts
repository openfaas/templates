type Handler<TContext = any, TResult = any> = (
  context: TContext,
  callback: Callback<TResult>
) => void;

type Callback<TResult = any> = (
  error?: Error | null | string,
  result?: TResult
) => void;
