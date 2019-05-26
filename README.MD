# Hierarchical organisation chart application

This is example of hierarchical organisation chart application with ability to have more than one manager per employer. 

This project is built using:

- ES6
- Vis.js 4.21.0
- Spring Boot 2.1.4
- Spring Data Neo4j 5.1.6
- Neo4j 3.5.5
- Neo4j OGM 3.1.9

It uses the native ECMAScript 6 frontend application with hierarchical chart support using [vis.js](http://visjs.org) library to interact with Spring Boot backend application with the Neo4j Object Graph Mapper(OGM) via a RESTful interface.

Getting Started
---------------

### Prerequisites

You will need to following to run this application:

- [Java 8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Maven](https://maven.apache.org/)


### Download the application

Either [download](https://github.com/jutsir/orgchart/archive/master.zip) the application or use `git` to clone the application:

```
git clone git@github.com:jutsir/orgchart.git
cd orgchart
```

### Starting the application

No configuration is required as this application will run with a temporary embedded Neo4j Database by default.
You can start the application using maven:

```
mvn spring-boot:run
```

And that's it! Head to <http://localhost:8080> to see your application running.

> **NOTE**
> If you restart your application you may notice changes made in your database disappear.
> That's because if you don't supply a directory to house the database only a temporary data store is set up (and gets destroyed on close).
> If you want the data to persist between restarts then modify `src/main/resources/ogm.properties` and uncomment the line 
> with `URI=file:///var/tmp/neo4j.db` or wherever you want your database to reside.

#### Using the binary Bolt driver

If you'd like to run this application using the fast bolt driver against a standalone Neo4j instance then you will need to do the following.

1. Make sure you have a [Neo4j](http://neo4j.org) instance installed and running.
2. You'll need to provide connection credentials for the database to the bolt driver. To do this, set the Neo4j username and password in `src/main/resources/ogm.properties`
    ```
    spring.data.neo4j.uri=bolt://localhost
    spring.data.neo4j.username=<your username>
    spring.data.neo4j.password=<your password>

    ```
    You can use commented example of connection in the top of `src/main/resources/ogm.properties` file.
3. You can then start the application using the same maven run command shown above.

### Loading the sample data

> **WARNING**
>
> By default, the application will use an Neo4j embedded instance of Neo4j.
> If you are running this application with the bolt drivers be careful as **IT WILL DESTROY ALL THE DATA IN THAT DATABASE** after hitting the URL below.
> So if you don't want that to happen please back up any existing database first.

You may notice that there is no data for you to interact with. To fix this, hit the following endpoint from your browser or using `curl`:

<http://localhost:8080/api/reload>

This will pre-load the Neo4j database with sample of hierarchical employees data.  
File with sample data located in the `src/main/resources/orgchart.cql` and file reference can be changed via `src/main/resources/application.properties` file.

### Stopping the application server

You can stop the application server at any time by pressing `Ctrl-C` in the console window from where you launched it.


### Running the tests

You can run the unit test using maven:

```
mvn test
```

Tests used to test controllers, services and database repository levels.
Tests configuration files located in `src/test/resources/ogmtest.properties` and `src/test/resources/application.properties`

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Accessing Data with Neo4j](https://spring.io/guides/gs/accessing-data-neo4j/)
* [Accessing JPA Data with REST](https://spring.io/guides/gs/accessing-data-rest/)
* [Accessing Neo4j Data with REST](https://spring.io/guides/gs/accessing-neo4j-data-rest/)

## Authors

* **Juri Tsirihhov** - *Initial work* - [jutsir](https://github.com/jutsir)



