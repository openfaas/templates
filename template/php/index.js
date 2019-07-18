const express = require("express");
const axios = require("axios");
const bodyParser = require('body-parser');
 
const app = express();
app.use(bodyParser.json());
app.use(bodyParser.raw());
app.use(bodyParser.text({
    type: "text/*"
}));
app.disable('x-powered-by');


var middleware = async (req, res) => {
    try {
        let options = {
            url: 'http://localhost:9000',
            method: 'POST',
            data: req.body,
            headers: req.headers
        }
        let response = await axios(options);
        let {data} = response;
        let { body } = data;
        let headers = JSON.parse(data.headers);
        res.set(headers);
        res.status(response.status).send(body);
    } catch (error) {
        if( error.response ) {
            let { data } = error.response;
            let { body } = data;
            let headers = JSON.parse(data.headers);
            res.set(headers);
            res.status(error.response.status).send(body);
        } else if( error.request ) {
            console.log(error.request);
            res.status(500).send(error.message);
        } else {
            res.status(500).send(error.message);
        }
    }
};


app.post('/*', middleware);
app.get('/*', middleware);
app.patch('/*', middleware);
app.put('/*', middleware);
app.delete('/*', middleware);

const port = process.env.http_port || 3000;

app.listen(port, () => {
    console.log(`OpenFaaS Node.js listening on port: ${port}`)
});
