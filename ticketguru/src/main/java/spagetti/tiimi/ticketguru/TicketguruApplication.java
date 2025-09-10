package spagetti.tiimi.ticketguru;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

@SpringBootApplication
public class TicketguruApplication {

	private static final Logger log = LoggerFactory.getLogger(TicketguruApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}

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

}

/*
 * @Bean
 * public CommandLineRunner ticketGuru(TicketRepository repository) {
 * return (args) -> {
 * log.info("Adding some test books");
 * repository.save(new Ticket("test1"));
 * repository.save(new Ticket("Test 2"));
 * repository.save(new Ticket("Test 3"));
 * repository.save(new Ticket("Test 3"));
 * log.info("fetch tickets");
 * for (Ticket ticket : repository.findAll()) {
 * log.info(ticket.toString());
 * }
 * 
 * };
 * }
 */