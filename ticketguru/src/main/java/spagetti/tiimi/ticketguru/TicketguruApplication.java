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
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;

@SpringBootApplication
public class TicketguruApplication {

	private static final Logger log = LoggerFactory.getLogger(TicketguruApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}

	@Bean
	public CommandLineRunner ticketGuru(TicketRepository repository, EventRepository erepository,
			AppUserRepository urepository, CostRepository crepository, SaleRepository srepository,
			TicketTypeRepository trepository) {

		return (args) -> {
			log.info("Adding some test books");

			if (!urepository.existsByUsername("admin")) {
				LocalDateTime testTimeNow = LocalDateTime.now();
				LocalDateTime testTimeStatic = LocalDateTime.of(1999, 1, 31, 20, 00);

				Event event1 = new Event("Event1", "Venue1", testTimeNow);
				Event event2 = new Event("Event2", "Venue2", testTimeStatic);
				TicketType type1 = new TicketType("Aikuinen");
				TicketType type2 = new TicketType("Eläkeläinen", "Tarkista eläkeläisyys tarvittaessa");

				AppUser testUser = new AppUser("user", "$2a$10$8vXq2.voWt5otOsgzMoYoe27VgDlCrBvqb9GQnNuWBz9t8zKuqtgu",
						"User", "Esimerkki", "USER");
				AppUser testAdmin = new AppUser("admin", "$2a$10$xh.egrUL4AuuvOuEEhW9nOV9JsVCIQe5OjOYEfJkh83T9zgTyU/pm",
						"Admin", "Esimerkki", "ADMIN");
				Cost cost1 = new Cost(type1, 20.50, event1);
				Cost cost2 = new Cost(type2, 7.99, event2);
				Cost cost3 = new Cost(type1, 25.50, event2);
				Sale sale1 = new Sale(testUser, testTimeNow, (cost1.getPrice() + cost2.getPrice()));
				Sale sale2 = new Sale(testAdmin, testTimeStatic, cost3.getPrice() * 2 + cost1.getPrice() * 3);
				Ticket ticket1 = new Ticket("test1", cost1, sale1);
				Ticket ticket2 = new Ticket("test2", cost2, sale1);
				Ticket ticket3 = new Ticket("test3", cost1, sale2);
				Ticket ticket4 = new Ticket("test4", cost3, sale2);
				Ticket ticket5 = new Ticket("test5", cost3, sale2);
				Ticket ticket6 = new Ticket("test6", cost1, sale2);
				Ticket ticket7 = new Ticket("test7", cost1, sale2);
				trepository.save(type1);
				trepository.save(type2);

				urepository.save(testUser);
				urepository.save(testAdmin);

				erepository.save(event1);
				erepository.save(event2);

				crepository.save(cost1);
				crepository.save(cost2);
				crepository.save(cost3);

				srepository.save(sale1);
				srepository.save(sale2);

				repository.save(ticket1);
				repository.save(ticket2);
				repository.save(ticket3);
				repository.save(ticket4);
				repository.save(ticket5);
				repository.save(ticket6);
				repository.save(ticket7);

				log.info("fetch tickets");
				for (Ticket ticket : repository.findAll()) {
					log.info(ticket.toString());
				}
			}
		};
	}

}