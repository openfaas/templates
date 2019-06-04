"use strict"

module.exports = handler;

async function* handler(req, res, exports) {

    try {
	let p = exports._fib(req.query.n)
	res.status(200).send(p.toString())
    } catch (e) {
	res.status(500).send(e.message);
    }
}
