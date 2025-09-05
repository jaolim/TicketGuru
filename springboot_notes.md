# Spring boot notes

## Project

 - Spring boot: 3.5.5
 - Java
 - Group Id: spagetti.tiimi
 - Artifact Id: ticketguru
 - Package type: Jar
 - Java version: 24
 
## Dependencies

 - Spring Boot DevTools
 - Spring Web
 - Thymeleaf
 
 ## Spring Boot Cheatsheet
 
 ### Add Dependencies
 
 **pom.xml:**
 ```
 <dependencies>
    ...
    <dependency>
        <groupId>com.example.project</groupId>
        <artifactId>name-of-dependency</artifactId>
    </dependency>
 </dependencies>
 ```
 
### Basic structure

**Stucture:**
 
```
d:ticketguru
  f:pom.xml #settings & dependencies
  d:src
    d:main
      d:java
        d:spagetti
          d:tiimi
            d:ticketguru
              f:TicketguruApplication.java
              d:domain #classes
                f: Ticket.java
              d:web #controllers
                f: HomeController.java
      d:resources
        d:templates #html files
          f:index.html    
```

### End points (localhost:8080)

 - `/` & `/index` - index.html