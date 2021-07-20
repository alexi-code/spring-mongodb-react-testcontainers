
# Reactive Ontology Local Server

## Java + Spring Boot + Reactive MongoDB + Testcontainers

Sample of 1-object middle-way lookup service with REST API.
The main logic is to be able to store the information in a local MongoDB instance or retrieve it from the remote server if it doesn't exist in the local one.

- Controller, service, repository and tests are implemented in a non-blocking manner.
- Integration tests cover possible use cases by deploying MongoDB in a temporary test docker container, being similar to production.

### Components

- JDK 11
- Spring Boot 2.5.2
- Spring WebFlux  
- Spring Reactor 
- Spring Data MongoDB Reactive
- Lombok 

Additionally, for test purposes: 
  
- Testcontainers
- Reactor Test
- Maven FailSafe plugin

### Local deploy


- To deploy MongoDB run ```docker-compose.yaml``` with ```docker-compose up```


- Run app: ```./mvnw spring-boot:run```


- Run tests: ```./mvnw test```


### Debugging


- MongoDB's connection string for Compass: ```mongodb://ontologydbadmin:ontologydbpassword@localhost:27017/ontologydb?authSource=ontologydb```


- For integration tests might be useful: ```MONGO_DB_CONTAINER.execInContainer("mongo", "test", "--eval", "db.getSiblingDB('test').createUser({user:'test',pwd:'test',roles:[{role:'readWrite',db:'test'}]});").getStdout()```


- Intellij HTTP sandbox for manual testing in a Postman manner: ```ManualRestTests.http```

### Troubleshooting 

- ```Exception opening socket``` - check if MongoDB instance available to connect

### Credentials

:hammer: Created by [Alex Antukhov](https://www.linkedin.com/in/antukhov/)
