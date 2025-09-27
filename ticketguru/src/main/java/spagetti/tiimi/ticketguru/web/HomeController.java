package spagetti.tiimi.ticketguru.web;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;


@Controller
public class HomeController {

    @Autowired
    private TicketRepository repository;
    @Autowired
    private EventRepository erepository;
    @Autowired
    private AppUserRepository urepository;
    @Autowired
    private CostRepository crepository;
    @Autowired
    private TicketTypeRepository trepository;



    public static Event testEvent = new Event("Testitapahtuma", "Testipaikka", LocalDateTime.now());
    public static AppUser testUser = new AppUser("Testi", "Esimerkki");
    public static TicketType testType = new TicketType("Lapsi");
    public static Cost testCost = new Cost(testType, 9.99, testEvent);
    public static Ticket testTicket = new Ticket("Testi", testCost);

    @GetMapping(value = {"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("ticket", testTicket);
        model.addAttribute("event", testEvent);
        model.addAttribute("user", testUser);
        model.addAttribute("tickets", repository.findAll());
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("users", urepository.findAll());
        model.addAttribute("cost", testCost);
        model.addAttribute("tickets", repository.findAll());
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("costs", crepository.findAll());
        return "index";
    }

}
