# Student Database - Spring REST API

## Overview

This project is a REST API that allows for creation of accounts and CRUD operations with Student data.

## Table of Contents

- [Stack](#stack)<br/>
- [Model](#model)<br/>
- [Development](#development)<br/>

## Stack

This project was developed with Java Spring Boot and PostgreSQL.

**Spring Dependencies**

- Spring Boot Security
- Spring Boot Web
- Spring Boot Data JPA
- PostgreSQL
- JWT
- Thymeleaf
- Dotenv-Java
- JUnit

## Model
![](misc/studentdatabase-model.png)*Modeled using PgModeler*

## Development

To run this application locally, you will need the following installed:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven 3.8.4+](https://maven.apache.org/download.cgi)
- [PostgreSQL](https://www.postgresql.org/download/)

**Setup**

1) Open up StudentDatabaseAPI folder.

```
cd .\StudentDatabaseAPI\
```

2) Create a `.env` file (**Note**: this file will be git ignored)
    
```
touch .env
```

3) Open up the `.env` file and provide the following variables. **Imporant** - Database must exist. This application is able to modify the given instance, not create a new one.

```bash
# URL of database including the database name eg. localhost:5432/studentdatabase
SPRING_DATASOURCE_URL=

# database username
SPRING_DATASOURCE_USERNAME=

# database password
SPRING_DATASOURCE_PASSWORD=

# JPA Mode
# create-drop - Create, then destroy Schema at the end of session (Will erase everything)
# validate - Validate the Schema but make no changes
# update - Update the Schema if necessary
# none
SPRING_JPA_DDL_MODE=

# JWT secret key
JWT_SECRET=
```

4) Now you can run the application using an IDE or by using the command below

```bash
mvn spring-boot:run
```