<?
include("config.php");
include("classi.php");
session_start();
session_register("carrello");
session_register("last");
session_register("user");
include("config.php");
include("func.inc");
OpenDB();
include("top.foot.php");

if ($_SESSION['carrello']== "") {
	header("Location: index.php"); /* Ridireziona il browser */
	exit;
}
$error =0;
$sped =0;
//print_r($_POST);
if (isset($_POST['autorizzo']) &&  $_POST['autorizzo']=="on") {
	$_SESSION['user']['autorizzo'] = $_POST['autorizzo'];
	$_SESSION['user']['autorizzo_e'] = "";
} else {
	$_SESSION['user']['autorizzo'] = "";
	$_SESSION['user']['autorizzo_e'] = "Autorizzazione obbligatoria.";
	$error++;
}
if (isset($_POST['user']['nome']) &&  $_POST['user']['nome']!="") {
	$_SESSION['user']['nome'] = $_POST['user']['nome'];
	$_SESSION['user']['nome_e'] = "";
} else {
	$_SESSION['user']['nome'] = "";
	$_SESSION['user']['nome_e'] = "manca il nome.";
	$error++;
}
if (isset($_POST['user']['cognome']) &&  $_POST['user']['cognome']!="") {
	$_SESSION['user']['cognome'] = $_POST['user']['cognome'];
	$_SESSION['user']['cognome_e'] = "";
} else {
	$_SESSION['user']['cognome'] = "";
	$_SESSION['user']['cognome_e'] = "manca il cognome.";
	$error++;
}
if (isset($_POST['user']['email']) &&  $_POST['user']['email']!="" && validaEmail($_POST['user']['email']) !=0) {
	$_SESSION['user']['email'] = $_POST['user']['email'];
	$_SESSION['user']['email_e'] = "";
} else {
	$_SESSION['user']['email'] = "";
	$_SESSION['user']['email_e'] = "manca l'email.";
	$error++;
}
if (isset($_POST['user']['codfisc']) &&  $_POST['user']['codfisc']!="") {
	$_SESSION['user']['codfisc'] = $_POST['user']['codfisc'];
	$_SESSION['user']['codfisc_e'] = "";
} else {
	$_SESSION['user']['codfisc'] = "";
	$_SESSION['user']['codfisc_e'] = "manca cod.fiscale/p.iva.";
	$error++;
}

if (isset($_POST['user']['ragionesociale']) &&  $_POST['user']['ragionesociale']!="") {
	$_SESSION['user']['ragionesociale'] = $_POST['user']['ragionesociale'];
}else {
	$_SESSION['user']['ragionesociale'] = "";
}
if (isset($_POST['user']['indirizzo_fatturazione']) &&  $_POST['user']['indirizzo_fatturazione']!="") {
	$_SESSION['user']['indirizzo_fatturazione'] = $_POST['user']['indirizzo_fatturazione'];
	$_SESSION['user']['indirizzo_fatturazione_e'] = "";
} else {
	$_SESSION['user']['indirizzo_fatturazione'] = "";
	$_SESSION['user']['indirizzo_fatturazione_e'] = "manca l'indirizzo.";
	$error++;
}
if (isset($_POST['user']['cap']) &&  $_POST['user']['cap']!="") {
	$_SESSION['user']['cap'] = $_POST['user']['cap'];
	$_SESSION['user']['cap_e'] = "";
} else {
	$_SESSION['user']['cap'] = "";
	$_SESSION['user']['cap_e'] = "manca il cap.";
	$error++;
}
if (isset($_POST['user']['city']) &&  $_POST['user']['city']!="") {
	$_SESSION['user']['city'] = $_POST['user']['city'];
	$_SESSION['user']['city_e'] = "";
} else {
	$_SESSION['user']['city'] = "";
	$_SESSION['user']['city_e'] = "manca la citta'.";
	$error++;
}
if (isset($_POST['user']['provincia']) &&  $_POST['user']['provincia']!="" &&  $_POST['user']['provincia']!="--") {
	$_SESSION['user']['provincia'] = $_POST['user']['provincia'];
	$_SESSION['user']['provincia_e'] = "";
} else {
	$_SESSION['user']['provincia'] = "";
	$_SESSION['user']['provincia_e'] = "manca la provincia.";
	$error++;
}
if (isset($_POST['user']['telefono']) &&  $_POST['user']['telefono']!="") {
	$_SESSION['user']['telefono'] = $_POST['user']['telefono'];
	$_SESSION['user']['telefono_e'] = "";
} else {
	$_SESSION['user']['telefono'] = "";
	$_SESSION['user']['telefono_e'] = "manca il telefono.";
	$error++;
}
if ($error >0){
	$_SESSION['error'] =1;
	header("Location: concludiacquisto.php"); /* Redirect browser */
	exit;
} else {
	//echo "tutto ok!!";
}

if (isset($_POST['user']['nome_sped']) &&  $_POST['user']['nome_sped']!="") {
	$_SESSION['user']['nome_sped'] = $_POST['user']['nome_sped'];
} else {
	$sped++;
}
if (isset($_POST['user']['cognome_sped']) &&  $_POST['user']['cognome_sped']!="") {
	$_SESSION['user']['cognome_sped'] = $_POST['user']['cognome_sped'];
} else {
	$sped++;
}
if (isset($_POST['user']['indirizzo_sped']) &&  $_POST['user']['indirizzo_sped']!="") {
	$_SESSION['user']['indirizzo_sped'] = $_POST['user']['indirizzo_sped'];
} else {
	$sped++;
}
if (isset($_POST['user']['cap_sped']) &&  $_POST['user']['cap_sped']!="") {
	$_SESSION['user']['cap_sped'] = $_POST['user']['cap_sped'];
} else {
	$sped++;
}
if (isset($_POST['user']['city_sped']) &&  $_POST['user']['city_sped']!="") {
	$_SESSION['user']['city_sped'] = $_POST['user']['city_sped'];
} else {
	$sped++;
}
if (isset($_POST['user']['provincia_sped']) &&  $_POST['user']['provincia_sped']!="" &&  $_POST['user']['provincia_sped']!="--") {
	$_SESSION['user']['provincia_sped'] = $_POST['user']['provincia_sped'];
} else {
	$sped++;
}
//echo "spedizione: ".$sped."<br>";



top();
$vedi_acc= "<div id=\"sfondosez\">";
$tot=0;
$parz=0;
$spSped=0;
$serial = serialize($_SESSION['carrello']);
$i=1;
$art ="";
foreach ($_SESSION['carrello'] as $key => $val) {
	$parz=$val['quantita'] * correggi($val['prezzo']);
	$tot += correggi($parz);
	$sID = $val['sped'];
	if ($spese[$sID] > $spSped) {
		$spSped = $spese[$sID];
	}

	$vedi_acc .= "<div class=benvenuto2>Nome:  ".$val['nome']."</div>";
	$vedi_acc .= "<div class=scheda style=\"font-weight:normal\">&nbsp;&nbsp;Prezzo unitario: ".$val['prezzo'] ."<br/>" ;
	$prezzo = str_replace(",", ".", $val['prezzo']);
	if ($val['taglSiNo'] != 0) {
		foreach ($val['tagle'] as $k => $v) {
			$vedi_acc .=  "&nbsp;&nbsp;Taglia: ".$k." - Q.ta':".$v['tagliaNum']."<br>";
			$art .= "<input type='hidden' name='amount_".$i."' value='".correggi($val['prezzo'])."'>
			<input type='hidden' name='item_name_".$i."' value='".$val['nome']." (tg:".$k.")'>
			<input type='hidden' name='quantity_".$i."' value='".$v['tagliaNum']."'>";
			$i++;
		}
	} else {
		$art .= "<input type='hidden' name='amount_".$i."' value='".$prezzo."'>
			<input type='hidden' name='item_name_".$i."' value='".$val['nome']."'>
			<input type='hidden' name='quantity_".$i."' value='".$val['quantita']."'>";
		$i++;
	}

	//$vedi_acc .= "&nbsp;&nbsp;Qta':  ".$val['quantita'] ."<br/>" ;
	$vedi_acc .= "&nbsp;&nbsp;Spesa parziale: euro ".$parz."<br/>";
	$vedi_acc .= "<div class=clear><div></div></div><br/>";

	//amount_1 - item_name_1 -item_number_1

}
$art .= "<input type='hidden' name='shipping_1' value='".$spSped."'>";

$vedi_acc .=  "<div class=spesatotale>SPESA TOTALE:</b> EURO ".$tot."<br/>";
$vedi_acc .= "SPESE DI SPEDIZIONE:</b> EURO ".$spSped."</div>";
$vedi_acc .= "<p></p>";
$serial2 = serialize($_SESSION['user']);
$dati = array(			'carrello'=>q($serial),
						'totale'  =>q($tot),
						'spediz'  =>q($spSped),
						'pagato'=>'0',
						'stato'=>q('non pagato'),
						'id_user'=>q($serial2));
$id = InsertIntoArray("acquisti", $dati);
$vedi_acc .= "</div>";
$contrsp = ($tot * 2) / 100;
if (correggi($contrsp) < 3.60) { $contrsp = 3.60;   }
$str = "";
$str .= "<div style=\"font-size:12px; width:80%;background:#EAE5CE ; border:1px solid #ff6600;padding:10px;\">";
if ($sped > 0){
	$str .= "
		<b>Indirizzo di spedizione:</b> <br><br>
		Nome: ". $_SESSION['user']['nome']."<br/>
		Cognome: ". $_SESSION['user']['cognome']."<br/>
		Indirizzo E-Mail: ". $_SESSION['user']['email']."<br/>
		Codice Fiscale/Partita Iva ". $_SESSION['user']['codfisc']."<br/>
		Ragione sociale: ". $_SESSION['user']['ragionesociale']."<br/>
		Indirizzo fatturazione: ". $_SESSION['user']['indirizzo_fatturazione']." <br/>
		CAP: ". $_SESSION['user']['cap']." <br/>
		Citta': ". $_SESSION['user']['city']."<br/>
		Provincia: ". $_SESSION['user']['provincia']." <br/>
		Telefono: ". $_SESSION['user']['telefono']."<br/>
		<br/></div>";
} else {
	$str .= "<b>Indirizzo di spedizione:</b> <br><br>
		Nome: ". $_SESSION['user']['nome_sped']."  <br/>
		Cognome: ". $_SESSION['user']['cognome_sped']."  <br/>
		Indirizzo: ". $_SESSION['user']['indirizzo_sped']." <br/>
		CAP: ". $_SESSION['user']['cap_sped']." <br/>
		Citta': ". $_SESSION['user']['city_sped']." <br/>
		Provincia: ". $_SESSION['user']['provincia_sped']." <br/>
		<br/></div>";

}
$str .= "
	<div class=benvenuto2>Seleziona Modalita' di Pagamento:</div>
	<form action='https://".PAYPAL_URL."/cgi-bin/webscr' method='post'>
<input type='hidden' name='cmd' value='_cart'>
<input type='hidden' name='redirect_cmd' value='_xclick'>
<input type='hidden' name='email' value='".$_SESSION['user']['email']."'>"
.$art."
<input type='hidden' name='notify_url' value='http://".MY_URL."/paypal_ipn_handler.php'>
<input type='hidden' name='cancel_return' value='http://".MY_URL."/risultato.php?tipo=paypal&result=false&id=".$id."'>
<input type='hidden' name='return' value='http://".MY_URL."/risultato.php?tipo=paypal&result=true&id=".$id."'>
<input type='hidden' name='rm' value='2'>
<input type='hidden' name='currency_code' value='EUR'>
<input type='hidden' name='business' value='".MY_EMAIL."'>
<input type='hidden' name='first_name' value='".$_SESSION['user']['nome']."'>
<input type='hidden' name='last_name' value='".$_SESSION['user']['cognome']."'>
<input type='hidden' name='address1' value='".$_SESSION['user']['indirizzo_fatturazione']."'>
<input type='hidden' name='city' value='".$_SESSION['user']['city']."'>
<input type='hidden' name='state' value='".$_SESSION['user']['provincia']."'>
<input type='hidden' name='zip' value='".$_SESSION['user']['cap']."'>
<input type='hidden' name='country' value='IT'>
<input type='hidden' name='lc' value='IT'>
<input type='hidden' name='shipping' value='0.00'>
<input type='hidden' name='custom' value='".$id."'>
<input type='hidden' name='upload' value='1'>
<input type='hidden' name='on0' value='Codice Fiscale/Partita Iva'>
<input type='hidden' name='os0' value='".$_SESSION['user']['codfisc']."'>
<input type='hidden' name='on1' value='Telefono'>
<input type='hidden' name='os1' value='". $_SESSION['user']['telefono']."'>
<input name='submit' type='submit' value='Paga con PayPal' />
</form><strong class=\"scheda\">&nbsp;(PayPal - Carta di credito - PostPay)</strong><br/>
<p><form action='risultato.php?tipo=contrassegno&result=true&id=".$id."' method='post'>
<input name='submit' type='submit' value='Paga in contrassegno' />
</form><strong class=\"scheda\">&nbsp;(spese di contrassegno: euro ".$contrsp.")</strong></p>

<p><form action='risultato.php?tipo=bonifico&result=true&id=".$id."' method='post'>
<input name='submit' type='submit' value='Paga con bonifico bancario' />
</form></p> 

<p><form action='risultato.php?tipo=svuota&result=false&id=".$id."' method='post'>
<input name='submit' type='submit' value='Svuota Carrello' /></p>
</div>";

?>
<table width="95%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<div align="center"></div>
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr bordercolor="#ff0000">
		<td>
		<div align="right" class="benvenuto">RIEPILOGO ACQUISTO</div>
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr bordercolor="#ff0000">
		<td>
		<div align="right"></div>
		</td>
		<td>&nbsp;</td>
	</tr>
	<tr bordercolor="#FF0000">
		<td>
		<div align="right" class="categorie"></div>
		</td>
		<td>&nbsp;</td>
	</tr>
</table>
<td valign="top" width="2%" bordercolor="#ff0000" bgcolor="#ffffff"
	class="benvenuto" cellspacing=0 cellpadding=0>&nbsp;
<td width="75%" valign="top" bordercolor="#ff0000" bgcolor="#ffffff"
	class="benvenuto" cellspacing=0 cellpadding=0" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="3" valign="top">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="3" valign="top" class="benvenuto"></td>
	</tr>
	<tr>
		<td colspan="3" valign="top">&nbsp;</td>
	</tr>
	<?
	echo $vedi_acc;
	echo $str;
	?>
	<tr>
		<td colspan="3" valign="top">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="3" valign="top">&nbsp;</td>
	</tr>
</table>

	<?
	foot();
	?>