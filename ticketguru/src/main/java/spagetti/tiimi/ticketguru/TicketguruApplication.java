package spagetti.tiimi.ticketguru;

import java.time.LocalDateTime;
import java.util.Base64;

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

			if (!urepository.existsByUsername("admin")) {
				LocalDateTime testTimeNow = LocalDateTime.now();
				LocalDateTime testTimeStatic = LocalDateTime.of(1999, 1, 31, 20, 00);

				Event event1 = new Event("Event1", "Venue1", testTimeNow);
				Event event2 = new Event("Event2", "Venue2", testTimeStatic);
				TicketType type1 = new TicketType("Aikuinen");
				TicketType type2 = new TicketType("Eläkeläinen", "Tarkista eläkeläisyys tarvittaessa");

				AppUser testUser = new AppUser("user", "$2a$10$6GGxgQSK2de0tm6SJjVRxePIbw2cOOqcp4Wb/Ne/b/1tpLX1ghVbe",
						"User", "Esimerkki", "USER");
				AppUser testAdmin = new AppUser("admin", "$2a$10$.BwaemM9KhTg6Ty/UQngsuh9k9hdGsvfAXKxNPX.2rfaxlXa86wDe",
						"Admin", "Esimerkki", "ADMIN");
				Cost cost1 = new Cost(type1, 20.50, event1);
				Cost cost2 = new Cost(type2, 7.99, event2);
				Cost cost3 = new Cost(type1, 25.50, event2);
				Sale sale1 = new Sale(testUser, testTimeNow, (cost1.getPrice() + cost2.getPrice()));
				Sale sale2 = new Sale(testAdmin, testTimeStatic, cost3.getPrice() * 2 + cost1.getPrice() * 3);
				Ticket ticket1 = new Ticket(cost1, sale1);
				Ticket ticket2 = new Ticket(cost2, sale1);
				Ticket ticket3 = new Ticket(cost1, sale2);
				Ticket ticket4 = new Ticket(cost3, sale2);
				Ticket ticket5 = new Ticket(cost3, sale2);
				Ticket ticket6 = new Ticket(cost1, sale2);
				Ticket ticket7 = new Ticket(cost1, sale2);

				trepository.save(type1);
				trepository.save(type2);

				urepository.save(testAdmin);

				urepository.save(testUser);

				erepository.save(event1);
				erepository.save(event2);

				cost1 = crepository.save(cost1);
				crepository.save(cost2);
				crepository.save(cost3);

				srepository.save(sale1);
				srepository.save(sale2);

				ticket1 = repository.save(ticket1);
				ticket1.setTicketCode(Base64.getEncoder().encodeToString((ticket1.getTicketid().toString()
						+ ticket1.getCost().getCostid() + ticket1.getSale().getSaleid().toString()).getBytes()));
				repository.save(ticket1);
				ticket2 = repository.save(ticket2);
				ticket2.setTicketCode(Base64.getEncoder().encodeToString((ticket2.getTicketid().toString()
						+ ticket2.getCost().getCostid() + ticket2.getSale().getSaleid().toString()).getBytes()));
				ticket3 = repository.save(ticket3);
				repository.save(ticket2);
				ticket3.setTicketCode(Base64.getEncoder().encodeToString((ticket3.getTicketid().toString()
						+ ticket3.getCost().getCostid() + ticket3.getSale().getSaleid().toString()).getBytes()));
				repository.save(ticket3);
				ticket4 = repository.save(ticket4);
				ticket4.setTicketCode(Base64.getEncoder().encodeToString((ticket4.getTicketid().toString()
						+ ticket4.getCost().getCostid() + ticket4.getSale().getSaleid().toString()).getBytes()));
				repository.save(ticket4);
				ticket5 = repository.save(ticket5);
				ticket5.setTicketCode(Base64.getEncoder().encodeToString((ticket5.getTicketid().toString()
						+ ticket5.getCost().getCostid() + ticket5.getSale().getSaleid().toString()).getBytes()));
				repository.save(ticket5);
				ticket6 = repository.save(ticket6);
				ticket6.setTicketCode(Base64.getEncoder().encodeToString((ticket6.getTicketid().toString()
						+ ticket6.getCost().getCostid() + ticket6.getSale().getSaleid().toString()).getBytes()));
				repository.save(ticket6);
				ticket7 = repository.save(ticket7);
				ticket7.setTicketCode(Base64.getEncoder().encodeToString((ticket7.getTicketid().toString()
						+ ticket7.getCost().getCostid() + ticket7.getSale().getSaleid().toString()).getBytes()));
				repository.save(ticket7);

				/*
				 * log.info("fetch tickets");
				 * for (Ticket ticket : repository.findAll()) {
				 * log.info(ticket.toString());
				 * }
				 */
				log.info("Added initial DB data");
				log.info("Get event id:" + cost1.getEvent().getEventid());
			}
		};
	}

}