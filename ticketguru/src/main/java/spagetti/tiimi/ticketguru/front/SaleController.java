package spagetti.tiimi.ticketguru.front;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;
import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

@Controller
public class SaleController {

    private EventRepository eRepository;
    private SaleRepository sRepository;
    private CostRepository cRepository;
    private TicketRepository tRepository;
    private AppUserRepository auRepository;

    public SaleController(EventRepository eRepository, SaleRepository sRepository, CostRepository cRepository,
            TicketRepository tRepository, AppUserRepository auRepository) {
        this.eRepository = eRepository;
        this.sRepository = sRepository;
        this.cRepository = cRepository;
        this.tRepository = tRepository;
        this.auRepository = auRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = { "/sell" }, params = "!tickets")
    public String getSell(Model model) {
        model.addAttribute("events", eRepository.findAll());
        model.addAttribute("costs", cRepository.findAll());
        return "sell-ticket";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sell")
    public String sellTickets(@RequestParam String tickets, Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes, Authentication authentication) {
        String referer = request.getHeader("Referer");
        AppUser user = auRepository.findByUsername(authentication.getName());
        if (tickets.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Add tickets to submit");
            return "redirect:" + referer;
        }
        HashMap<Long, Integer> ticketsMapped = new HashMap<>();
        double total = 0;
        try {
            String[] ticketListString = tickets.split(",");
            for (String ticket : ticketListString) {
                String[] split = ticket.split(":");
                if (split.length != 2) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error parsing input");
                    return "redirect:" + referer;
                }
                try {
                    Long costid = Long.valueOf(split[0]);
                    int amount = Integer.valueOf(split[1]);
                    Optional<Cost> cost = cRepository.findById(costid);
                    if (!cost.isPresent()) {
                        redirectAttributes.addFlashAttribute("errorMessage", "Invalid costid");
                        return "redirect:" + referer;
                    }
                    total += cost.get().getPrice() * amount;
                    ticketsMapped.put(costid, amount);
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Error parsing numbers");
                    return "redirect:" + referer;
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error parsing input");
            return "redirect:" + referer;
        }
        LocalDateTime now = LocalDateTime.now();
        double rounded = new BigDecimal(Double.toString(total)).setScale(2, RoundingMode.HALF_UP).doubleValue();
        Sale sale = new Sale(user, now, rounded);
        Long saleid = sRepository.save(sale).getSaleid();
        ticketsMapped.forEach((k, v) -> {
            Ticket ticket = new Ticket(cRepository.findById(k).get(), sale);
            Ticket saved = tRepository.save(ticket);
            saved.setTicketCode(Base64.getEncoder().encodeToString((saved.getTicketid().toString()
                    + saved.getCost().getCostid() + saved.getSale().getSaleid().toString()).getBytes()));
            tRepository.save(saved);
        });

        model.addAttribute("events", eRepository.findAll());
        model.addAttribute("costs", cRepository.findAll());
        redirectAttributes.addFlashAttribute("sale", sRepository.findById(saleid).get());
        redirectAttributes.addFlashAttribute("tickets", tRepository.findBySale(sRepository.findById(saleid).get()));
        redirectAttributes.addFlashAttribute("successMessage", "New sale created");
        return "redirect:" + referer;
    }

}
