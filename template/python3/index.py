# Some portions of this code are Copyright 2019 Oath Inc. Licensed under the terms of the project license.
# Copyright (c) Alex Ellis 2017. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.

from flask import Flask, request
from function import handler
from gevent.pywsgi import WSGIServer

app = Flask(__name__)

@app.before_request
def fix_transfer_encoding():
    """
    Sets the "wsgi.input_terminated" environment flag, thus enabling
    Werkzeug to pass chunked requests as streams.  The gunicorn server
    should set this, but it's not yet been implemented.
    """

    transfer_encoding = request.headers.get("Transfer-Encoding", None)
    if transfer_encoding == u"chunked":
        request.environ["wsgi.input_terminated"] = True

@app.route("/", defaults={"path": ""}, methods=["POST", "GET", "HEAD", "PUT", "DELETE", "OPTIONS"])
@app.route("/<path:path>", methods=["POST", "GET", "HEAD", "PUT", "DELETE", "OPTIONS"])
def main_route(path):
    ret = handler.handle(request)
    return ret



if __name__ == '__main__':
    http_server = WSGIServer(('', 8080), app)
    http_server.serve_forever()
