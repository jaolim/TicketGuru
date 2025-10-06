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

### Get Ticket

**URL:** `{baseurl}/tickets` & `{baseurl}/tickets/{id}`

**Metodi:** `GET`

**Vastaukset:**

- `200` - Lippu tai liput haettu
- `404` - Lippua ei löydy (vain id:llä haku)

**Esimerkkituloste:**

```
[
    {
        "ticketid": 1,
        "cost": {
            "costid": 1,
            "price": 20.5,
            "type": {
                "name": "Aikuinen",
                "note": null,
                "typeid": 1
            }
        },
        "sale": null,
        "redeemed": false,
        "name": "test1"
    },
    {
        "ticketid": 2,
        "cost": {
            "costid": 2,
            "price": 7.99,
            "type": {
                "name": "Eläkeläinen",
                "note": null,
                "typeid": 2
            }
        },
        "sale": null,
        "redeemed": false,
        "name": "test2"
    }
]
```

### Post Ticket

**URL:** `{baseurl}/tickets`

**Metodi:** `POST`

**Vastaukset:**

- `201` - Lippu luotu
- `400` - Puuttuvaa tai väärää dataa

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

**Vastaukset:**

- `201` - Lippua muokattu
- `400` - Puuttuvaa tai väärää dataa
- `404` - Lippua ei ole olemassa

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

{id}: tällä id:llä oleva lippu on olemassa

### Delete Ticket

**URL:** `{baseurl}/tickets/{id}`

**Metodi:** `DELETE`

**Vastaukset:**

- `200` - Lippu tuhottu
- `404` - Lippua ei ole olemassa

## TicketType

**Dev base url:** `http://locahost:8080`

| Metodi | URL | Kuvaus |
| -------- | ------- | --------- |
| GET | {baseurl}/tickettypes | Listaa kaikki lipputytyypit |
| GET | {baseurl}/tickettypes/{id} | Palauttaa tietyn lipputyypin|
| POST | {baseurl}/tickettypes | Luo uuden lipputyypin|
| PUT | {baseurl}/tickettypes/{id} | Muokkaa olemassa olevaa lipputyyppiä tai luo uuden |
| DELETE | {baseurl}/tickettypes/{id} | Poistaa tietyn lipputyypin |

### Get TicketType

**URL:** `{baseurl}/tickettypes`

**Metodi:** `GET`

**Esimerkkituloste:**

```
[
    {
        "name": "Aikuinen",
        "note": null,
        "typeid": 1
    },
    {
        "name": "Eläkeläinen",
        "note": null,
        "typeid": 2
    }
]
```

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

## Sale

**Dev base url:** `http://locahost:8080`

| Metodi | URL | Kuvaus |
| ------- | --- | ------ |
| GET | {baseurl}/sales | Listaa kaikki myynnit |
| GET | {baseurl}/sales/{id} | Palauttaa tietyn myynnin |
| POST | {baseurl}/sales | Luo uuden myynnin |
| PUT | {baseurl}/sales/{id} | Muokkaa olemassa olevaa myyntiä (päivittää hinnan) |
| DELETE | {baseurl}/sales/{id} | Poistaa tietyn myynnin |

### Get Sale

**URL:** `{baseurl}/sales` & `{baseurl}/sales/{id}`

**Metodi:** `GET`

**Vastaukset:**

- `200`- Myyntien haku onnistui

**Esimerkkituloste:**

```
[
    {
        "saleid": 1,
        "user": {
            "userid": 2,
            "firstname": "firstname1",
            "lastname": "lastname1"
        },
        "price": 0,
        "time": "2025-09-30T18:23:07.018202"
    },
    {
        "saleid": 2,
        "user": {
            "userid": 3,
            "firstname": "firstname2",
            "lastname": "lastname2"
        },
        "price": 0,
        "time": "2025-09-30T18:23:07.018202"
    }
]
```

### Post Sale

**URL:** `{baseurl}/sales`

**Metodi:** `POST`

**Vastaukset:** 

- `201` - Myynti luotu
- `400`- Puuttuvaa tai virheellistä dataa

**Vaadittu:**

User: ei null ja userid:tä vastaava User löytyy

**Esimerkkipyyntö:**

```
{
    "saleid": 1,
    "user": {
        "userid": 2,
        "firstname": "firstname1",
        "lastname": "lastname1"
    },
    "price": 10,
    "time": "2025-09-30T17:06:31.077351"
}
```

### Put Sale

**URL:** `{baseurl}/sales/{id}`

**Metodi:** `PUT`

**Vastaukset:**

- `200`- Myyntiä muokattu
- `400`- Puuttuvaa tai virheellistä dataa
- `404`- Myyntiä ei ole olemassa

**Vaadittu:**

{id}: tällä id:llä oleva Sale on olemassa
User: ei null ja userid:tä vastaava User löytyy

**Esimerkkipyyntö:**

```
{
    "price": 35
}
```

### Delete Sale

**URL:** `{baseurl}/sales/{id}``

**Metodi:** `DELETE``

**Vastaukset:**

- `204`- Myynti poistettu
- `404`- Myyntiä ei ole olemassa

## Cost

**Dev base url:** `http://locahost:8080`

| Metodi | URL | Kuvaus |
| -------- | ------- | --------- |
| GET | {baseurl}/costs | Listaa kaikki hinnat |
| GET | {baseurl}/costs/{id} | Palauttaa tietyn hinnan |
| POST | {baseurl}/costs | Luo uuden hinnan |
| PUT | {baseurl}/costs/{id} | Muokkaa olemassa olevaa hintaa |
| DELETE | {baseurl}/costs/{id} | Poistaa tietyn hinnan |

## Get Cost

**URL:** `{baseurl}/costs`

**Metodi:** `GET`

**Esimerkkituloste:**

```
[
    {
        "costid": 1,
        "price": 20.5,
        "type": {
            "name": "testi",
            "note": null,
            "typeid": 1
        }
    },
    {
        "costid": 2,
        "price": 7.99,
        "type": {
            "name": "testi 2",
            "note": null,
            "typeid": 2
        }
    }
]
```

### Post Cost

**URL:** `{baseurl}/costs`

**Metodi:** `POST`

**Esimerkkipyyntö:**

```
{
    "price": 27.5,
    "type": {
        "name": "Aikuinen",
        "note": null,
        "typeid": 1
    }
}
```

### Put Cost

**URL:** `{baseurl}/costs/{id}`

**Metodi:** `PUT`

**Esimerkkipyyntö:**

```
{
    "price"; 22.0
}
```
### Delete Cost

**URL:** `{baseurl}/costs/{id}`

**Metodi:** `DELETE`