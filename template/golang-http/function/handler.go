package function

import (
	"fmt"
	"github.com/openfaas-incubator/go-function-sdk"
	"net/http"
	"net/textproto"
)

/**
 * Request contains the following fields:
 *	Header:      r.Header,
 *	Method:      r.Method,
 *	QueryString: r.URL.RawQuery,
 * This template is based on openfaas-incubator template.
 *	For further information look https://github.com/openfaas-incubator/golang-http-template
 */

// Handle a function invocation
func Handle(req handler.Request) (handler.Response, error) {
	var err error

	message := fmt.Sprintf("Hello world, input was: %s", string(req.Body))
	header := make(map[string][]string)
	textproto.MIMEHeader(header).Add("Authorization", "Bearer ...")
	return handler.Response{
		Header:     header,
		Body:       []byte(message),
		StatusCode: http.StatusOK,
	}, err
}
