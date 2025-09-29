# REST API -dokumentaatio

Tämä dokumentti kuvaa palvelun tarjoamat REST API -päätepisteet.

**Dev base url:** `http://locahost:8080`

## Events

| Metodi | URL | Kuvaus |
| -------- | ------- | --------- |
| GET | {baseurl}/events | Listaa kaikki tapahtumat |
| GET | {baseurl}/events/{id} | Palauttaa tietyn tapahtuman|
| POST | {baseurl}/events | Luo uuden tapahtuman |
| PUT | {baseurl}/events/{id} | Muokkaa olemassa olevaa tapahtumaa |
| DELETE | {baseurl}/events/{id} | Poistaa tietyn tapahtuman |



### Get Events

**URL:** `{baseurl}/events`

**Metodi:** `GET`

**Esimerkkituloste:**

```
[
    {
        "eventid": 1,
        "name": "Event1",
        "venue": "Venue1",
        "start": "2025-09-22T17:46:44.847199"
    },
    {
        "eventid": 2,
        "name": "Event2",
        "venue": "Venue2",
        "start": "1999-01-31T20:00:00"
    }
]
```

### Post Event

**URL:** `{baseurl}/events`

**Metodi:** `POST`

**Esimerkkipyyntö:**

```
{
    "name": "event name",
    "venue": "venue name",
    "start": "YYYY-MM-DDTHH:mm"
}
```

### Put Event

**URL:** `{baseurl}/events/{id}`

**Metodi:** `PUT`

**Esimerkkipyyntö:**

```
{
    "name": "event name",
    "venue": "edited venue",
    "start": "YYYY-MM-DDTHH:mm"
}
```

### Delete Event

**URL:** `{baseurl}/events/{id}`

**Metodi:** `DELETE`

**Path variable:** 
id = poistettavan tapahtuman tunniste (Long)

## Ticket

**Dev base url:** `http://locahost:8080`

| Metodi | URL | Kuvaus |
| -------- | ------- | --------- |
| GET | {baseurl}/tickets | Listaa kaikki liput |
| GET | {baseurl}/tickets/{id} | Palauttaa tietyn lipun|
| POST | {baseurl}/tickets | Luo uuden lipun|
| PUT | {baseurl}/tickets/{id} | Muokkaa olemassa olevaa lippua tai luo uuden |
| DELETE | {baseurl}/tickets/{id} | Poistaa tietyn lipun|

### Post Ticket

**URL:** `{baseurl}/tickets`

**Metodi:** `POST`

**Esimerkkipyyntö:**

```
{
    "name": "ticket name",
    "redeemed": "false",
    "cost": {"costid": 2},
    "sale": {"saleid": 2}
}
```
**Vaadittu:**

Cost: ei null ja costid:tä vastaava Cost löytyy

Sale: ei null ja saleid:tä vastaava Sale löytyy

### Put Ticket

**URL:** `{baseurl}/tickets/{id}`

**Metodi:** `PUT`

**Esimerkkipyyntö:**

```
{
    "name": "new name",
    "redeemed": "false",
    "cost": {"costid": 2},
    "sale": {"saleid": 2}
}
```

**Vaadittu:**

Cost: ei null ja costid:tä vastaava Cost löytyy

Sale: ei null ja saleid:tä vastaava Sale löytyy




## TicketType

**Dev base url:** `http://locahost:8080`

| Metodi | URL | Kuvaus |
| -------- | ------- | --------- |
| GET | {baseurl}/tickettypes | Listaa kaikki lipputytyypit |
| GET | {baseurl}/tickettypes/{id} | Palauttaa tietyn lipputyypin|
| POST | {baseurl}/tickettypes | Luo uuden lipputyypin|
| PUT | {baseurl}/tickettypes/{id} | Muokkaa olemassa olevaa lipputyyppiä tai luo uuden |
| DELETE | {baseurl}/tickettypes/{id} | Poistaa tietyn lipputyypin |

### Post TicketType

**URL:** `{baseurl}/tickettypes`

**Metodi:** `POST`

**Esimerkkipyyntö:**

```
{
    "name": "type name",
    "note": "type note"
}
```

### Put TicketType

**URL:** `{baseurl}/tickettypes/{id}`

**Metodi:** `PUT`

**Esimerkkipyyntö:**

```
{
    "name": "new type name",
    "note": "new type note"
}
```

## AppUser

**Dev base url:** `http://locahost:8080`

| Metodi | URL | Kuvaus |
| -------- | ------- | --------- |
| GET | {baseurl}/users | Listaa kaikki käyttäjät |
| GET | {baseurl}/users/{id} | Palauttaa tietyn käyttäjän |
| POST | {baseurl}/users | Luo uuden käyttäjän |
| PUT | {baseurl}/users/{id} | Muokkaa olemassa olevaa käyttäjää tai luo uuden |
| DELETE | {baseurl}/users/{id} | Poistaa tietyn käyttäjän |

### Get AppUser

**URL:** `{baseurl}/users`

**Metodi:** `GET`

**Esimerkkituloste:**

```
[
    {
        "userid": 1,
        "firstname": "Testi",
        "lastname": "Esimerkki"
    },
    {
        "userid": 2,
        "firstname": "firstname1",
        "lastname": "lastname1"
    }
]
```

### Post AppUser

**URL:** `{baseurl}/users`

**Metodi:** `POST`

**Esimerkkipyyntö:**

```
{
    "firstname": "Testi",
    "lastname": "Käyttäjä"
}
```

### Put AppUser

**URL:** `{baseurl}/users/{id}`

**Metodi:** `PUT`

**Esimerkkipyyntö:**

```
{
    "firstname": "Testi",
    "lastname": "Käyttäjä"
}
```

### Delete AppUser

**URL:** `{baseurl}/users/{id}`

**Metodi:** `DELETE`

**Path variable:** 
id = poistettavan käyttäjän tunniste (Long)