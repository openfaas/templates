"use strict"

module.exports = handler;

async function* handler(req, res) {

    try {
	let p = 0
	let s = 1

	// compute fibonacci sequence with yielding
	for (let i=0; i<req.query.n; i+=1) {
	    if (i % 100 == 0) yield null
	    let a = s // save
	    s += p
	    p = a
	}

	res.status(200).send(p.toString())
    } catch (e) {
	res.status(500).send(e.message);
    }
}
