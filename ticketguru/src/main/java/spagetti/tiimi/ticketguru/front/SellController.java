package spagetti.tiimi.ticketguru.front;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;
import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;

@Controller
public class SellController {

    private EventRepository eRepository;
    private SaleRepository sRepository;
    private CostRepository cRepository;
    private TicketRepository tRepository;
    private AppUserRepository auRepository;

    public SellController(EventRepository eRepository, SaleRepository sRepository, CostRepository cRepository,
            TicketRepository tRepository, AppUserRepository auRepository) {
        this.eRepository = eRepository;
        this.sRepository = sRepository;
        this.cRepository = cRepository;
        this.tRepository = tRepository;
        this.auRepository = auRepository;
    }

    // Sell endpoint without tickets parameter
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = { "/sell" }, params = "!tickets")
    public String getSell(Model model) {
        model.addAttribute("events", eRepository.findAllByOrderByDateAsc());
        model.addAttribute("costs", cRepository.findAll());
        return "sell-ticket";
    }

    // Get specific sale by id
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping(value = { "/sell/sale" }, params = "id")
    public String getSale(@RequestParam String id, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            return "redirect:" + referer;
        }
        try {
            Long saleid = Long.valueOf(id);
            model.addAttribute("events", eRepository.findAll());
            model.addAttribute("costs", cRepository.findAll());
            model.addAttribute("events", eRepository.findAll());
            model.addAttribute("costs", cRepository.findAll());
            Optional<Sale> sale = sRepository.findById(saleid);
            if (!sale.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sale id " + saleid + " does not exist");
                return "redirect:" + referer;
            }
            redirectAttributes.addFlashAttribute("sale", sale.get());
            redirectAttributes.addFlashAttribute("tickets", tRepository.findBySale(sRepository.findById(saleid).get()));
            redirectAttributes.addFlashAttribute("successMessage", "Sale id " + saleid + " fetched");
            return "redirect:" + referer;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error parsing ID");
            return "redirect:" + referer;
        }
    }

    // parse tickets parameter and create tickets and sales accordingly
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sell")
    public String sellTickets(@RequestParam String tickets, Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes, Authentication authentication) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Referer address not found");
            return "redirect:/sell";
        }
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
                    if (ticketsMapped.containsKey(costid)) {
                        ticketsMapped.put(costid, (ticketsMapped.get(costid) + amount));
                    } else {
                        ticketsMapped.put(costid, amount);
                    }
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

        Sale sale = sRepository.save(new Sale(user, now, rounded));
        Long saleid = sale.getSaleid();
        for (Map.Entry<Long, Integer> entry : ticketsMapped.entrySet()) {
            Long k = entry.getKey();
            Integer v = entry.getValue();
            Event event = cRepository.findById(k).get().getEvent();
            Long totalTickets = tRepository.countByCost_Event_Eventid(event.getEventid());
            if (totalTickets + v > event.getCapacity()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sale has not been created: capacity exceeded");
                return "redirect:/sell/delete/" + saleid;
            }
            totalTickets += v;
            for (int i = 0; i < v; i++) {
                Ticket ticket = new Ticket(cRepository.findById(k).get(), sale);
                ticket.setPrice(ticket.getCost().getPrice());
                Ticket saved = tRepository.save(ticket);
                saved.setTicketCode(Base64.getEncoder().encodeToString((saved.getTicketid().toString()
                        + saved.getCost().getCostid()).getBytes()));
                tRepository.save(saved);
            }
        }

        for (Map.Entry<Long, Integer> entry : ticketsMapped.entrySet()) {
            Long k = entry.getKey();
            Event event = cRepository.findById(k).get().getEvent();
            Long totalTickets = tRepository.countByCost_Event_Eventid(event.getEventid());
            event.setTotalTickets(totalTickets);
            eRepository.save(event);
        }

        model.addAttribute("events", eRepository.findAll());
        model.addAttribute("costs", cRepository.findAll());
        redirectAttributes.addFlashAttribute("sale", sRepository.findById(saleid).get());
        redirectAttributes.addFlashAttribute("tickets", tRepository.findBySale(sRepository.findById(saleid).get()));
        redirectAttributes.addFlashAttribute("successMessage", "New sale created");
        return "redirect:" + referer;
    }

    // Creates a special sale for tickets to be sold at the door
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sell/doorSale")
    public String doorSell(@RequestParam String tickets, Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes, Authentication authentication) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Referer address not found");
            return "redirect:/sell";
        }
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
                    if (ticketsMapped.containsKey(costid)) {
                        ticketsMapped.put(costid, (ticketsMapped.get(costid) + amount));
                    } else {
                        ticketsMapped.put(costid, amount);
                    }
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

        Sale sale = sRepository.save(new Sale(user, now, rounded));
        sale.setDoorSale(true);
        Long saleid = sale.getSaleid();
        for (Map.Entry<Long, Integer> entry : ticketsMapped.entrySet()) {
            Long k = entry.getKey();
            Integer v = entry.getValue();
            Event event = cRepository.findById(k).get().getEvent();
            Long totalTickets = tRepository.countByCost_Event_Eventid(event.getEventid());
            if (totalTickets + v > event.getCapacity()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Sale has not been created: capacity exceeded");
                return "redirect:/sell/delete/" + saleid;
            }
            totalTickets += v;
            for (int i = 0; i < v; i++) {
                Ticket ticket = new Ticket(cRepository.findById(k).get(), sale);
                ticket.setPrice(ticket.getCost().getPrice());
                Ticket saved = tRepository.save(ticket);
                saved.setTicketCode(Base64.getEncoder().encodeToString((saved.getTicketid().toString()
                        + saved.getCost().getCostid()).getBytes()));
                tRepository.save(saved);
            }
        }

        for (Map.Entry<Long, Integer> entry : ticketsMapped.entrySet()) {
            Long k = entry.getKey();
            Event event = cRepository.findById(k).get().getEvent();
            Long totalTickets = tRepository.countByCost_Event_Eventid(event.getEventid());
            event.setTotalTickets(totalTickets);
            eRepository.save(event);
        }

        model.addAttribute("events", eRepository.findAll());
        model.addAttribute("costs", cRepository.findAll());
        redirectAttributes.addFlashAttribute("sale", sRepository.findById(saleid).get());
        redirectAttributes.addFlashAttribute("tickets", tRepository.findBySale(sRepository.findById(saleid).get()));
        redirectAttributes.addFlashAttribute("successMessage", "New sale created");
        return "redirect:" + referer;
    }

    // deletes a sale
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/sell/delete/{id}")
    public String deleteSale(@PathVariable Long id, Model model, HttpServletRequest request,
            RedirectAttributes redirectAttributes, Authentication authentication,
            @ModelAttribute("errorMessage") String errorMessage) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Referer address not found");
            return "redirect:/sell";
        }
        List<Event> events = eRepository.findDistinctByCosts_Tickets_Sale_Saleid(id);
        sRepository.deleteById(id);

        events.forEach(event -> {
            event.setTotalTickets(tRepository.countByCost_Event_Eventid(event.getEventid()));
            eRepository.save(event);
        });

        if (errorMessage != null && !errorMessage.isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Sale by the id of " +
                    id + " has been deleted");
        }

        return "redirect:" + referer;
    }

}
