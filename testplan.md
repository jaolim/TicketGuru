# Testausprosessit -dokumentaatio

Tässä dokumentissa käydään läpi projektin testausprosessit. Testauksen tavoitteena on varmistaa, että ohjelmsito toimii suunnitellusti. Eli virheet havaitaan ja järjestelmäne eri komponentit toimivat yhdessä. Testaus pitää sisällään yksikkötestejä, integraatiotestejä sekä end-2-end-testejä. 

1. [Yksikkötestit](#yksikkötestit)
2. [Integraatiotestit](#integraatiotestit)
3. [End-2-end-testit](#end-to-end-testit)
4. [Playwright testit](#playwright-testit)

## Yksikkötestit
Yksikkötestien tarkoituksena on varmistaa että yksittäiset koodikomponentit toimivat oikein. Testataan luokan tai metodin toimintaa erillään muusta järjestelmästä. 

### AppUser-luokka
- `shouldCreateAppUserWithCorrectFields` tarkistaa, että AppUserin kentät alustavat oikein konstruktiolla.
- `shouldUpdatePasswordHash` tarkistaa, että salasanan hash voidaan päivittää.
- `shouldUpdateFirstnameAndLastname` testaa, että etu- ja sukunimen päivityksen.
- `shouldUpdateUserRole` testaa käyttäjäroolin päivityksen.
- `shouldReturnFullName`testaa, että yhdistetty etu- ja sukunimi toimii oikein. 

### Cost-luokka
- `shouldCreateCostWithCorrectValues()` tarkistaa, että `Cost`-oliolle on annettu hinta, `TicketType` ja `Event` tallentuvat oikein.
- `shouldUpdatePrice()` varmistaa, että `setPrice()`muuttaa hinnan oikein.
- `shouldThrowExceptionForNegativePrice()`tarkistaa, että negatiivinen hinta aiheuttaa poikkeuksen.

### Ticket-luokka
- `shouldCreateTicketWithCostOnly` testaa, että Ticket-olio voidaan tallentaa pelkän Costin kanssa ja sen redeemed-kenttä on false.
- `shouldCreateTicketWithCostAndSale` testaa, että Ticket-olio voidaan luoda sekä Costilla sekä Salella ja redeemed-kenttä false.
- `shouldSetAndGetFields` testaa, että Ticketin getterit ja setterit toimivat oikein kaikille kentille.
- `toStringShouldContainCostAndRedeemed` testaa, että Ticketin `ToString()`-metodi palauttaa merkkijonon, joka sisältää Costa ja redeemed-arvon. 

### TicketType-luokka
- `houldCreateTicketTypeWithName` testaa, että TicketType voidaan luoda pelkän nimen kanssa ja nimi tallentuu oikein.
- `shouldCreateTicketTypeWithNameAndNote` testaa, että TicketType voidaan luoda nimen ja huomautuksen kanssa ja ne tallentuvat oikein.
- `shouldSetAndGetFields` testaa, että getterit ja setterit toimivat oikein.
- `toStringShouldReturnName`testaa, että TicketTypen `ToString()` palauttaa TicketTypen nimen.

## Integraatiotestit
Sama periaate kuin yksikkötesteissä, mutta testaa useamman komponentin yhteistyötä. `@SpringBootTest`lataa koko sovelluskontekstin, jonka avulla testattavat objektit kommunikoivat tietokannan kanssa ja sovelluksen eri osien välinen logiikka testautuu.

### Cost-luokan CRUD-toiminnot
- `shouldCreateNewCost()` testaa, että uusi hinta tallentuu tietokantaan.
- `shouldReturnCostById()` testaa, että tietokannasta haetaan oikea hinta ID:n perusteella.
- `shouldDeleteCost()` testaa, että hinnan voi poistaa ja sitä ei löydä enää tietokannasta.
- `shouldUpdatePrice()` testaa, että hinnan päivitys onnsituu ja tallentuu tietokantaan.

### Event-luokan CRUD-toiminnot
- `shouldCreateNewEvent()` testaa, että uusi eventti tallentuu tietokantaan.
- `shouldReturnEventById()` testaa, että tietokannasta haetaan oikea eventti ID:llä.
- `shouldUpdateEventVenue` testaa, että eventin paikkaa voidaan muuttaa.
- `shouldDeleteEvent()` testaa, että eventti voidaan poistaa.
- `shouldNotFindNonExistingEvent` testaa, että tietokannasta ei löydetä eventtejä, joita ei ole.

### Ticket-luokan CRUD-toiminnot
- `shouldCreateTicketWithCostOnly` testaa, että Ticket voidaan tallentaa pelkän Costin kanssa ja sen redeemed-arvo on false.
- `shouldCreateTicketWithCostAndSale` testaa, että Ticket voidaan tallentaa Costin ja Salen kanssa ja linkit tallentuvat oikein.
- `houldUpdateRedeemed` testaa, että lipun redeemed päivitys tallentuu tietokantaan.
- `shouldDeleteTicket` testaa, että ticket voidaan poistaa onnistuneesti tietokannasta.

### Sale-luokan CRUD-toiminnot
- `shouldCreateSale` testaa, että uusi Sale tallentuu onnistuneesti tietokantaan.
- `shouldReturnSaleById` testaa, että tallennettu Sale voidaan hakea ID:n perusteella.
- `shouldUpdateSalePrice` testaa, että Salen hinnan päivitys tallentuu tietokantaan.
- `shouldDeleteSale` testaa, että Sale voidaan poistaa tietokannasta onnistuneesti. 

## End-2-end-testit

End-to-end -testit varmistavat koko järjestelmän toiminnan käyttäjän näkökulmasta. Näin varmistetaan, että autentikointi, controllerit, palvelukerros ja tietokanta toimivat kokonaisuutena.

### SellTicket (end-2-end)

`sellTicket_endToEndFlow` testaa lipun myyntiprosessin alusta loppuun.
- Hakee tapahtumat `GET /events` -kutsulla ja varmistaa palvelimen vasteen.
- Hakee olemassa olevan myyntitapahtuman `GET /sales` ja poimii `saleid`-arvon.
- Luo uuden lipun `POST /tickets` käyttäen olemassa olevaa `saleid`- ja `costid`-arvoa.
- Varmistaa `GET /tickets` -kutsulla, että luotu lippu näkyy järjestelmän listauksessa.

## Playwright-testit

Playwright testit toteuttu TypeScriptiä käyttävänä NPM Playwright projektina ja ne ovat ajettavissa joko lokaalisti tai GitHubissa manuaalisesti käynnistettävänä Workflowna.

Testit on jaettu kahteen tiedostoon, joista toisessa testataan login toimintaja ja toisessa CRUD toimintoja.

### Testit:

Playwright testit käyttävät h2 tietokantaa käyttävää [Rahdin kehitysversiota](https://ticketguru-ticketguru-postgres.2.rahtiapp.fi/).

CRUD toiminnoissa luotetaan testattavan sivun itse generoiman datan löytyvän oikeassa muodossa, mutta testien generoima data validoidaan ja tarvittaessa poistetaan, jos se löytyy ennen testien ajoa.

Tällöin keskeytynyt testioja (joka voi johtua vaikka yhteysongelmasta) ei pilaa sivuston dataa tuleville testiajoille.

#### ticketguru_login.spec.ts

-	**TicketGuru responds**: Sivu latautuu
-	**Unauthenticated right elements shown**: Tunnistautuman käyttäjä näkee vain Login elementin
-	**User right elements shown**: `USER` roolilla tunnistautunut käyttäjä ei näe linkkiä käyttäjienhallintaan
-	**Admin right elements shown**: `ADMIN` roolin käyttäjä näkee kaikki elementit

#### ticketguru_use.spec.ts

*Ennen testejä kirjaudutaan admininä sisään ja testit ajetaan samaa sessiota käyttäen*

-	**Venues CRUD**: Venue listaus, lisääminen, editointi ja deletointi toimii
-	**Events CRUD**: Event listaus, lisääminen, editointi ja deletointi toimii
-	**TicketTypes CRUD**: Ticket type listaus, lisääminen, editointi ja deletointi toimii
-	**Costs CRUD**: Cost type listaus, lisääminen, editointi ja deletointi toimii
-	**Sales and tickets CRUD**:
	-	Salen luonti toimii
	-	Lippujen luonti toimii ja päivittää salea
	-	Salen tuhoaminen poistaa myös siihen liittyvät liput

### Playwright lähde:

Pohjana käytetty tiimin jäsenen [Playwright testaus seminaarityötä](https://github.com/jaolim/seminaari_testaus) kurssille Ohjelmistokehityksen Teknologioita.

Itse testit on kirjoitettu tällä kurssilla, mutta apufunktiot ja GitHub Workflow määrittely on tehty toisen kurssin puolella.




