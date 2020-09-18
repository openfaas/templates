# Copyright (c) Alex Ellis 2017. All rights reserved.
# Licensed under the MIT license. See LICENSE file in the project root for full license information.

import sys
import os
from function import handler


# distutils.util.strtobool() can throw an exception
def is_true(val):
    return val and val.lower() == "true" or val == "1"


def get_stdin():
    buf = ""
    while(True):
        line = sys.stdin.readline()
        buf += line
        if line == "":
            break
    return buf


if(__name__ == "__main__"):
    st = get_stdin()

    raw_body = os.getenv("RAW_BODY")
    if is_true(raw_body):
        st = st.encode("utf-8", "surrogateescape")

    ret = handler.handle(st)
    if ret != None:
        print(ret)
