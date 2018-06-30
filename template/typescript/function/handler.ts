"use strict"

export function handle (context: string, callback: (err: any, res: any) => any) {
    callback(undefined, {input: context});
}
