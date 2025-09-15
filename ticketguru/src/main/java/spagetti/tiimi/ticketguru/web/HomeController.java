package spagetti.tiimi.ticketguru.web;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.User;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.domain.UserRepository;


@Controller
public class HomeController {

    @Autowired
    private TicketRepository repository;

    @Autowired
    private EventRepository erepository;

    @Autowired
    private UserRepository urepository;


    public static Ticket testTicket = new Ticket("Testi");
    public static Event testEvent = new Event("Testitapahtuma", "Testipaikka", LocalDateTime.now());
    public static User testUser = new User("Testi", "Esimerkki");

    @GetMapping(value = {"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("ticket", testTicket);
        model.addAttribute("event", testEvent);
        model.addAttribute("user", testUser);
        model.addAttribute("tickets", repository.findAll());
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("users", urepository.findAll());
        return "index";
    }

}
