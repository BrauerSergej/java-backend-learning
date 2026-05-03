# Java Backend Development: From Servlets to Hibernate

This repository contains a comprehensive set of educational projects demonstrating the evolution of Java-based server-side development: from the fundamentals of HTTP Servlets to advanced data management using Hibernate.

## 🚀 Tech Stack
* **Java 17+**
* **Servlets API** (Jakarta EE)
* **JDBC** (PostgreSQL)
* **Hibernate ORM**
* **Maven** (Project Management)
* **Jackson** (JSON Processing)

---

## 🏗 Key Implementation Stages

### 1. Clean Architecture
The project is built following the principle of separation of concerns:
* **Domain**: Core business entities.
* **Repository**: Data access layer (interfaces and multiple implementations).
* **Service**: Business logic connecting controllers and repositories.
* **Controller**: Handling incoming HTTP requests.

### 2. HTTP & Servlets
Implemented low-level server logic:
* Handling `GET`, `POST`, `PUT`, and `DELETE` methods.
* Managing `HttpServletRequest` and `HttpServletResponse`.
* Integrated **Jackson** for seamless JSON-to-Java object parsing.

### 3. Persistence Layer
The project showcases three distinct approaches to data management:
* **List-based**: In-memory storage for rapid testing.
* **JDBC**: Direct interaction with PostgreSQL via SQL queries.
* **Hibernate**: Utilizing ORM for automated mapping of database tables to Java classes (`House.java`, `house.mapping.xml`).

---

## 📂 Project Structure
* `src/main/java/app/controller` — API Entry points.
* `src/main/java/app/service` — Business logic layer.
* `src/main/java/app/repository` — Persistence implementations (JDBC/Hibernate).
* `src/main/resources` — Database configurations and ORM mappings.

---

## 🛠 Getting Started
1. Clone the repository: `git clone https://github.com/BrauerSergej/java-backend-learning.git`
2. Configure your PostgreSQL connection in the configuration files.
3. Build the project: `mvn clean install`.
4. Run the application using Tomcat or an embedded server.