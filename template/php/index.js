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
        console.log(req.headers)
        let options = {
            url: 'http://localhost:9000',
            method: 'POST',
            data: req.body,
            headers: req.headers
        }
        let response = await axios(options);
        res.set(response.headers);
        res.status(response.status).send(response.data);
    } catch (error) {
        console.log(error);
        res.status(500).send(error);
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
