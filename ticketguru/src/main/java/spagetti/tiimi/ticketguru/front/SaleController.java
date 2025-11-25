package spagetti.tiimi.ticketguru.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;

@Controller
public class SaleController {

    private final SaleRepository sRepository;
    private final AppUserRepository uRepository;
    private final TicketRepository tRepository;

    public SaleController(SaleRepository sRepository,
                          AppUserRepository uRepository,
                          TicketRepository tRepository) {
        this.sRepository = sRepository;
        this.uRepository = uRepository;
        this.tRepository = tRepository;
    }

    @GetMapping("/salepage")
    public String getSales(Model model) {
        model.addAttribute("sales", sRepository.findAll());
        return "sales";
    }

    @GetMapping("/sale/edit/{id}")
    public String editSale(@PathVariable Long id, Model model) {
        Sale sale = sRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        model.addAttribute("sale", sale);
        model.addAttribute("users", uRepository.findAll());
        model.addAttribute("allTickets", tRepository.findAll());

        model.addAttribute("title", "Edit Sale");
        model.addAttribute("entity", "sale");

        return "sale-edit";
    }

    @PostMapping("/sale/edit/{id}")
    public String saveSale(@PathVariable Long id,
                           @RequestParam Long userId,
                           @RequestParam Double price) {

        Sale sale = sRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        AppUser user = uRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        sale.setUser(user);
        sale.setPrice(price);

        sRepository.save(sale);

        return "redirect:/salepage";
    }

    @PostMapping("/sale/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        Sale sale = sRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found"));

        sRepository.delete(sale);
        return "redirect:/salespage";
    }

    @GetMapping("/sale/add")
    public String addSaleForm(Model model) {
        model.addAttribute("users", uRepository.findAll());
        model.addAttribute("allTickets", tRepository.findAll());
        return "sale-add"; 
    }

    @PostMapping("/sale/add")
    public String addSale(@RequestParam Long userId,
                          @RequestParam Double price) {

        AppUser user = uRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Sale sale = new Sale();
        sale.setUser(user);
        sale.setPrice(price);
        sale.setTime(LocalDateTime.now());


        sRepository.save(sale);

        return "redirect:/salepage";
    }
}
