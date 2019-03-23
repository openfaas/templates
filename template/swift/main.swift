import Foundation

let handler = Handler()
let standardInput = FileHandle.standardInput
let input = String(data: standardInput.availableData, encoding: .utf8)

print(handler.handle(args: input!))
