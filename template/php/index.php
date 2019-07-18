<?php
include ('vendor/autoload.php');
use prodigyview\network\Request;
use prodigyview\network\Response;
require ('function/src/Handler.php');
require_once ('./Response.php');

// Requires Function composer's autoload
if (file_exists('function/vendor/autoload.php')) {
    require('function/vendor/autoload.php');
}

//Create And Process The Current Request
$request = new Request();

//Get The Request Method(GET, POST, PUT, DELETE)
$method = strtolower($request->getRequestMethod());

//RETRIEVE Data From The Request
$data = $request->getRequestData('array');

//Route The Request
route($request);

function route($request) {
    $response = (new App\Handler())->handle($request);
    echo Response::createResponse($response->StatusCode, $response->Body);
    exit();
}

?>