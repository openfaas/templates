using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using response;

namespace Function
{
    public class FunctionHandler
    {
        public Response Handle(HttpContext context)
        {
            Response response = new Response(); 
            //return Task.FromResult($"Hello! Your input was {input}");
            response.body = "Hello, World!";
            response.statusCode = 200;
            return response;
        }
    }

}