# Spring boot notes

## Project

 - Spring boot: 3.5.5
 - Java
 - Group Id: spagetti.tiimi
 - Artifact Id: ticketguru
 - Package type: Jar
 - Java version: 21
 
## Dependencies

 - Spring Boot DevTools
 - Spring Web
 - Thymeleaf
 - H2 database
 - Spring data jpa
 
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
 - `/h2-console` - h2 database console
 
## H2 use

### Settings

```
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.datasource.url=jdbc:h2:mem:testdb
```

### Console

`localhost:8080/h2-console`

### insert dummy data


Edit TicketguruApplication:

```
	@Bean
	public CommandLineRunner ticketGuru(TicketRepository repository) {
		return (args) -> {
			log.info("Adding some test books");
			repository.save(new Ticket("test1"));
			repository.save(new Ticket("Test 2"));
			repository.save(new Ticket("Test 3"));
			repository.save(new Ticket("Test 3"));
			log.info("fetch tickets");
			for (Ticket ticket : repository.findAll()) {
				log.info(ticket.toString());
			}

		};
	}
```

### use data

1. Inject repository in Controller:

```
    public HomeController(TicketRepository repository) {
        this.repository = repository;
    }
```

2. Pass data:

```
    @GetMapping(value = {"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("ticket", testTicket);
        model.addAttribute("tickets", repository.findAll()); // Passing tickets list
        return "index";
    }
```