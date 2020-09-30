Interview question
==================

## Description
 This project provides API for Questions.
 It has 4 endpoints to save and retrieve questions posted in the forum.

## APIs List
Following are the list of API endpoints created.
1. Create Question:
   Endpoint: http://localhost:5000/questions <br/>
   Method  : POST
2. Create Reply: <br/>
   Endpoint: http://localhost:5000/questions/{questionId}/reply <br/>
   Method: POST
3. Get List of Questions:
   Endpoint: http://localhost:5000/questions <br/>
   Method  : GET
4. Get Question Details: <br/>
   Endpoint: http://localhost:5000/questions/{questionId} <br/>
   Method: GET
   
All api definitions are found at swagger: `/api/questions-forum.yml`

## Build And Deployment
 To run build and run the project you should have Java 8 and Maven install in your system.
 To check if they are installed run the following commands. <br/>
 `java -version` <br/>
 `mvn -version`
 
 To build project run: 
 `mvn clean install`
 
 To run the project: `mvn spring-boot:run `
 
 ## Javadoc
 Project Javadoc documentation is located at: `/javadoc/ `

## Junit Tests
Unit tests and integration tests covers are scenarios and validations defined by the API. 
Any change in source code requires a corresponding change in defined test cases.

## Database
For the purposes of easy testing and running application is configured to use In-memory H2 Database.
 
