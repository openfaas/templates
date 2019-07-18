using System;
using System.Collections.Generic;

namespace response {
    public class Response {
        

        public Response() { 
            headers = new Dictionary<string, string>();
            statusCode = 200;
            body = "";
        }
        public int statusCode;

        public Dictionary<string, string> headers;

        public string body;

    }
}