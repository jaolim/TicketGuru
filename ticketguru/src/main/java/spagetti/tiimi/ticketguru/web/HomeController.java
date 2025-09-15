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
import spagetti.tiimi.ticketguru.domain.TicketRepository;

@Controller
public class HomeController {

    @Autowired
    private TicketRepository repository;

    @Autowired
    private EventRepository erepository;

    @Autowired
    private CostRepository crepository;


    public static Event testEvent = new Event("Testitapahtuma", "Testipaikka", LocalDateTime.now());
    public static Cost testCost = new Cost("Lapsi", 9.99, testEvent);
    public static Ticket testTicket = new Ticket("Testi", testCost);

    @GetMapping(value = {"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("ticket", testTicket);
        model.addAttribute("event", testEvent);
        model.addAttribute("cost", testCost);
        model.addAttribute("tickets", repository.findAll());
        model.addAttribute("events", erepository.findAll());
        model.addAttribute("costs", crepository.findAll());
        return "index";
    }

}
