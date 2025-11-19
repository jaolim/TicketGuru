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
			TicketTypeRepository trepository) {

		return (args) -> {

			repository.deleteAll();
			crepository.deleteAll();
			trepository.deleteAll();
			srepository.deleteAll();
			erepository.deleteAll();
			urepository.deleteAll();
		};
	}

}