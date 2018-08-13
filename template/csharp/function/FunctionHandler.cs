using System;
using System.Text;

namespace Function
{
    public class FunctionHandler
    {
        public string Handle(string input) {
            return string.Format("Hi there - your input was: {0}\n", input);
        }
    }
}
