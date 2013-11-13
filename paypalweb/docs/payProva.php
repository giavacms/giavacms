<?
include("config.php");
include("config.php");
include("func.inc");
OpenDB();
/ read the post from PayPal system and add 'cmd'
$req = 'cmd=_notify-validate';

foreach ($_POST as $key => $value) {
$value = urlencode(stripslashes($value));
$req .= "&$key=$value";
}

// post back to PayPal system to validate
$header .= "POST /cgi-bin/webscr HTTP/1.0\r\n";
$header .= "Content-Type: application/x-www-form-urlencoded\r\n";
$header .= "Content-Length: " . strlen($req) . "\r\n\r\n";
$fp = fsockopen ('www.paypal.com', 80, $errno, $errstr, 30);

// assign posted variables to local variables
$item_name = $_POST['item_name'];
$item_number = $_POST['item_number'];
$payment_status = $_POST['payment_status'];
$payment_amount = $_POST['mc_gross'];
$payment_currency = $_POST['mc_currency'];
$txn_id = $_POST['txn_id'];
$receiver_email = $_POST['receiver_email'];
$payer_email = $_POST['payer_email'];

if (!$fp) {
// HTTP ERROR
} else {
fputs ($fp, $header . $req);
while (!feof($fp)) {
$res = fgets ($fp, 1024);
if (strcmp ($res, "VERIFIED") == 0) {

$dati = array(				'item_name'=>q($item_name),
					'item_number'=>q($item_number),
					'payment_status' =>q($payment_status),
					'payment_amount' =>q($payment_amount),
					'payment_currency' =>q($payment_currency),
					'txn_id' =>q($txn_id),
					'receiver_email' =>q($receiver_email),
					'payer_email' =>q($payer_email),
					'status'  =>q('VERIFIED') );
$id = InsertIntoArray("payPalAcquisti", $dati);
// check the payment_status is Completed
// check that txn_id has not been previously processed
// check that receiver_email is your Primary PayPal email
// check that payment_amount/payment_currency are correct
// process payment
}
else if (strcmp ($res, "INVALID") == 0) {
// log for manual investigation
$dati = array(				'item_name'=>q($item_name),
					'item_number'=>q($item_number),
					'payment_status' =>q($payment_status),
					'payment_amount' =>q($payment_amount),
					'payment_currency' =>q($payment_currency),
					'txn_id' =>q($txn_id),
					'receiver_email' =>q($receiver_email),
					'payer_email' =>q($payer_email),
					'status'  =>q('INVALID') );
$id = InsertIntoArray("payPalAcquisti", $dati);
}
}
fclose ($fp);
}
?>
