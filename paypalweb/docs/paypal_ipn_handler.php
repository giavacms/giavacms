<?
include("config.php");
include("classi.php");
include("func.inc");
OpenDB();
// assign posted variables to local variables
$payment_status = $_POST['payment_status'];
$total = $_POST['mc_gross'];
$txn_id = $_POST['txn_id'];
$payment_currency = $_POST['mc_currency'];
$cartid = $_POST['custom'];
$my_email = $_POST['business'];
$email = $_POST['payer_email'];
// read the post from PayPal system and add 'cmd'
$req="";
foreach ($_POST as $key => $value) {
    $req .= $key."=".urlencode($value) . "&";

}
$dati = array(		'acquisto'=>q('entro'),
				'totale'  =>q($total),
				'status'=> q($payment_status),
				'post' => q($req)
		);
$id = InsertIntoArray("log", $dati);

$req .= 'cmd=_notify-validate';

$fp = fsockopen (PAYPAL_URL, 80, $errno, $errstr, 30);
if (!$fp) 
    die();

// post back to PayPal system to validate
fputs($fp, "POST /cgi-bin/webscr HTTP/1.1\r\n"); 
fputs($fp, "Host: ".PAYPAL_URL."\r\n"); 
fputs($fp, "Content-type: application/x-www-form-urlencoded\r\n"); 
fputs($fp, "Content-length: ".strlen($req)."\r\n"); 
fputs($fp, "Connection: close\r\n\r\n"); 
fputs($fp, $req . "\r\n\r\n"); 

//loop through the response from the server 
$info = "";
while(!feof($fp)) { 
    $info .= @fgets($fp, 1024); 
}
if(strstr($info, "VERIFIED") != ''){
    if($payment_currency == "EUR" &&
       $payment_status == "Completed" &&
       $my_email == MY_EMAIL) {
        // Check for duplicate transactions through $txn_id

        $query = "select spediz, totale from acquisti where id = '".$cartid."' ";
		
		$result = GetDBResult($query);
		$num_rows = mysql_num_rows($result);
		if ($num_rows >0) {
			while ( $row = mysql_fetch_array($result)) {
				$totale = $row['totale'];
				$sped = $row['spediz'];
			}
			$totale = $totale + $sped - $total;
			if ($totale == 0) {
				$pagato = $total;
				$stato = "in consegna";
			} else {
				$pagato = $total;
				$stato = "pagamento scorretto";
			}
			$tot_agg = array('stato' => q($stato), 'pagato' =>q($pagato));
			$where = " id = '".$cartid."'";
			UpdateArray("acquisti", $tot_agg, $where);
		} else {
			$dati = array(		'acquisto'=>q($cartid),
				'totale'  =>q($total),
				'status'=> q($payment_status),
				'post' => q($_POST)
				);
			$id = InsertIntoArray("log", $dati);
		}
    }
}
else{
	$dati = array(		'acquisto'=>q($cartid),
				'totale'  =>q($total),
				'status'=> q($payment_status),
				'post' => q($req)
		);
	$id = InsertIntoArray("log", $dati);
}
$dati = array(		'acquisto'=>q('esco'),
				'totale'  =>q($total),
				'status'=> q($payment_status),
				'post' => q($req)
		);
$id = InsertIntoArray("log", $dati);
fclose ($fp);
?>

