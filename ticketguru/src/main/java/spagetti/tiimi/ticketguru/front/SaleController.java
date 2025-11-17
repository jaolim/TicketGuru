package spagetti.tiimi.ticketguru.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.SaleRepository;

@Controller
public class SaleController {

    private EventRepository eRepository;
    private SaleRepository sRepository;
    private CostRepository cRepository;
    
    public SaleController(EventRepository eRepository, SaleRepository sRepository, CostRepository cRepository) {
        this.eRepository = eRepository;
        this.sRepository = sRepository;
        this.cRepository = cRepository;
    }

    @GetMapping(value = { "/sellticket" })
    public String getSell(Model model) {
        model.addAttribute("events", eRepository.findAll());
        model.addAttribute("costs", cRepository.findAll());
        return "sell-ticket";
    }



}
