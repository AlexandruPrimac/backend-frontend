# Programming5-Project-Primac-Alexandru

## Author
**Name:** Alexandru Primac

**Student ID:** 0159060-77

**email:** alexandru.primac@student.kdg.be

**Class:** ACS201

**Academic Year:** 2025

---

## Domain Explanation and Relations Between Entities
### Domain Overview:
This project models a racing ecosystem with entities such as Cars, Races, and Sponsors. The application manages cars, their participation in races, and their sponsorships.

### Entity Relationships:
1. **Cars**:
   - Participate in multiple races (Many-to-Many relationship with Races).
   - Have multiple sponsors (Many-to-Many relationship with Sponsors).

2. **Races**:
   - Feature multiple cars (Many-to-Many relationship with Cars).

3. **Sponsors**:
   - Can sponsor multiple cars (Many-to-Many relationship with Cars).


---

## Week 2 & Week 3

### Filtering Cars by Brand - OK

```
GET http://localhost:8080/api/cars?brand=ferrari
Accept: application/json

Json Response:
HTTP/1.1 200 OK
[{"id":1,"brand":"Ferrari","model":"488 GTB","engine":3.9,...}]
```
### Filtering Cars by Brand - OK(empty list)
```
GET http://localhost:8080/api/cars?brand=Citroen
Accept: application/json

Json Response:
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 06 Mar 2025 19:46:09 GMT
[]
```
### Deleting Car with id 1 - 204 No Content

```
DELETE http://localhost:8080/api/cars/1

Json Response:
HTTP/1.1 204
<Response body is empty>
```

### Deleting Car with id 999 - 404 Not Found

```
DELETE http://localhost:8080/api/cars/999

Json Response:
HTTP/1.1 404 Not Found
{"error":"Car not found"}
```

### Creating New Car - 201 Created
```
POST http://localhost:8080/api/cars
Accept: application/json
Content-Type: application/json

{
"brand": "Toyota",
"model": "Corolla",
"engine": 1.8,
"horsePower": 130,
"year": 2020,
"category": "SPORTS"
}

Json Response:
HTTP/1.1 201 Created
{"id":2,"brand":"Toyota","model":"Corolla",...}
```

### Partial Car Update - 200 OK
```
PATCH http://localhost:8080/api/cars/1
Accept: application/json
Content-Type: application/json

{"horsepower": 630}

Json Response:
HTTP/1.1 200 OK
{"id":1,"horsepower":630,...}
```

### Partial Car Update - 404 NOT FOUND (id not found)
```
PATCH http://localhost:8080/api/races/100
Accept: application/json
Content-Type: application/json

{
"name": "Monaco Grand Prix",
"date": "2023-01-29",
"track": "Monaco Grand Prix",
"location": "Monaco",
"distance": 9.0
}

Json Response:
HTTP/1.1 404 
Content-Length: 0
<Response body is empty>
```

### Adding Race to Car - 200 ok
```
PATCH http://localhost:8080/api/cars/1/add-race?raceId=12
Accept: application/json
Content-Type: application/json

Json Response:
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Thu, 06 Mar 2025 19:54:06 GMT

{
"id": 1,
"brand": "Ferrari",
"model": "488 GTB",
"image": "ferrari.jpg",
"races": [
{
"id": 1,
"name": "Monaco Grand Prix",
"date": "2023-05-28",
"location": "Monaco",
"image": "monaco.jpg",
"track": "Monaco Grand Prix"
},
{
"id": 3,
"name": "Nurburgring Endurance",
"date": "2023-08-10",
"location": "Germany",
"image": "nurburgring.jpg",
"track": "Nurburgring"
},
{
"id": 12,
"name": "NHRA Nationals",
"date": "2024-03-15",
"location": "USA",
"image": "nhra.jpg",
"track": "Las Vegas Motor Speedway"
}]}
```

---

## Week 4 Security

### Users:
1. **alexandru@gmail.com**:
   - Password: alex1234

This user has the "USER_ROLE" and can only access the following pages:
```
   - http://localhost:8080/, http://localhost:8080/register, http://localhost:8080/cars, http://localhost:8080/races, http://localhost:8080/sponsors, http://localhost:8080/carDetails, http://localhost:8080/raceDetails, http://localhost:8080/user/details
```
   - The users with this role can't add or delete cars, races or sponsors. They also don't have access to modify the information from the these entiteis.


2. **sponge@gmail.com**:
   - Password: sponge1234

This user has the "USER_ADMIN" and can access every page:
   - The users with this role can access and modify anything. 

3. Guest users
Guests can only access the following pages:
```
   - http://localhost:8080/, http://localhost:8080/register, http://localhost:8080/cars, http://localhost:8080/races, http://localhost:8080/sponsors
```
   - The functionality of these pages is limited, they don't have the filter feature and they are not able to inspect the details of the entities.


## Instructions to Run the Project
### Prerequisites:
1. **Java 21**

### Steps:
1. Clone the repository:
   https://gitlab.com/kdg-ti/programming-5/projects-24-25/acs201/alexandru.primac/spring-backend.git