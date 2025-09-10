package spagetti.tiimi.ticketguru.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

@Controller
public class HomeController {

    private TicketRepository repository;

    public HomeController(TicketRepository repository) {
        this.repository = repository;
    }

    public static Ticket testTicket = new Ticket("Testi");

    @GetMapping(value = {"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("ticket", testTicket);
        model.addAttribute("tickets", repository.findAll());
        return "index";
    }

}
