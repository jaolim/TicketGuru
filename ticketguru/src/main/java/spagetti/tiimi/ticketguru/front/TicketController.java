package spagetti.tiimi.ticketguru.front;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class TicketController {
    private final TicketRepository tRepository;
    private final CostRepository cRepository;
    private final SaleRepository sRepository;
    private final EventRepository eRepository;

    public TicketController(TicketRepository tRepository, CostRepository cRepository, SaleRepository sRepository,
            EventRepository eRepository) {
        this.tRepository = tRepository;
        this.cRepository = cRepository;
        this.sRepository = sRepository;
        this.eRepository = eRepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/ticketpage")
    public String listTickets(Model model) {
        List<Ticket> tickets = tRepository.findAll();
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/ticket/add")
    public String addTicketPage(@RequestParam(required = false) Long eventId, Model model) {
        Ticket ticket = new Ticket();

        if (eventId != null) {
            ticket.setSelectedEventId(eventId);
        }

        model.addAttribute("ticket", ticket);

        List<Event> events = new ArrayList<>();
        eRepository.findAll().forEach(events::add);
        model.addAttribute("events", events);

        List<Cost> costs;
        if (ticket.getSelectedEventId() != null) {
            Event event = eRepository.findById(ticket.getSelectedEventId()).orElseThrow();
            costs = cRepository.findByEvent(event);
        } else {
            costs = List.of();
        }
        model.addAttribute("costs", costs);
        model.addAttribute("sales", sRepository.findAllByOrderByTimeDesc());

        return "ticket-add";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/ticket/add")
    public String saveNewTicket(@RequestParam Long costId, @RequestParam Long saleId) {
        Cost cost = cRepository.findById(costId).orElseThrow();
        Sale sale = sRepository.findById(saleId).orElseThrow();
        Optional<Event> event = eRepository.findById(cost.getEvent().getEventid());
        if (!event.isPresent()) {
            return "redirect:/ticketpage";
        } else if (event.get().getTotalTickets() >= event.get().getCapacity()) {
            return "redirect:/ticketpage";
        }

        event.get().setTotalTickets(event.get().getTotalTickets() + 1);
        Ticket ticket = new Ticket();
        ticket.setSale(sale);
        ticket.setCost(cost);
        ticket.setRedeemed(false);
        ticket.setRedeemedTime(null);
        ticket.setPrice(cost.getPrice());

        tRepository.save(ticket);

        Long id = tRepository.save(ticket).getTicketid();
        Optional<Ticket> saved = tRepository.findById(id);
        sale.setPrice(sale.getPrice() + saved.get().getPrice());
        sRepository.save(sale);
        saved.get().setTicketCode(
                Base64.getEncoder().encodeToString(((id.toString() + cost.getCostid()).getBytes())));

        tRepository.save(saved.get());
        return "redirect:/ticketpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/ticket/edit/{id}")
    public String editTicketPage(@PathVariable Long id,
            @RequestParam(required = false) Long eventId,
            Model model) {
        Ticket ticket = tRepository.findById(id).orElseThrow();

        if (eventId != null) {
            ticket.setSelectedEventId(eventId);
        } else if (ticket.getCost() != null) {
            ticket.setSelectedEventId(ticket.getCost().getEvent().getEventid());
        }

        model.addAttribute("ticket", ticket);

        List<Event> events = new ArrayList<>();
        eRepository.findAll().forEach(events::add);
        model.addAttribute("events", events);

        List<Cost> costs;
        if (ticket.getSelectedEventId() != null) {
            Event event = eRepository.findById(ticket.getSelectedEventId()).orElseThrow();
            costs = cRepository.findByEvent(event);
        } else {
            costs = List.of();
        }
        model.addAttribute("costs", costs);

        return "ticket-edit";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/ticket/edit/{id}")
    public String saveEditedTicket(@PathVariable Long id,
            @RequestParam Long costId) {
        Ticket ticket = tRepository.findById(id).orElseThrow();
        Cost cost = cRepository.findById(costId).orElseThrow();
        ticket.setCost(cost);
        tRepository.save(ticket);
        return "redirect:/ticketpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/ticket/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        tRepository.deleteById(id);
        return "redirect:/ticketpage";
    }
}
