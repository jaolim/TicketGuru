# Tiimi Spagetti - TicketGuru


Tiimi: Eetu, Henna, Janne, Jenny, Veera

## Johdanto

Projektin tavoitteena on toteuttaa TicketGuru lipunmyyntijärjestelmä, joka on suunnattu lipputoimistolle tapahtumien lippujen hallintaan ja myyntiin. Järjestelmän ensisijaisia käyttäjiä ovat lipunmyyjät sekä toimistohenkilöstä, jotka voivat luoda uusia tapahtumia, määritellä lipputyyppejä ja tapahtumien kapasiteettia sekä myydä ja tulostaa asiakkaille yksilöidyt liput. Lisäksi järjestelmä tukee ovelta myyntiä: jäljellä olevat liput voidaan tulostaa ja myydä tapahtuman yhteydessä, ja lipuissa oleva koodi mahdollistaa nopean tarkastuksen sekä käytön merkitsemisen ovella.

Projektin valmistuessa järjestelmässä on toiminnallisuudet tapahtumien luomiseen, lipputyyppien ja myyntien hallintaan, lipun tulostamiseen yksilöllisellä QR-koodilla sekä lipun käyttämisen merkitsemiseen. Lisäksi järjestelmä sisältää kirjautumisen, joka varmistaa, että vain valtuutettu henkilökunta voi käyttää lipunmyyntijärjestelmää. Admin käyttäjällä on oikeudet kaikille toiminnoille ja muilla käyttäjillä on lippujen ja tapahtumien hallintaan tarvittavat oikeudet. 

### Toteutus- ja toimintaympäristö

Järjestelmä toteutetaan Spring boot -sovelluskehyksellä, joka tarjoaa REST-rajapinnat lipunmyyntiin, tapahtumatietoihin ja lipuntarkistukseen liittyville toiminnoille. Kehitysympäristössä järjestelmä käyttää joko H2 tai PostgreSQL-tietokantaa. Tuotantoympäristössä järjestelmä käyttää PostgreSQL-tietokantaa tietojen tallentamiseen. Palvelin voidaan ajaa paikallisesti kehitysympäristössä ja valmistunut järjestelmä on CSC:n tarjoamassa Rahti-palvelussa. 

Käyttöliittymä toteutetaan Thymeleaf-mallipohjilla, joista luodaan HTML-sivut palvelimella. Käyttö tapahtuu aluksi työpöytäympäristössä lipunmyyjien työasemilla, ja käyttöliittymä toteutetaan selainpohjaisena ratkaisuna. Tämä mahdollistaa järjestelmän laajentamisen myöhemmin myös mobiililaitteille ja verkkokauppaan.


## Järjestelmän määrittely
### Käyttötapaukset
#### Käyttötapauskaavio

![TicketGuru käyttötapauskaavio](/resources/usercases.drawio.png)


### Käyttäjäroolit

**Lipunmyyjä**
- luo ja muokkaa tapahtumia
- määrittelee lipputyypit ja hinnat
- myy ja tulostaa lippuja
- lukee tarkistettavat liput QR-koodilla
- tarkkailee lippujenmyyntiä

**Lipuntarkastaja**  
- lukee tarkistettavat liput QR-koodilla

**Admin**
- oikeudet luoda, muokata ja poistaa käyttäjiä
- näkee kaikki järjestelmän tiedot 

#### Käyttäjätarinat

#### KT1
Lipunmyyjänä haluan lisätä järjestelmään uuden tapahtuman, jotta voin myydä siihen lippuja.

#### KT2
Lipunmyyjänä haluan muokata olemassa olevan tapahtuman tietoja, jotta voin korjata virheet tai päivittää tiedot.

#### KT3
Lipunmyyjänä haluan poistaa tapahtuman järjestelmästä, jotta virheellisiä tai peruttuja tapahtumia ei voi myydä asiakkaille.

#### KT4
Lipunmyyjänä haluan myydä ja tulostaa lipun asiakkaalle, jotta asiakas saa pääsylipun tapahtumaan.

#### KT5
Lipunmyyjänä haluan tarkastella myynnin tilannetta, jotta voin seurata tapahtuman myyntiä.

#### KT6
Lipuntarkastajana haluan skannata asiakkaan lipussa olevan koodin, jotta voin merkitä sen käytetyksi.

#### KT7
Lipunmyyjänä haluan myydä jäljelle jääneet liput tapahtuman ovella, jotta asiakkaat voivat ostaa lipun vielä sisäänpääsyä varten.

#### KT8
Lipuntarkastajana haluan nähdä virheilmoituksen, jos lippu on jo käytetty. 

#### KT9
Admin-käyttäjänä haluan muokata tai poistaa käyttäjän tai lisätä uuden käyttäjän. 

## Käyttöliittymä

![TicketGuru käyttöliittymäkaavio](/resources/Kayttoliittyma_Ticketguru.drawio.png)

### Lipunmyynti

**Endpoint:** `/sell`

Sell endpoint sisältää useita myyntiin ja lippujen tulostukseen liittyviä toimintoja.

-	Luo myyntitapahtuma joko normaalimyyntiä tai oveltamyyntiä lippujen kera.
-	Hae myyntitapahtuma lippuineen automaattisesti luonnin jälkeen tai manuaalisesti ID:n perusteella.
-	Päivitä Ovimyynnin lippujen tila tarkastusten perusteella.
-	Poista haettu myyntitapahtuma.

Toimintojen lisäksi sivulla näkyy tapahtumien tiedon mukaanluettuna kapasiteetti ja myydyt liput. Uusien lippujen luonti ei onnistu, jos kapasiteetti ei riitä niihin.

### Käyttäjät

**Endpoint:** `/userpage`

-   Lista käyttäjistä
-   Painike käyttäjien lisäykseen
-   Painike käyttäjän poistamiseen
-   Painike käyttäjän tietojen muokkaamiseen

#### Käyttäjän lisääminen

**Endpoint:** `/user/add`

-   Lomake käyttäjän lisäämiseen

#### Käyttäjän muokkaaminen

**Endpoint:** `/user/edit/{id}`

-   Lomake käyttäjän tietojen muokkaamiseen

### Tapahtumat

**Endpoint:** `/eventpage`

-   Lista tapahtumista
-   Painike tapahtumien lisäykseen
-   Painike tapahtuman poistamiseen
-   Painike tapahtuman tietojen muokkaamiseen

#### Tapahtuman lisääminen

**Endpoint:** `/event/add`

-   Lomake tapahtuman lisäämiseen

#### Tapahtuman muokkaaminen

**Endpoint:** `/event/edit/{id}`

-   Lomake tapahtuman tietojen muokkaamiseen

### Liput

**Endpoint:** `/ticketpage`

-   Lista lipuista
-   Painike lippujen lisäykseen
-   Painike lipun poistamiseen
-   Painike lipun tietojen muokkaamiseen

#### Lipun lisääminen

**Endpoint: ** `/ticket/add`

-   Lomake lipun lisäämiseen

#### Lipun muokkaaminen

**Endpoint: ** `/ticket/edit/{id}`

-   Lomake lipun tietojen muokkaamiseen

### Myynnit

**Endpoint: ** `/salespage`

-   Lista myynneistä
-   Painike myyntien lisäykseen
-   Painike myynnin poistamiseen
-   Painike myynnin tietojen muokkaamiseen

#### Myynnin lisääminen

**Endpoint: ** `/sales/add`

-   Lomake myynnin lisäämiseen

#### Myynnin muokkaaminen

**Endpoint: ** `/sales/edit/{id}`

-   Lomake myynnin tietojen muokkaamiseen

### Maksut

**Endpoint: ** `/costpage`

-   Lista maksuista
-   Painike maksujen lisäykseen
-   Painike maksun poistamiseen
-   Painike maksun tietojen muokkaamiseen

#### Maksun lisääminen

**Endpoint: ** `/cost/add`

-   Lomake maksun lisäämiseen

#### Maksun muokkaaminen

**Endpoint: ** `/cost/edit/{id}`

-   Lomake maksun tietojen muokkaamiseen

### Lipputyypit

**Endpoint: ** `/tickettypepage`

-   Lista lipputyypeistä
-   Painike lipputyypin lisäykseen
-   Painike lipputyypin poistamiseen
-   Painike lipputyypin tietojen muokkaamiseen

#### Lipputyypin lisääminen

**Endpoint: ** `/tickettype/add`

-   Lomake lipputyypin lisäämiseen

#### Lipputyypin muokkaaminen

**Endpoint: ** `/tickettype/edit/{id}`

-   Lomake lipputyypin tietojen muokkaamiseen

### Tapahtumapaikat

**Endpoint: ** `/venuepage`

-   Lista tapahtumapaikoista
-   Painike tapahtumapaikan lisäykseen
-   Painike tapahtumapaikan poistamiseen
-   Painike tapahtumapaikan tietojen muokkaamiseen

#### Tapahtumapaikan lisääminen

**Endpoint: ** `/venue/add`

-   Lomake tapahtumapaikan lisäämiseen

#### Tapahtumapaikan muokkaaminen

**Endpoint: ** `/venue/edit/{id}`

-   Lomake tapahtumapaikan tietojen muokkaamiseen

## Tietokanta

### Relaatiomalli

![Tietokannan relaatiomalli](/resources/DB_TicketGuru.png)

## Tekninen kuvaus

TicketGuru-lipunmyyntijärjestelmä on toteutettu Spring Boot -sovelluskehyksellä ja se suoritetaan Rahti-palvelussa. 

Järjestelmä sisältää Thymeleafilla toteutetun käyttöliittymän, jota käytetään selaimen kautta, jolloin erillisiä ohjelma-asennuksia ei tarvita. Lipunmyyjät ja toimistotyöntekijät voivat käyttää kaikkia lipunmyyntiin tarvittavia toimintoja selaimen käyttöliittymän kautta. 

Järjestelmä käyttää PostgreSQL-tietokantaa, jota suoritetaan palvelinympäristössä. Tietokantaan tallennetaan kaikki tietokannan määrittelyssä mainitut tiedot. 

### REST päätepisteet (endpoints)

[REST API -päätepisteet](rest.md)

### Autentikointi

Autentikointi on toteutettu Spring Securityn tarjoamalla HTTP Basic Authentication -ratkaisulla. Jokaisella käyttäjällä on oma käyttäjätunnus ja salasana järjestelmään. Ilman käyttäjätunnusta järjestelmän tietoja ei näe. 

## Testaus

Projektin oikea toiminta on varmistettu useilla testausmenetelmillä:
- **JUnit-yksikkötestit**: Testataan yksittäisten luokkien ja metodien toiminta.
- **Integraatiotestit**: Varmistetaan eri komponenttien ja tietokantayhteyksien yhteensopivuus.
- **End-to-end (E2E) testit**: Tarkastetaan koko sovelluksen toiminnallisuus front-endin ja back-endin välillä.
- **Playwright selain testit**: Web-käyttöliittymän kautta testataan login, navigointi ja CRUD toiminnot.

Tarkemmat testaustiedot: [katso dokumentti](testplan.md)

Tällä hetkellä ei ole tunnettuja ongelmia.

## Asennustiedot

### Kehitysympäristön asennus

Vaatimukset kehitysympäristölle: 

- Java 17
- Maven 3.9.11
- Git

Projekti sijaitsee Githubissa ja se voidaan kloonata kehitysympäristöön. Git-repositorion osoite on https://github.com/jaolim/TicketGuru.git

Ympäristömuuttujien määrittely on kuvattu alempana omassa [luvussaan](#ympäristömuuttujat). Ympäristömuuttujien avulla määritellään käytettävä tietokanta, jotta kehityksen aikana voidaan käyttää toista tietokantaa kuin mitä tuotannossa käytetään. 

Kehitysympäristössä voidaan käyttää H2-tietokantaa, joka ei vaadi erillisiä asennuksia. Sovelluksen käynnistyksen yhteydessä tietokantaan luodaan testauksessa tarvittavat tiedot tietokantaan: käyttäjät ja esimerkkidata kaikista tietokannan tiedoista. 

### Järjestelmän asennus tuotantoympäristöön

TicketGuru on asennettu CSC:n Rahti-palveluun ja se on rakennettu Dockerfile-tiedoston perusteella konttijulkaisuna. Rahti-palveluun tulee määritellä uusi PostgreSQL-tietokanta ja se tehdään sivuston käyttöliittymän kautta. Järjestelmä käyttää Rahti-palvelussa application-rahti.properties-ympäristömuuttujia, jotka tulee määrittää palveluun. Projekti voidaan tuoda palveluun Git-repositorion linkin avulla. 

Järjestelmän ensimmäisen käynnistyksen yhteydessä PostgreSQL-tietokantaan luodaan pohjatiedot, joka sisältää käyttäjät ja esimerkkidatan kaikista tietokannan tiedoista. 

## Ympäristömuuttujat

Projekti sisältää useita ympäristömuuttujien kautta määriteltäviä attribuutteja.

**Spring Boot projektin muuttujat:**

Lokaalisti muuttujat tallennetaan .env tiedostoon samaan hakemistoon pom.xml kanssa.

Rahdissa ne määritellään erikseen.

.env tiedot ilman salaisuuksia (salaisuudet Moodle palautuksen mukana):

```
#käytetty application.properties
ENV_MODE= #dev tai prod tai rahti, dev käyttää h2 tietokantaa, prod paikallista postgresql:ää ja rahti rahdin postgresql:ää
#postgres tiedot, jos prod on käytössä
DB_USER=
DB_PASSWORD=
DB_URL=
```

Rahdissa salaisuudet:
```
ENV_MODE
DB_NAME
DB_USER
DB_PASSWORD
DDL_TYPE #suositeltu update, create valinnalla voi pakottaa tietokantamuutokset, mutta tämä nollaa tietokannan
```

**Playwright projektin muuttujat**

Playwright testit ovat joko ajettavissa lokaalisti tai GitHub Actionsin kautta.

Lokaalisti käytettään .env tiedostoa playwright_tests hakemistossa.

.env:

```
#Julkaisun osoite
URL=

#user ja admin tiedot
USERNAME1=
PASSWORD1=
USERNAME2=
PASSWORD2=
```

Githubissa testit ovat ajettevissa manuaalisesti käynnistettävänä workflowna ja siellä ympäristömuuttujat määritellään GitHub repository secretteinä kohdassa:

Settings -> Secrets and variables -> Actions -> New repository secret


## Käynnistys- ja käyttöohje

Sovellusta käytetään internetselaimella osoitteesta:
https://ticket-guru-ticketguru-postgres.2.rahtiapp.fi/

### Kirjautuminen

Etusivulla on linkki kirjautumiseen, johon käytetään henkilökohtaisia tai admin-tunnuksia. Admin-käyttäjä voi luoda sivustolle kirjautuneena uusia käyttäjiä tarpeen mukaan. 

### Tapahtuman luominen

Uuden tapahtuman luominen sivustolle vaatii sen, että tapahtumalle on luotu järjestelmään tapahtumapaikka (venue). Tämän jälkeen tapahtuma voidaan luoda Events-sivun Add new event -painikkeella. Tämän jälkeen tapahtumalle voidaan luoda lipputyyppejä (ticket types), ellei niitä ole jo olemassa. Seuraavaksi lipuille luodaan aihiot (cost), jotka sisältävät lipputyypin sekä sen hinnan. Näiden vaiheiden jälkeen lippu voidaan myydä Sell tickets -sivulla. 

### Lipun myyminen

Lippu myydään Sell tickets -sivulla. Quantity-kohdassa valitaan kuinka monta lippua halutaan myydä, jonka jälkeen valitaan Add ja liput lisätään myyntiin. Kun kaikki halutut liput on valittu, niin valitaan Create sale, jonka jälkeen uudet liput luodaan järjestelmään ja ne tulostuvat näytölle. 

### Lipun tarkistaminen

Liput tarkistetaan Ticket reader -sivulla. Lippu luetaan kameralla ja tässä yhteydessä se merkitään järjestelmään käytetyksi. Järjestelmä ilmoittaa, jos lippu on jo käytetty. 
