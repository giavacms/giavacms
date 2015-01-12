<?
include("config.php");
include("classi.php");
session_start();
session_register("carrello");
session_register("user");
include("config.php");
include("func.inc");
OpenDB();

if ($_GET['tipo']== "paypal") {
	$_SESSION['carrello']="";
	if ($_GET['result']== "true") {
		//segna sul db acquisto tipo paypal usando id
		$where = " id = '".$_GET['id']."'";
		$dati = array('tipo' => q('paypal'));
		UpdateArray("acquisti", $dati, $where);
		header("Location: riepilogo_acquisto_finale.php?id=".$_GET['id']); /* Ridireziona il browser */
		exit;
	}
	if ($_GET['result']== "false") {
		//cancella dal db preacquisto usando id
		$values = array('acquisti' => 'id');
		Delete($values, $_GET['id']);

		header("Location: riepilogo_acquisto_finale.php?id=0"); /* Ridireziona il browser */
		exit;
	}
}
if ($_GET['tipo']== "contrassegno") {
	$oggetto ="Acquisto materiale dal sito Simonelli Moto con pagamento in contrassegno";
	$messaggio = "Acquisto materiale dal sito Simonelli Moto con pagamento in contrassegno\r\n";
	$tot=0;
	$parz=0;
	$spSped=0;
	foreach ($_SESSION['carrello'] as $key => $val) {
		$parz=$val['quantita'] * correggi($val['prezzo']);
		$tot += correggi($parz);
		$sID = $val['sped'];
		if ($spese[$sID] > $spSped) {
			$spSped = $spese[$sID];
		}
		$messaggio .= "Nome:  ".$val['nome']." \n";
		$messaggio .= "Prezzo unitario: ".$val['prezzo'] ." \n" ;
		if ($val['taglSiNo'] != 0) {
			foreach ($val['tagle'] as $k => $v) {
				$messaggio .=  "Taglia: ".$k."- Q.ta':".$v['tagliaNum']."\n";
			}
		}
		$messaggio .= "Spesa parziale: euro ".$parz." \n";
		$messaggio .= " \r\n";
	}
	// calcolo spesa di contrassegno:
	// calcolo il 2%: se < 3.60 = spese sped + 3.60
	// se maggiore lo agg alle spese di spedizione
	$contrsp = ($tot * 2) / 100;
	if (correggi($contrsp) > 3.60) {$spSped += $contrsp;}
	else { $spSped = $spSped + 3.60;   }
	$messaggio .=  "Spesa totale: euro ".$tot." \n";
	$messaggio .=  "Spese spedizione: euro ".$spSped." \n";

	$messaggio .= "Per informazioni: \r\n";
	$messaggio .= "Simonelli Moto S.a.s \nSan Benedetto del Tronto (AP) \nP.I. 01681320444 \nTel. 0735/86967 \ninfo@simonellimoto.it ";
	invia_email($_SESSION['user']['email'], $messaggio, $oggetto);
	invia_email("info@simonellimoto.it", $messaggio, $oggetto);

	$_SESSION['carrello']="";
	//segna sul db acquisto tipo contrassegno usando id
	$where = " id = '".$_GET['id']."'";
	$dati = array('tipo' => q('contrassegno'), 'stato' => q('contrassegno'), 'spediz'  =>q($spSped));
	UpdateArray("acquisti", $dati, $where);
	header("Location: riepilogo_acquisto_finale.php?id=".$_GET['id']); /* Ridireziona il browser */
	exit;
}
if ($_GET['tipo']== "bonifico") {
	$oggetto ="Acquisto materiale dal sito Simonelli Moto con pagamento tramite bonifico bancario";
	$messaggio = "Acquisto materiale dal sito Simonelli Moto tramite bonifico bancario\r\n\r\n";
	$tot=0;
	$parz=0;
	$spSped=0;
	foreach ($_SESSION['carrello'] as $key => $val) {
		$parz=$val['quantita'] * correggi($val['prezzo']);
		$tot += correggi($parz);
		$sID = $val['sped'];
		if ($spese[$sID] > $spSped) {
			$spSped = $spese[$sID];
		}
		$messaggio .= "Nome:  ".$val['nome']." \n";
		$messaggio .= "Prezzo unitario: ".$val['prezzo'] ." \n" ;
		if ($val['taglSiNo'] != 0) {
			foreach ($val['tagle'] as $k => $v) {
				$messaggio .=  "Taglia: ".$k."- Q.ta':".$v['tagliaNum']."\n";
			}
		}
		$messaggio .= "Spesa parziale: euro ".$parz." \n";
		$messaggio .= " \r\n";
	}
	$messaggio .=  "Spesa totale: euro ".$tot." \n";
	$messaggio .=  "Spese di spedizione: euro ".$spSped." \r\n";
	$messaggio .= "Per informazioni: \n";
	$messaggio .= "Simonelli Moto S.a.s \nSan Benedetto del Tronto (AP) \nP.I. 01681320444 \nTel. 0735/86967 \ninfo@simonellimoto.it ";
	invia_email($_SESSION['user']['email'], $messaggio, $oggetto);
	invia_email("info@simonellimoto.it", $messaggio, $oggetto);
	$_SESSION['carrello']="";
	//segna sul db acquisto tipo contrassegno usando id
	$where = " id = '".$_GET['id']."'";
	$dati = array('tipo' => q('bonifico'), 'stato' => q('bonifico'));
	UpdateArray("acquisti", $dati, $where);
	header("Location: riepilogo_acquisto_finale.php?id=".$_GET['id']); /* Ridireziona il browser */
	exit;
}
if ($_GET['tipo']== "svuota") {
	$_SESSION['carrello']="";
	$_SESSION['user']="";
	$_SESSION['last']="";
	//cancella dal db preacquisto usando id
	$values = array('acquisti' => 'id');
	Delete($values, $_GET['id']);
	header("Location: index.php"); /* Ridireziona il browser */
	exit;
}

?>
