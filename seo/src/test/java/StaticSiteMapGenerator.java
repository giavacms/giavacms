/**
 * Created by fiorenzo on 03/08/15.
 */
public class StaticSiteMapGenerator
{
   static String[] chiavi =

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
   static String[] altre = { "chalet", "classifica"
            , "vota", "come-si-fa", "cosa-si-vince", "regolamento", "privacy", "cookie-policy" };

   public static void main(String[] args)
   {
      for (String key : chiavi)
      {
         System.out.println("<url> "
                  + "<loc>http://votalatua.estate/#!/chalet_id/" + key + "</loc>" +
                  "</url>");
      }

      for (String key : altre)
      {
         System.out.println("<url> "
                  + "<loc>http://votalatua.estate/#!/" + key + "</loc>" +
                  "</url>");
      }

   }
}
