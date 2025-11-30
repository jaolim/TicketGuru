package spagetti.tiimi.ticketguru.front;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;

@Controller
public class EventController {
    private final EventRepository eRepository;
    private final VenueRepository vRepository;
    private final TicketRepository tRepository;
    private final CostRepository cRepository;

    public EventController(EventRepository eRepository, VenueRepository vRepository, TicketRepository tRepository,
            CostRepository cRepository) {
        this.eRepository = eRepository;
        this.vRepository = vRepository;
        this.tRepository = tRepository;
        this.cRepository = cRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/eventpage")
    public String getEventList(Model model) {
        model.addAttribute("events", eRepository.findAll());
        return "events";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/event/{id}")
    public String getEventDetails(@PathVariable String id, Model model, RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Referer not found: redirecting to home");
            return "redirect:/";
        }
        try {
            Long eventid = Long.valueOf(id);
            Optional<Event> event = eRepository.findById(eventid);
            if (!event.isPresent()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Event by the id of " + id + " does not exist");
                return "redirect:" + referer;
            }
            List<Ticket> tickets = tRepository.findByCost_Event_Eventid(eventid);
            List<Cost> costs = cRepository.findByEvent(event.get());
            List<String> costsTotal = new ArrayList<>();
            costs.forEach(cost -> {
                Long count = tRepository.countByCost_Costid(cost.getCostid());
                costsTotal.add(cost.getTicketType().getName() + " - " + cost.getPrice() + "€ - " + count + " units");
            });
            model.addAttribute("tickets", tickets);
            model.addAttribute("event", event.get());
            int redeemedCount = 0;
            double totalPrice = 0;
            for (int i = 0; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);
                if (ticket.getRedeemed()) {
                    redeemedCount++;
                }
                String costName = ticket.getCost().getTicketType().getName();
                totalPrice += ticket.getPrice();
            }
            model.addAttribute("costs", costsTotal);
            model.addAttribute("redeemedCount", redeemedCount);
            model.addAttribute("totalPrice", totalPrice);
            return "event-details";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error parsing ID");
            return "redirect:/eventpage";
        }
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/event/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Event> eventOpt = eRepository.findById(id);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event by the id of " + id + " does not exist");
            return "redirect:/eventpage";
        }

        Event event = eventOpt.get();
        model.addAttribute("item", event);
        model.addAttribute("venues", vRepository.findAll());
        model.addAttribute("title", "Edit Event");
        model.addAttribute("entity", "event");

        return "event-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/edit/{id}")
    public String saveEvent(@PathVariable Long id,
            @RequestParam Map<String, String> allParams, RedirectAttributes redirectAttributes) {

        Optional<Event> eventOpt = eRepository.findById(id);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event by the id of " + id + " does not exist");
            return "redirect:/eventpage";
        }

        Event event = eventOpt.get();
        event.setName(allParams.get("name"));
        event.setCapacity(Integer.parseInt(allParams.get("capacity")));
        event.setDate(LocalDateTime.parse(allParams.get("date")));

        Long venueId = Long.parseLong(allParams.get("venueId"));

        Optional<Venue> venueOpt = vRepository.findById(venueId);
        if (!venueOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Venue by the id of " + venueId + " does not exist");
            return "redirect:/eventpage";
        }
        event.setVenue(venueOpt.get());

        eRepository.save(event);

        return "redirect:/eventpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Event> eventOpt = eRepository.findById(id);
        if (!eventOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Event by the id of " + id + " does not exist");
            return "redirect:/eventpage";
        }

        eRepository.deleteById(id);
        return "redirect:/eventpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/event/add")
    public String addEventForm(Model model) {

        model.addAttribute("venues", vRepository.findAll());
        return "event-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/event/add")
    public String addEvent(
            @RequestParam String name,
            @RequestParam String date,
            @RequestParam Long venueId,
            @RequestParam Integer capacity, RedirectAttributes redirectAttributes) {

        Event event = new Event();
        event.setName(name);
        event.setCapacity(capacity);
        event.setDate(LocalDateTime.parse(date));

        Optional<Venue> venueOpt = vRepository.findById(venueId);
        if (!venueOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Venue by the id of " + venueId + " does not exist");
            return "redirect:/event/add";
        }
        event.setVenue(venueOpt.get());

        eRepository.save(event);

        return "redirect:/eventpage";
    }

}
