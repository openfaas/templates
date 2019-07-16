# Copyright (c) Alex Ellis 2017. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.

require_relative 'function/handler'

require 'sinatra'

set :port, 5000
# set :bind, '0.0.0.0'

handler = Handler.new

get '/*' do
  res, res_headers = handler.run request.body, request.env

  headers = res_headers

  return res
end

post '/*' do
    res, res_headers = handler.run request.body, request.env
    headers = res_headers
    return res
end

put '/*' do
    res, res_headers = handler.run request.body, request.env
    headers = res_headers

    return res
end

delete '/*' do
    res, res_headers = handler.run request.body, request.env
    headers = res_headers

    return res
end