# Tiimi Spagetti - TicketGuru


Tiimi: Eetu, Henna, Janne, Jenny, Veera

## Johdanto

Projektin tavoitteena on toteuttaa TicketGuru lipunmyyntijärjestelmä, joka on suunnattu lipputoimistolle tapahtumien lippujen hallintaan ja myyntiin. Järjestelmän ensisijaisia käyttäjiä ovat lipunmyyjät sekä toimistohenkilöstä, jotka voivat luoda uusia tapahtumia, määritellä lipputyyppejä ja tapahtumien kapasiteettia sekä myydä ja tulostaa asiakkaille yksilöidyt liput. Lisäksi järjestelmä tukee ovelta myyntiä: jäljellä olevat liput voidaan tulostaa ja myydä tapahtuman yhteydessä, ja lipuissa oleva koodi mahdollistaa nopean tarkastuksen sekä käytön merkitsemisen ovella.

Projektin valmistuessa järjestelmässä on toiminnallisuudet tapahtumien luomiseen, lipputyyppien ja myyntien hallintaan, lipun tulostamiseen yksilöllisellä QR-koodilla sekä lipun käyttämisen merkitsemiseen. Lisäksi järjestelmä sisältää kirjautumisen, joka varmistaa, että vain valtuutettu henkilökunta voi käyttää lipunmyyntijärjestelmää. Admin käyttäjällä on oikeudet kaikille toiminnoille ja muilla käyttäjillä on lippujen ja tapahtumien hallintaan tarvittavat oikeudet. 

### Toteutus- ja toimintaympäristö

Järjestelmä toteutetaan Spring boot -sovelluskehyksellä, joka tarjoaa REST-rajapinnat lipunmyyntiin, tapahtumatietoihin ja lipuntarkistukseen liittyville toiminnoille. Järjestelmä käyttää PostgreSQL-tietokantaa tietojen tallentamiseen. Palvelin voidaan ajaa paikallisesti kehitysympäristössä ja valmistuessaan se siirretään tuotantoon Rahti-palveluun. 

Käyttöliittymä toteutetaan Thymeleaf-mallipohjilla, joista luodaan HTML-sivut palvelimella. Käyttö tapahtuu aluksi työpöytäympäristössä lipunmyyjien työasemilla, ja käyttöliittymä toteutetaan selainpohjaisena ratkaisuna. Tämä mahdollistaa järjestelmän laajentamisen myöhemmin myös mobiililaitteille ja verkkokauppaan.


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

Järjestelmän asennus on syytä dokumentoida kahdesta näkökulmasta:

-   järjestelmän kehitysympäristö: miten järjestelmän kehitysympäristön saisi
    rakennettua johonkin toiseen koneeseen

-   järjestelmän asentaminen tuotantoympäristöön: miten järjestelmän saisi
    asennettua johonkin uuteen ympäristöön.

Asennusohjeesta tulisi ainakin käydä ilmi, miten käytettävä tietokanta ja
käyttäjät tulee ohjelmistoa asentaessa määritellä (käytettävä tietokanta,
käyttäjätunnus, salasana, tietokannan luonti yms.).

## Käynnistys- ja käyttöohje

Linkki ohjelmaan:
https://ticket-guru-ticketguru-postgres.2.rahtiapp.fi/

Tunnukset sivustolle tulee moodlen palautuksen yhteydessä.