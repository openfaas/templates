# -*- coding: utf-8 -*-
from flask import Response
import json

def handle(req):
    """handle a request to the function
    Args:
        req (str): request body
    """

    data = {
        'hello'  : 'world',
        'number' : 3
    }
    js = json.dumps(data)

    resp = Response(js, status=300, mimetype='application/json')
    resp.headers['Authorization'] = 'Bearer abc123'

    return resp
