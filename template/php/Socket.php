    
<?php
include('vendor/autoload.php');
use prodigyview\network\Socket;
use prodigyview\system\Security;
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
$server = new Socket('127.0.0.1', 8005, array(
	'bind' => true,
	'listen' => true
));
$server->startServer('', function($message) {
	
	echo "Processing...\n";
	
	$response = "c mamo";
	//Return an encrypted message
	return $response;
	
}, 'closure');