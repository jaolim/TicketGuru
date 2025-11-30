package spagetti.tiimi.ticketguru;

import java.time.LocalDateTime;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.image.BufferedImage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

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
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;

@SpringBootApplication
public class TicketguruApplication {

	private static final Logger log = LoggerFactory.getLogger(TicketguruApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TicketguruApplication.class, args);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}

	@Bean
	public CommandLineRunner ticketGuru(TicketRepository repository, EventRepository erepository,
			AppUserRepository urepository, CostRepository crepository, SaleRepository srepository,
			TicketTypeRepository trepository, VenueRepository vRepository) {

		return (args) -> {

			if (!urepository.existsByUsername("admin")) {
				repository.deleteAll();
				crepository.deleteAll();
				trepository.deleteAll();
				srepository.deleteAll();
				erepository.deleteAll();
				vRepository.deleteAll();
				urepository.deleteAll();

				LocalDateTime testTimeNow = LocalDateTime.now();
				LocalDateTime testTimeStatic = LocalDateTime.of(2025, 12, 20, 20, 00);
				LocalDateTime testTimeStatic2 = LocalDateTime.of(2026, 1, 20, 18, 30);
				LocalDateTime testTimeStatic3 = LocalDateTime.of(2025, 12, 5, 22, 00);
				LocalDateTime testTimeStatic4 = LocalDateTime.of(2026, 2, 15, 17, 00);
				LocalDateTime testTimeStatic5 = LocalDateTime.of(2025, 11, 30, 11, 49);

				Venue venue1 = new Venue("Espoon Kulttuurikeskus", "Kulttuuriaukio 2, 02100 Espoo");
				Venue venue2 = new Venue("Kansallisteatteri", "Läntinen Teatterikuja 1, 00100 Helsinki");
				Venue venue3 = new Venue("Kulttuuritalo", "Sturenkatu 4, 00510 Helsinki");
				vRepository.save(venue1);
				vRepository.save(venue2);
				vRepository.save(venue3);

				Event event1 = new Event("Joulukonsertti", venue1, testTimeStatic, 500);
				Event event2 = new Event("Hyväntekeväisyysillallinen", venue1, testTimeStatic2, 20);
				Event event3 = new Event("Toinen Tasavalta", venue2, testTimeStatic4, 883);
				Event event4 = new Event("Jytää ja iskelmää itsenäisyyspäivän etkot", venue3, testTimeStatic3, 200);
				TicketType type1 = new TicketType("Aikuinen");
				TicketType type2 = new TicketType("Eläkeläinen", "Tarkista eläkeläisyys tarvittaessa");
				TicketType type3 = new TicketType("Opiskelija", "Tarkista opiskelijakortti");
				TicketType type4 = new TicketType("Lapsi");
				TicketType type5 = new TicketType("Taso 1");
				TicketType type6 = new TicketType("Taso 2");
				TicketType type7 = new TicketType("Taso 3");
				TicketType type8 = new TicketType("Ovi");

				AppUser testUser = new AppUser("user", "$2a$10$6GGxgQSK2de0tm6SJjVRxePIbw2cOOqcp4Wb/Ne/b/1tpLX1ghVbe",
						"User", "Esimerkki", "USER");
				AppUser testAdmin = new AppUser("admin", "$2a$10$.BwaemM9KhTg6Ty/UQngsuh9k9hdGsvfAXKxNPX.2rfaxlXa86wDe",
						"Admin", "Esimerkki", "ADMIN");
				Cost cost1 = new Cost(type1, 50.00, event1);
				Cost cost2 = new Cost(type2, 29.90, event1);
				Cost cost3 = new Cost(type3, 29.90, event1);
				Cost cost4 = new Cost(type4, 19.50, event1);
				Cost cost5 = new Cost(type5, 100.00, event2);
				Cost cost6 = new Cost(type6, 200.00, event2);
				Cost cost7 = new Cost(type7, 500.00, event2);
				Cost cost8 = new Cost(type1, 8.00, event4);
				Cost cost9 = new Cost(type8, 11.50, event4);
				Cost cost10 = new Cost(type1, 62.00, event3);
				Cost cost11 = new Cost(type2, 55.00, event3);
				Cost cost12 = new Cost(type3, 25.00, event3);
				Sale sale1 = new Sale(testUser, testTimeNow, (cost5.getPrice() + cost7.getPrice()));
				Sale sale2 = new Sale(testAdmin, testTimeStatic5, cost3.getPrice() * 2 + cost1.getPrice() * 3);
				Ticket ticket1 = new Ticket(cost5, sale1);
				ticket1.setPrice(cost1.getPrice());
				Ticket ticket2 = new Ticket(cost7, sale1);
				ticket2.setPrice(cost2.getPrice());
				Ticket ticket3 = new Ticket(cost1, sale2);
				ticket3.setPrice(cost1.getPrice());
				Ticket ticket4 = new Ticket(cost3, sale2);
				ticket4.setPrice(cost3.getPrice());
				Ticket ticket5 = new Ticket(cost3, sale2);
				ticket5.setPrice(cost3.getPrice());
				Ticket ticket6 = new Ticket(cost1, sale2);
				ticket6.setPrice(cost1.getPrice());
				Ticket ticket7 = new Ticket(cost1, sale2);
				ticket7.setPrice(cost1.getPrice());

				trepository.save(type1);
				trepository.save(type2);
				trepository.save(type3);
				trepository.save(type4);
				trepository.save(type5);
				trepository.save(type6);
				trepository.save(type7);
				trepository.save(type8);

				urepository.save(testAdmin);

				urepository.save(testUser);

				Long savedEvent4Id = erepository.save(event4).getEventid();
				Long savedEvent1Id = erepository.save(event1).getEventid();
				Long savedEvent2Id = erepository.save(event2).getEventid();
				Long savedEvent3Id = erepository.save(event3).getEventid();

				crepository.save(cost1);
				crepository.save(cost2);
				crepository.save(cost3);
				crepository.save(cost4);
				crepository.save(cost5);
				crepository.save(cost6);
				crepository.save(cost7);
				crepository.save(cost8);
				crepository.save(cost9);
				crepository.save(cost10);
				crepository.save(cost11);
				crepository.save(cost12);

				srepository.save(sale1);
				srepository.save(sale2);

				ticket1 = repository.save(ticket1);
				ticket1.setTicketCode(Base64.getEncoder().encodeToString((ticket1.getTicketid().toString()
						+ ticket1.getCost().getCostid()).getBytes()));
				repository.save(ticket1);
				ticket2 = repository.save(ticket2);
				ticket2.setTicketCode(Base64.getEncoder().encodeToString((ticket2.getTicketid().toString()
						+ ticket2.getCost().getCostid()).getBytes()));
				ticket3 = repository.save(ticket3);
				repository.save(ticket2);
				ticket3.setTicketCode(Base64.getEncoder().encodeToString((ticket3.getTicketid().toString()
						+ ticket3.getCost().getCostid()).getBytes()));
				repository.save(ticket3);
				ticket4 = repository.save(ticket4);
				ticket4.setTicketCode(Base64.getEncoder().encodeToString((ticket4.getTicketid().toString()
						+ ticket4.getCost().getCostid()).getBytes()));
				repository.save(ticket4);
				ticket5 = repository.save(ticket5);
				ticket5.setTicketCode(Base64.getEncoder().encodeToString((ticket5.getTicketid().toString()
						+ ticket5.getCost().getCostid()).getBytes()));
				repository.save(ticket5);
				ticket6 = repository.save(ticket6);
				ticket6.setTicketCode(Base64.getEncoder().encodeToString((ticket6.getTicketid().toString()
						+ ticket6.getCost().getCostid()).getBytes()));
				repository.save(ticket6);
				ticket7 = repository.save(ticket7);
				ticket7.setTicketCode(Base64.getEncoder().encodeToString((ticket7.getTicketid().toString()
						+ ticket7.getCost().getCostid()).getBytes()));
				repository.save(ticket7);

				Event savedEvent1 = erepository.findById(savedEvent1Id).get();
				Event savedEvent2 = erepository.findById(savedEvent2Id).get();
				Event savedEvent3 = erepository.findById(savedEvent3Id).get();
				Event savedEvent4 = erepository.findById(savedEvent4Id).get();
				savedEvent1.setTotalTickets(repository.countByCost_Event_Eventid(savedEvent1.getEventid()));
				savedEvent2.setTotalTickets(repository.countByCost_Event_Eventid(savedEvent2.getEventid()));
				savedEvent3.setTotalTickets(repository.countByCost_Event_Eventid(savedEvent3.getEventid()));
				savedEvent4.setTotalTickets(repository.countByCost_Event_Eventid(savedEvent4.getEventid()));

				erepository.save(savedEvent1);
				erepository.save(savedEvent2);
				erepository.save(savedEvent3);
				erepository.save(savedEvent4);

				log.info("Added initial DB data");
			}
		};
	}

}