import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by fiorenzo on 03/08/15.
 */
public class TestCrawler
{
   static WebClient webClient;
   String[] chiavi =

            {
                     "45-com", "albachiara", "antares", "bacio-dellonda", "bagni-007", "bagni-camiscioni",
                     "bagni-da-federico", "bagni-fausto", "bagni-fortuna", "bagni-giacomino", "bagni-holiday",
                     "bagni-hotel-beaurivage", "bagni-hotel-excelsior", "bagni-hotel-haiti", "bagni-hotel-marconi",
                     "bagni-hotel-nettuno", "bagni-hotel-solarium", "bagni-hotel-soraya", "bagni-hotel-sunrise",
                     "bagni-michelangelo-e-persico", "bagni-nedio", "bagni-nik", "bagni-pensione-doria",
                     "bagni-pensione-olimpo", "bagni-zappa", "basciu", "bijou", "blumarine", "boobies-bar", "bossanova",
                     "brasil", "calypso", "capitan-mario", "casablanca", "cavalluccio-marino", "chalet-alex",
                     "chalet-americo", "chalet-claudia", "chalet-degli-angeli", "chalet-il-pinguino", "chalet-jose",
                     "chalet-la-gioconda", "chalet-letoile", "chalet-malibu", "chalet-stella", "chalet-vitadamare",
                     "ciao", "club-23", "da-andrea", "da-luigi", "da-pietro", "da-vincenzo", "da-vincenzo-1",
                     "diamante", "golden-beach", "happy-sun-beach", "iguana", "il-farfallone", "il-gambero",
                     "il-monello", "il-pescatore", "il-pirata", "il-timone", "il-tritone", "imperial-beach", "kontiky",
                     "la-bussola", "la-conchiglia", "la-croisette", "la-lancette", "la-medusa", "la-promenade",
                     "la-scogliera", "la-serenella", "la-siesta", "la-tartana", "la-tellina", "le-anfore",
                     "lega-navale-italiana", "lido-azzurro", "lido-dei-carabinieri", "lido-dellesercito",
                     "lido-il-pescatore", "lido-sabbiadoro", "lo-zodiaco", "lorca", "marina-di-nico", "miramare",
                     "oltremare", "pagoda-beach", "paradise-beach", "piccolo-lido", "poker", "polizia-di-stato",
                     "renos", "rivabella", "rivamare", "sabbia", "sapore-di-mare", "sea-club", "spiaggia-75",
                     "spiaggia-doro", "stella-marina", "sud-est", "tourist-residence", "tre-caravelle", "vela-club",
                     "voglia-di-mare"

            };
   String[] altre = { "chalet", "classifica"
            , "vota", "come-si-fa", "cosa-si-vince", "regolamento", "privacy", "cookie-policy" };

   @BeforeClass
   public static void before()
   {

   }

   @Test
   public void test() throws IOException
   {
      //         webClient.setJavaScriptEnabled(true);
      //      for (String key : chiavi)
      //      {
      //         webClient = new WebClient(BrowserVersion.CHROME);
      //         String pageName = "http://votalatua.estate/#!/chalet_id/" + key;
      //         HtmlPage page = webClient.getPage(pageName);
      //         webClient.waitForBackgroundJavaScriptStartingBefore(3000);
      //         FileUtils.writeStringToFile(new File("src/test/resources/cache", key + ".html"), page.asXml());
      ////         System.out.println(page.asXml());
      ////         webClient.close();
      //      }
      //      if (true)
      //         return;
      for (String key : altre)
      {
         System.out.println(key);
         webClient = new WebClient(BrowserVersion.CHROME);
         String pageName = "http://votalatua.estate/#!/" + key;
         HtmlPage page = webClient.getPage(pageName);
         webClient.waitForBackgroundJavaScriptStartingBefore(3000);
         FileUtils.writeStringToFile(new File("src/test/resources/cache", key + ".html"), page.asXml());
      }

   }

   @AfterClass
   public static void after()
   {
      webClient.close();
   }
}
