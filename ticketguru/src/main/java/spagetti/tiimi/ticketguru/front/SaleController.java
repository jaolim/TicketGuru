package spagetti.tiimi.ticketguru.front;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;

@Controller
public class SaleController {

    private final SaleRepository sRepository;
    private final AppUserRepository uRepository;
    private final TicketRepository tRepository;
    private final EventRepository eRepository;

    public SaleController(SaleRepository sRepository,
            AppUserRepository uRepository,
            TicketRepository tRepository, EventRepository eRepository) {
        this.eRepository = eRepository;
        this.sRepository = sRepository;
        this.uRepository = uRepository;
        this.tRepository = tRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/salepage")
    public String getSales(Model model) {
        model.addAttribute("sales", sRepository.findAll());
        return "sales";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
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

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/sale/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        Sale sale = sRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sale not found"));
        List<Event> events = eRepository.findDistinctByCosts_Tickets_Sale_Saleid(id);
        sRepository.deleteById(id);
        events.forEach(event -> {
            event.setTotalTickets(tRepository.countByCost_Event_Eventid(event.getEventid()));
            eRepository.save(event);
        });

        sRepository.delete(sale);
        return "redirect:/salepage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sale/add")
    public String addSaleForm(Model model) {
        model.addAttribute("users", uRepository.findAll());
        model.addAttribute("allTickets", tRepository.findAll());
        return "sale-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
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

    // Removes unredeemed tickets from a doorsale and calculates price and event
    // total tickets
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = { "/sale/check/door" })
    public String checkDoorsell(@RequestParam String saleid, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Referer not found: redirected to home");
            return "redirect:/";
        } else if (saleid == null || saleid.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid ID");
            return "redirect:" + referer;
        }
        try {
            Long id = Long.valueOf(saleid);
            Sale sale = sRepository.findById(id).get();
            if (!sale.getDoorSale()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sale by the id of " + id + " is not a door sale");
                return "redirect:" + referer;
            }
            List<Ticket> tickets = tRepository.findBySale(sale);
            Double total = 0.0;
            for (int i = 0; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);
                if (tickets.get(i).getRedeemed()) {
                    total += ticket.getPrice();
                } else {
                    tRepository.delete(ticket);
                }
            }
            sale.setPrice(total);
            List<Event> events = eRepository.findDistinctByCosts_Tickets_Sale_Saleid(id);
            sRepository.save(sale);
            events.forEach(event -> {
                event.setTotalTickets(tRepository.countByCost_Event_Eventid(event.getEventid()));
                eRepository.save(event);
            });
            redirectAttributes.addFlashAttribute("checkSale", sale);
            redirectAttributes.addFlashAttribute("successMessage", "Door sale has been checked");
            return "redirect:" + referer;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid ID");
            return "redirect:" + referer;
        }
    }
}
