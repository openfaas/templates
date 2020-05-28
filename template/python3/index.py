# Copyright (c) Alex Ellis 2017. All rights reserved.
# Copyright (c) OpenFaaS Author(s) 2018. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.

import sys
import os
import json
from cgi import parse_header
from function import handler

if __name__ == "__main__":
    st = sys.stdin.buffer.read()
    # decode text to string
    content_type, type_options = parse_header(os.getenv('Http_Content_Type', ''))
    if not content_type or content_type.startswith('text/') or content_type == 'application/x-www-form-urlencoded':
        encoding = type_options.get('charset', 'utf-8')
        try:
            st = st.decode(encoding)
        except UnicodeDecodeError:
            # if explicitly defined encoding can't decode bytes, fail
            if 'charset' in type_options:
                raise
            # otherwise utf-8 was incorrect guess, then keep as bytes
        # unknown encoding (LookupError) will fail and propagate
    # decode JSON and JSON-LD to dictionary
    elif content_type.endswith('json'):
        st = json.load(st)
    # otherwise keep bytes

    ret = handler.handle(st)

    # encode response to JSON if necessary
    if ret is not None:
        if isinstance(ret, bytes):
            sys.stdout.buffer.write(ret)
        elif isinstance(ret, str):
            sys.stdout.write(ret)
        else:
            json.dump(ret, sys.stdout)
