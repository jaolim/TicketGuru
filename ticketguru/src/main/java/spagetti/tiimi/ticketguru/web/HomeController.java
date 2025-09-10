package spagetti.tiimi.ticketguru.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import spagetti.tiimi.ticketguru.domain.Ticket;

@Controller
public class HomeController {

    public static Ticket testTicket = new Ticket("Testi");

    @GetMapping(value = {"/", "/index"})
    public String getIndex(Model model) {
        model.addAttribute("ticket", testTicket);
        return "index";
    }

}
