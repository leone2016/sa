# Lab Exercise: Real-time Messaging with Spring WebSockets and PostgreSQL

In this exercise, you will develop a small Spring Boot application that leverages WebSockets to enable real-time message exchange between clients. Each message sent will be persisted in a PostgreSQL database using JPA. You will set up the database, create a JPA entity class for Message, store each message, and broadcast new messages to all connected clients.

## Run
```bash
mvn spring-boot:run
```