using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using Function;
using response;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.DependencyInjection;

namespace csharp_web
{
    public class Startup
    {
        // This method gets called by the runtime. Use this method to add services to the container.
        // For more information on how to configure your application, visit https://go.microsoft.com/fwlink/?LinkID=398940
        public void ConfigureServices(IServiceCollection services)
        {
        }

        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.Run(async (context) =>
            {   
                var functionHandler = new FunctionHandler();
                try
                {
                    var response = functionHandler.Handle(context.Request);
                    context.Response.StatusCode = response.statusCode;
                    foreach(KeyValuePair<string, string> entry in response.headers)
                    {
                        HeaderDictionaryExtensions.Append(context.Response.Headers, entry.Key, entry.Value);
                    }
                    await context.Response.WriteAsync(response.body);
                }
                catch (Exception ex)
                {
                    Console.Write(ex);
                    context.Response.StatusCode = (int)HttpStatusCode.InternalServerError;
                    await context.Response.WriteAsync(ex.Message);
                }
            });
        }

        private string getRequest(Stream inputBody)
        {
            StreamReader reader = new StreamReader(inputBody);
            string text = reader.ReadToEnd();
            return text;
        }
    }

}