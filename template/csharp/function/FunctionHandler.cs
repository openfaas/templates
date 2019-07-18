using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using response;

/**
 * For getting info about request,
 * See https://docs.microsoft.com/en-us/dotnet/api/microsoft.aspnetcore.http.httprequest?view=aspnetcore-2.2
 */

namespace Function
{
    public class FunctionHandler
    {
        public Response Handle(HttpRequest request)
        {
            Response response = new Response(); 
            //return Task.FromResult($"Hello! Your input was {input}");
            response.body = "Hello, World!";
            response.statusCode = 200;
            return response;
        }
    }

}