# Vizsgaremek - vonatjegy applikáció

## Leírás 
Jelen vizsgaremek egy vonatjegy applikáció egyszerűsített változatát hivatott megvalósítani. 
Az applikációban létrehozhatóak vonatok, utasok. Az utasok kereshetnek számukra megfelelő vonatot, 
majd megválthatják a vonatjegyüket. A vonat keresésénél megadható a kiinduló és a cél állomás és az indulás időpontja.
A vonatjegy teljes áránál azt veszi alapul, hogy hány kilométer a távolság a két állomás között. 
Az utasok az életkoruk alapján érvényesíthetnek kedvezményt a jegyvásárláskor. 

## Használata

### Train - vonat

A `Train` entitás a következő attribútumokkal rendelkezik:

* `id` (a vonat egyedi azonosítója)
* `nameOfTrain` (a vonat neve, nem lehet `null` és üres, valamint `String` típusú, legfeljebb 255 karakter hosszú.)
* `trainType` (a vonat típusa, ez egy enum a következő értékekkel: `PASSENGER_TRAIN`, `FAST_TRAIN`, `INTERCITY`.)
* `departureTime` (a vonat indulási ideje, nem lehet `null`, valamint `LocalDateTime` típusú.)
* `departurePlace` (a vonat erről az állomásról indul, nem lehet `null` és üres, valamint `String` típusú, legfeljebb 255 karakter hosszú.)
* `arrivalTime` (a vonat érkezési ideje, nem lehet `null`, valamint `LocalDateTime` típusú.)
* `arrivalPlace` (a vonat erre az állomásra érkezik, nem lehet `null` és üres, valamint `String` típusú, legfeljebb 255 karakter hosszú.)
* `distance` (a két állomás távolsága kilométerben, nem lehet `0` és csak pozitív egész számot vehet fel értéknek.)
* `tickets` (a vonatra megváltott jegyek listája, `List<Ticket>` típusú és OneToMany kétirányú kapcsolatban áll a `Ticket` entitással.)

A következő végpontokon érjük el az entitást:

| HTTP metódus | Végpont              | Leírás                            |  HTTP Response státusz  |
|--------------|----------------------|-----------------------------------|:-----------------------:|
| GET          | `"/api/trains"`      | lekérdezi az összes vonatot       |           202           |       
| GET          | `"/api/trains/{id}"` | lekérdez egy vonatot `id` alapján |           202           |       
| POST         | `"/api/trains"`      | létrehoz egy vonatot              |           201           |       
| PUT          | `"/api/trains/{id}"` | módosít egy vonatot `id` alapján  |           202           |       
| DELETE       | `"/api/trains/{id}"` | `id` alapján kitöröl egy vonatot  |           204           |       
| DELETE       | `"/api/trains/"`     | az összes vonatot törli           |           204           |       

A `Train` entitás adatai az adatbázisban a `trains` táblában tárolódnak.

---

### Passenger - utas

A `Passenger` entitás a következő attribútumokkal rendelkezik:

* `id` (az utas egyedi azonosítója)
* `name` (az utas neve, nem lehet `null` és üres, valamint `String` típusú, legfeljebb 255 karakter hosszú.)
* `dateOfBirth` (az utas születési ideje (az utazási kedvezményhez szükséges), nem lehet `null` és `LocalDate` típusú.)
* `tickets` (az utas által birtokolt jegyek listája, `List<Ticket>` típusú és OneToMany kétirányú kapcsolatban áll `Ticket` entitással.)

A következő végpontokon érjük el az entitást:

| HTTP metódus | Végpont                  | Leírás                          | HTTP Response státusz |
|--------------|--------------------------|---------------------------------|:---------------------:|
| GET          | `"/api/passengers"`      | lekérdezi az összes utast       |          202          |         
| GET          | `"/api/passengers/{id}"` | lekérdez egy utast `id` alapján |          202          |         
| POST         | `"/api/passengers"`      | létrehoz egy utast              |          201          |         
| PUT          | `"/api/passengers/{id}"` | módosít egy utast `id` alapján  |          202          |         
| DELETE       | `"/api/passengers/{id}"` | `id` alapján kitöröl egy utast  |          204          |         
| DELETE       | `"/api/passengers/"`     | az összes utast törli           |          204          |         

A `Passenger` entitás adatai az adatbázisban a `passengers` táblában tárolódnak.

---

### Discounts - kedvezmények
Az applikáción keresztül nem módosítható, induláskor az adatbázis már tartalmazza ezeket az értékeket. 

| Életkortól | Életkorig | Kedvezmény mértéke |                         
|:----------:|:---------:|:------------------:|
|     0      |     6     |       100 %        |
|     6      |    14     |        50 %        |
|     14     |    26     |        33 %        |
|     27     |    65     |        0 %         |
|     65     |   99999   |       100 %        |

A `Discount` entitás a következő attribútumokkal rendelkezik:

* `id` (a kedvezmény egyedi azonosítója.)
* `fromAge` (életkortól, típusa `integer` értéke nem lehet negatív.)
* `toAge` (életkorig, típusa `integer` értéke nem lehet negatív.)
* `discountValue` (a kedvezmény mértéke százalékban, típusa `integer` értéke nem lehet negatív.)

következő végpontokon érjük el az entitást:

| HTTP metódus | Végpont                 | Leírás                                     | HTTP Response státusz |
|--------------|-------------------------|--------------------------------------------|:---------------------:|
| GET          | `"/api/discounts"`      | lekérdezi az összes kedvezményt            |          202          |         
| GET          | `"/api/discounts/age"`  | age paraméterrel lekérdezhető a kedvezmény |          202          |
| GET          | `"/api/discounts/{id}"` | lekérdez egy kedvezményt `id` alapján      |          202          |   

---

### Ticket - jegy

A `Ticket` entitás a következő attribútumokkal rendelkezik:

* `id` (az utas egyedi azonosítója)
* `fullPrice` (a jegy teljes ára,  típusa `double` értéke nem lehet negatív.)
* `priceWithDiscount` (a jegy kedvezményes ára,  típusa `double` értéke nem lehet negatív.)
* `passenger` (ManyToOne kétirányú kapcsolatban áll `Passenger` entitással.)
* `train` (ManyToOne kétirányú kapcsolatban áll a `Train` entitással.)
* `discount` (ManyToOne kétirányú kapcsolatban áll a `Discount` entitással.)

A következő végpontokon érjük el az entitást:

| HTTP metódus | Végpont               | Leírás                           | HTTP Response státusz |
|--------------|-----------------------|----------------------------------|:---------------------:|
| GET          | `"/api/tickets"`      | lekérdezi az összes jegyet       |          202          |         
| GET          | `"/api/tickets/{id}"` | lekérdez egy jegyet `id` alapján |          202          |         
| POST         | `"/api/passengers"`   | létrehoz egy jegyet              |          201          |         
| DELETE       | `"/api/tickets/{id}"` | `id` alapján kitöröl egy jegyet  |          204          |         
| DELETE       | `"/api/tickets/"`     | az összes jegyet törli           |          204          |         

A `Ticket` entitás adatai az adatbázisban a `tickets` táblában tárolódnak.
---


## Technológiai részletek

Ez egy klasszikus háromrétegű webes alkalmazás, controller, service és repository
réteggel, entitásonként a rétegeknek megfelelően elnevezett osztályokkal. A megvalósítás
Java programnyelven, Spring Boot használatával történt. Az alkalmazás HTTP kéréseket
képes fogadni, ezt a RESTful webszolgáltatások segítségével valósítja meg.
Adattárolásra SQL alapú MariaDB adatbázist használ, melyben a táblákat Flyway hozza létre.
Az adatbáziskezelés Spring Data JPA technológiával történik. A beérkező adatok validálását a
Spring Boot `spring-boot-starter-validation` modulja végzi, az általános hibakezelést pedig
a `problem-spring-web-starter` projekt.
Az alkalmazás tesztelésére WebClient-tel implementált integrációs
tesztek állnak rendelkezésre, a kipróbálásához pedig az `src/test/java` könyvtáron belül
HTTP fájlok, valamint egy részletesen feliratozott Swagger felület. A mellékelt `Dockerfile`
segítségével az alkalmazásból Docker image készíthető.

---

## Swagger felület és Open API link

[Swagger UI](http://localhost:8080/swagger-ui.html)

[Open API](http://localhost:8080/v3/api-docs)

---

## MariaDb indítása Dockerben
`docker run -d -e MYSQL_DATABASE=traintickets -e MYSQL_USER=traintickets -e MYSQL_PASSWORD=traintickets -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -p 3306:3306 --name traintickets-mariadb mariadb`

## Az alkalmazás buildelése
`mvnw package`

`docker build -t traintickets .` 