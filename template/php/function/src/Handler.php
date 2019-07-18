<?php
namespace App;

require ('Response.php');
use ResponseModel\Resp;

/**
 * To retrieve the Request Method(GET, POST, PUT, DELETE)
 *   $method = strtolower($request->getRequestMethod());
 * To retrieve data from the request
 *   $data = $request->getRequestData($format); // The format
	 * 						can be set to the options of 'json', 'array' and 'object', and will return the
	 * 						data in each of those types.
 *  To retrieve header
 *   $header = $request->getHeader($headerName);
 */ 

/**
 * Class Handler
 * @package App
 */
class Handler
{
    /**
     * @return
     */
    public function handle($request) {
        // Build the response object
        $response = new Resp();
        $response->StatusCode = 404;
        
        // Headers should be written in this way
        $keys = array();
        $keys['asd'] = 'bc';
        $response->Headers = $keys;

        $response->Body = ['asd' => '123'];
        // Be sure your body is encoded 

        $response->Body = json_encode($response->Body);
        return $response;
    }
}

?>