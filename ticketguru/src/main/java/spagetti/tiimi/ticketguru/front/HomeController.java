package spagetti.tiimi.ticketguru.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;



@CrossOrigin(originPatterns = "*")
@Controller
public class HomeController {



    @GetMapping(value = { "/", "/index" })
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping(value = { "/client" })
    public String getClient(Model model) {
        return "ticket-client";
    }

}
