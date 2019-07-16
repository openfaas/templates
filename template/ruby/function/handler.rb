class Handler
  def run(body, headers)
    response_headers = {"content-type": "text/plain"}
    body = "Hello world from the Ruby template"

    return body, response_headers
  end
end