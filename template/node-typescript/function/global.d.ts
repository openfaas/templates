type FaaSHandlerCallback = (error: any, result: any) => void;
type FaasHandlerContext = string | null;
type FaasHandler = (context: string | null, callback: FaaSHandlerCallback ) => void;