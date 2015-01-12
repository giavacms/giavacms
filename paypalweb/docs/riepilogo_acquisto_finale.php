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


if(isset($_GET['id'])&& $_GET['id'] =='0'){
	$_SESSION['carrello']="";
	$_SESSION['user']="";
	$_SESSION['last']="";
	header("Location: index.php"); /* Ridireziona il browser */
	exit;
}
top();

$sql = "SELECT A.id, A.data, A.data_spediz,  A.carrello, A.stato, A.tipo, A.pagato, A.spediz, A.totale FROM `acquisti` AS A
WHERE A.id = ". $_GET['id'];
$blocco_opzioni ="";
$res=GetDBResult($sql);
while ( $row = mysql_fetch_array($res)) {
	$id = $row['id'];
	$data = $row['data'];
	$data_spediz = $row['data_spediz'];
	if ($data_spediz == "0000-00-00 00:00:00") { $data_spediz = "N.I.";}
	$stato = $row['stato'];
	$carrello = unserialize($row['carrello']);
	$totale = $row['totale'];
	$sped = $row['spediz'];
	$tipo = $row['tipo'];
	$pagato = $row['pagato'];
	if ($tipo == "contrassegno"){

		//	$contrsp = ($tot * 2) / 100;
		//	if (correggi($contrsp) < 3.60) { $contrsp = 3.60;   }
		//	$sped += $contrsp;

	}
}
$vedi_acc = "";
foreach ($carrello as $key => $val) {
	$vedi_acc .= "<span style=\"color:#000\"><b>Nome Prodotto:</b></span> <span style=\"color:#ff0000;font-weight:bold\">".$val['nome']."</span><br/>";
	$prezzo = str_replace(",", ".", $val['prezzo']);
	if ($val['taglSiNo'] != 0) {
		foreach ($val['tagle'] as $k => $v) {
			$vedi_acc .=  "&nbsp;&nbsp;Taglia: ".$k." - Q.ta':".$v['tagliaNum']."<br>";
		}
	}

	$vedi_acc .= "<span style=\"color:#000\"><b>Prezzo unitario:</b></span> <span style=\"color:#666;font-weight:bold\">" .$val['prezzo'] ."</span><br/>" ;
	$vedi_acc .= "<span style=\"color:#000\"><b>Quantita':</b></span> <span style=\"color:#666;font-weight:bold\">" .$val['quantita'] ."</span><br/><br/>" ;
	$vedi_acc .= "";
}

$vedi_acc .=  "<span style=\"color:#000\"><b>Spese spedizione:</b></span><span style=\"color:#ff0000;font-weight:bold\"> $sped Euro</span>";
if ($tipo == "contrassegno"){
	$vedi_acc .=" ( comprese spese di contrassegno.)";
}
$spTot = $totale + $sped;

$vedi_acc .="<br/>";
$vedi_acc .=  "<span style=\"color:#000\"><b>Spesa totale (comprese spese di spedizione):</b></span> <span style=\"color:#ff0000;font-weight:bold\"> ".$spTot." Euro</span><br/>";
$vedi_acc .=  "<span style=\"color:#000\"><b>Data Acquisto:</b></span> <span style=\"color:#666;font-weight:bold\"> ".$data."</span><br/>";
if ( ($tipo !="contrassegno") && ($tipo !="contrassegno"))$vedi_acc .=  "<span style=\"color:#000\"><b>Pagato:</b></span> <span style=\"color:#666;font-weight:bold\"> ".$totale." Euro</span><br/>";
$vedi_acc .=  "<span style=\"color:#000\"><b>Tipo di Pagamento scelto:</b></span> <span style=\"color:#666;font-weight:bold\"> ".$tipo."</span><br/>";
//$vedi_acc .=  "<span style=\"color:#000\"><b>Stato:</b></span> <span style=\"color:#666;font-weight:bold\"> ".$stato."</span><br/>";
//$vedi_acc .=  "<span style=\"color:#000\"><b>Data Spedizione:</b></span> <span style=\"color:#666;font-weight:bold\"> ".$data_spediz."</span><br/>";
if ($tipo =="bonifico") {

	$sql2 = "SELECT bonifico FROM configurazione WHERE id = 1";

	$res2=GetDBResult($sql2);
	while ( $row2 = mysql_fetch_array($res2)) {
		$dati_bonifico = $row2['bonifico'];
	}
	$vedi_acc .= "<br/><b>Dati per effettuare il bonifico:</b><br><br>"
	.nl2br($dati_bonifico)."";
}
$ind="";
$ind .= "<span style=\"color:#000\"><b>Nome:</b></span> ". $_SESSION['user']['nome']."<br/>";
$ind .= "<span style=\"color:#000\"><b>Cognome:</b></span> ". $_SESSION['user']['cognome']."<br/>";
$ind .= "<span style=\"color:#000\"><b>Indirizzo E-Mail:</b></span> ". $_SESSION['user']['email']."<br/>";
$ind .= "<span style=\"color:#000\"><b>Codice Fiscale/Partita Iva:</b></span>  ". $_SESSION['user']['codfisc']."<br/>";
$ind .= "<span style=\"color:#000\"><b>Ragione sociale:</b></span> ". $_SESSION['user']['ragionesociale']."<br/>";
$ind .= "<span style=\"color:#000\"><b>Indirizzo fatturazione:</b></span> ". $_SESSION['user']['indirizzo_fatturazione']." <br/>";
$ind .= "<span style=\"color:#000\"><b>CAP:</b></span> ". $_SESSION['user']['cap']." <br/>";
$ind .= "<span style=\"color:#000\"><b>Citta':</b></span> ". $_SESSION['user']['city']."<br/>";
$ind .= "<span style=\"color:#000\"><b>Provincia:</b></span> ". $_SESSION['user']['provincia']." <br/>";
$ind .= "<span style=\"color:#000\"><b>Telefono:</b></span> ". $_SESSION['user']['telefono']."<br/>	";


$_SESSION['carrello']="";
$_SESSION['user']="";
$_SESSION['last']="";
?>
<table width="95%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<div align="center"></div>
		</td>
		<td>&nbsp;</td>

	</tr>

	<tr>
		<td width="95%">
		<div align="right" class="benvenuto">RIEPILOGO ACQUISTO</div>
		</td>
	</tr>

</table>
                  
             <br>
             <div id="infomenu" style="text-align:right;float:right">
	<legend><b>Guida all'Acquisto:</b></legend><br/>
	
	<a href="carrello.php">Carrello</a><br>
	<a href="pagamenti.php">Pagamenti</a><br>
	<a href="spese-di-spedizione.php">Spese di spedizione</a><br>
	<a href="condizioni-di-vendita.php">Condizioni di vendita</a><br>
	<a href="privacy.php">Privacy</a><br>
	<a href="contatti.php">Contatti</a><br><br>
	<b>Assistenza Clienti: 0735 86967</b><br>
	</div>



<td valign="top" width="2%" bordercolor="#ff0000" bgcolor="#ffffff"
	cellspacing=0 cellpadding=0>&nbsp;
<td width="75%" valign="top" bordercolor="#ff0000" bgcolor="#ffffff"
	cellspacing=0 cellpadding=0" >
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="3" valign="top">&nbsp;</td>
	</tr>
	<tr>
		<td colspan="3" valign="top">

		<div id=sfondosez1><b>PRODOTTI ACQUISTATI:</b><br />
		<br />
		<?
		echo $vedi_acc;
		?> <br />
		<br />
		<b>Indirizzo per la consegna e fatturazione:</b><br />
		<br />
		<?
		echo $ind;
		?> Riceverete una email con il riassunto della caratteristiche del
		vostro acquisto.<br />
		<br />

		Per qualsiasi problema o dubbio:<br>
		<br />
		<div class=sfondosez1><b>Concessionaria Simonelli Moto<br>
		San Benedetto del Tronto (Ascoli Piceno)<br>
		Tel./Fax: 0735/86967 <br>
		E-mail: info@simonellimoto.it</b></div>
		<br>
		<br>
		</div>
		</div>
		</td>
	
	
	<tr>
		<td colspan=\ "3\" valign=\"top\">&nbsp;</td>
	</tr>
</table>

		<?
		foot();
		?>