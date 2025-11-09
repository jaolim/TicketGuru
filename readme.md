# Tiimi Spagetti - TicketGuru


Tiimi: Eetu, Henna, Janne, Jenny, Veera

## Johdanto

Projektin tavoitteena on toteuttaa TicketGuru lipunmyyntijärjestelmä, joka on suunnattu lipputoimistolle lippujen hallintaan ja myyntiin. Järjestelmän ensisijainen käyttäjä on lipunmyyjä, joka voi myyntipisteessä myydä lippuja ja tulostaa ne asiakkaille. Toimiston täytyy voida luoda järjestelmään tapahtumat, joihin lippuja myydään. Lisäksi järjestelmä tukee ovelta myyntiä: jäljellä olevat liput voidaan tulostaa ja myydä tapahtuman yhteydessä, ja lipuissa oleva koodi mahdollistaa nopean tarkastuksen sekä käytön merkitsemisen ovella.

### (Alustava) Toteutus- ja toimintaympäristö

Järjestelmä toteutetaan modernilla palvelinteknologialla hyödyntäen relaatiotietokantaa tapahtumien, lippujen ja myyntitapahtumien tallentamiseen. Käyttö tapahtuu aluksi desktop-ympäristössä lipunmyyjien työasemilla, ja käyttöliittymä toteutetaan selainpohjaisena ratkaisuna. Tämä mahdollistaa järjestelmän laajentamisen myöhemmin myös mobiililaitteille ja verkkokauppaan.


## Järjestelmän määrittely
### Käyttötapaukset
#### Käyttötapauskaavio

![TicketGuru käyttötapauskaavio](/resources/Kayttotapauskaavio_Ticketguru.drawio.png)


### Käyttäjäroolit
- Lipunmyyjä - vastaa tapahtuman määrittelyn järjestelmässä, myy lippuja sekä tarkkailee lippujenmyyntiä.
- Lipuntarkastaja  - lukee ostetut liput ja voi myydä jäljelle jääneitä lippuja ovilla.

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
Lipuntarkastajana haluan myydä jäljelle jääneet liput tapahtuman ovella, jotta asiakkaat voivat ostaa lipun vielä sisäänpääsyä varten.

## Käyttöliittymä

![TicketGuru käyttöliittymäkaavio](/resources/Kayttoliittyma_Ticketguru.drawio.png)

## Tietokanta

### Relaatiomalli

![Tietokannan relaatiomalli](/resources/DB_TicketGuru.png)

#### Selvitys

Pakolliset kentät merkitty tähdellä (*)

**Ticket**: Yksittäinen lippu tiettyyn tapahtumaan
- PK: ticket_id
- FK: sale_id *
- FK: cost_id *
- time
- redeemed: onko lippu vielä voimassa
- price: snapshot hinnasta **Price** taulusta

**Cost**: Tietyn tapahtuman lippuhinnnat
- PK: cost_id *
- FK: event_id *
- FK: type_id *
- price

**Type**: Lipun tyyppi (Aikuinen, Lapsi etc...)
- PK: type_id *
- name * : lipputyypin nimi
- note: mahdollinen lisätieto

**Sale**: Myyntitapahtuma
- PK: sale_id *
- FK: user_id *
- price *
- time *

**Event**: Tietty tapahtuma
- PK: event_id *
- FK: venue_id *
- name *
- date *

**User**: Viittaa lipputoimiston henkilökuntaan (Laajennettavissa sisältämään muita käyttäjätyyppeja)
- PK: user_id *
- username *
- password *
- firstname *
- lastname *

#### Mahdolliset laajennukset

**Venue**: Tietty tapahtumapaikka
- PK: venue_id
- FK: address_id
- name
- note

**Address**: Tapahtumapaikan osoite
- PK: address_id
- FK: postalcode
- street
- number
- note

**Postalcode(PK: postalcode, FK: city_id)**: Postiosoitteet

**City(PK: city_id, name, note)**: Kaupungit

## Tekninen kuvaus

### REST päätepisteet (endpoints)

Lisätietoa REST API -päätepisteistä: [katso dokumentaatio](rest.md)

### Autentikointi

#### Basic Auth

Teknisessä kuvauksessa esitetään järjestelmän toteutuksen suunnittelussa tehdyt tekniset
ratkaisut, esim.

-   Missä mikäkin järjestelmän komponentti ajetaan (tietokone, palvelinohjelma)
    ja komponenttien väliset yhteydet (vaikkapa tähän tyyliin:
    https://security.ufl.edu/it-workers/risk-assessment/creating-an-information-systemdata-flow-diagram/)
-   Palvelintoteutuksen yleiskuvaus: teknologiat, deployment-ratkaisut yms.
-   Keskeisten rajapintojen kuvaukset, esimerkit REST-rajapinta. Tarvittaessa voidaan rajapinnan käyttöä täsmentää
    UML-sekvenssikaavioilla.
-   Toteutuksen yleisiä ratkaisuja, esim. turvallisuus.

Tämän lisäksi

-   ohjelmakoodin tulee olla kommentoitua
-   luokkien, metodien ja muuttujien tulee olla kuvaavasti nimettyjä ja noudattaa
    johdonmukaisia nimeämiskäytäntöjä
-   ohjelmiston pitää olla organisoitu komponentteihin niin, että turhalta toistolta
    vältytään
	
## Käyttöliittymä

### Lipunmyynti

**Endpoint: ** `/sell`

-	Kentät basic auth tunnistaumiselle ja base url:lle(oletuksena main julkaisu)
-	Painike tapahtumien hakuun ja listaukseen
-	Kentät ja painike lippujen lisäykseen
-	Painike myyntitapahtuman lisäämiseen
-	Painike ja kenttä tiettyyn myyntiin liittyvien lippujen hakuun ja QR koodien generoimiseen

## Testaus

Tarkemmat testaustiedot: [katso dokumentti](testplan.md)

Tässä kohdin selvitetään, miten ohjelmiston oikea toiminta varmistetaan
testaamalla projektin aikana: millaisia testauksia tehdään ja missä vaiheessa.
Testauksen tarkemmat sisällöt ja testisuoritusten tulosten raportit kirjataan
erillisiin dokumentteihin.

Tänne kirjataan myös lopuksi järjestelmän tunnetut ongelmat, joita ei ole korjattu.

## Asennustiedot

Järjestelmän asennus on syytä dokumentoida kahdesta näkökulmasta:

-   järjestelmän kehitysympäristö: miten järjestelmän kehitysympäristön saisi
    rakennettua johonkin toiseen koneeseen

-   järjestelmän asentaminen tuotantoympäristöön: miten järjestelmän saisi
    asennettua johonkin uuteen ympäristöön.

Asennusohjeesta tulisi ainakin käydä ilmi, miten käytettävä tietokanta ja
käyttäjät tulee ohjelmistoa asentaessa määritellä (käytettävä tietokanta,
käyttäjätunnus, salasana, tietokannan luonti yms.).

## Käynnistys- ja käyttöohje

Tyypillisesti tässä riittää kertoa ohjelman käynnistykseen tarvittava URL sekä
mahdolliset kirjautumiseen tarvittavat tunnukset. Jos järjestelmän
käynnistämiseen tai käyttöön liittyy joitain muita toimenpiteitä tai toimintajärjestykseen liittyviä asioita, nekin kerrotaan tässä yhteydessä.

Usko tai älä, tulet tarvitsemaan tätä itsekin, kun tauon jälkeen palaat
järjestelmän pariin !