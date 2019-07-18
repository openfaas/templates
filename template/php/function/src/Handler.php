<?php
namespace App;

require ('Response.php');
use ResponseModel\Resp;

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
        //$response->StatusCode = 503;
        //$response->Body = "Hello, world!";
        $response = new Resp();
        $response->Body = "Hello, world!!";
        return $response;
    }
}

?>