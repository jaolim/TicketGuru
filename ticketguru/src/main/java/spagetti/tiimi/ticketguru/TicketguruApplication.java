package spagetti.tiimi.ticketguru;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

@SpringBootApplication
public class TicketguruApplication {

	private static final Logger log = LoggerFactory.getLogger(TicketguruApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}

	@Bean
	public CommandLineRunner ticketGuru(TicketRepository repository, EventRepository erepository, CostRepository crepository) {
		return (args) -> {
			log.info("Adding some test books");

			LocalDateTime testTimeNow = LocalDateTime.now();
			LocalDateTime testTimeStatic = LocalDateTime.of(1999, 1, 31, 20, 00);

			erepository.save(new Event("Event1", "Venue1", testTimeNow));
			erepository.save(new Event("Event2", "Venue2", testTimeStatic));

			crepository.save(new Cost("Aikuinen", 20.50));
			crepository.save(new Cost("Eläkeläinen", 7.99));

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