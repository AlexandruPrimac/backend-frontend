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

---

## Instructions to Run the Project
### Prerequisites:
1. **Java 21**

### Steps:
1. Clone the repository:
   https://gitlab.com/kdg-ti/programming-5/projects-24-25/acs201/alexandru.primac/spring-backend.git