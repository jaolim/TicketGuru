package spagetti.tiimi.ticketguru.front;

import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import spagetti.tiimi.ticketguru.domain.Cost;
import spagetti.tiimi.ticketguru.domain.CostRepository;
import spagetti.tiimi.ticketguru.domain.Event;
import spagetti.tiimi.ticketguru.domain.EventRepository;
import spagetti.tiimi.ticketguru.domain.Sale;
import spagetti.tiimi.ticketguru.domain.SaleRepository;
import spagetti.tiimi.ticketguru.domain.Ticket;
import spagetti.tiimi.ticketguru.domain.TicketRepository;
import spagetti.tiimi.ticketguru.Exception.BadRequestException;
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
    public String saveNewTicket(@RequestParam Long costId, @RequestParam Long saleId,
            RedirectAttributes redirectAttributes) {

        Cost cost = cRepository.findById(costId).orElseThrow();
        Sale sale = sRepository.findById(saleId).orElseThrow();

        Optional<Event> event = eRepository.findById(cost.getEvent().getEventid());
        if (!event.isPresent()) {
            return "redirect:/ticketpage";
        } else if (event.get().getTotalTickets() >= event.get().getCapacity()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Capacity exceeded");
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
                Base64.getEncoder().encodeToString(((id.toString() + costId.toString()).getBytes())));

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
            @RequestParam Long costId, RedirectAttributes redirectAttributes) {
        Ticket ticket = tRepository.findById(id).orElseThrow();
        Cost cost = cRepository.findById(costId).orElseThrow();
        Event oldEvent = ticket.getCost().getEvent();
        Event newEvent = cost.getEvent();
        Sale sale = ticket.getSale();
        sale.setPrice(sale.getPrice() - ticket.getPrice() + cost.getPrice());
        ticket.setPrice(cost.getPrice());
        if (oldEvent.getEventid() != newEvent.getEventid()) {
            if (newEvent.getCapacity() <= newEvent.getTotalTickets()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Capacity exceeded");
                return "redirect:/ticketpage";
            }
            oldEvent.setTotalTickets(oldEvent.getTotalTickets() - 1);
            newEvent.setTotalTickets(newEvent.getTotalTickets() + 1);
            eRepository.save(oldEvent);
            eRepository.save(newEvent);
        }
        ticket.setCost(cost);
        ticket.setTicketCode(Base64.getEncoder().encodeToString(((id.toString() + costId.toString()).getBytes())));
        tRepository.save(ticket);

        return "redirect:/ticketpage";
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/ticket/delete/{id}")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Ticket> ticket = tRepository.findById(id);
        if (!ticket.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket by the id of " + id + " does not exist");
            return "redirect:/ticketpage";
        }
        Sale sale = ticket.get().getSale();
        if (sale != null) {
            sale.setPrice(sale.getPrice() - ticket.get().getPrice());
            sRepository.save(sale);
        }

        Event event = ticket.get().getCost().getEvent();
        event.setTotalTickets(event.getTotalTickets() - 1);
        eRepository.save(event);
        tRepository.deleteById(id);
        return "redirect:/ticketpage";
    }

    //mark ticket as redeemed based with ticketCode
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/ticket/check")
    public String checkTicket(@RequestParam String code, HttpServletRequest request,
            RedirectAttributes redirectAttributes, Model model) {
        String referer = request.getHeader("Referer");
        if (referer == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Referer not found: redirected to home");
            return "redirect:/";
        }
        if (code.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket code missing");
            return "redirect:" + referer;
        }
        String trimmed = code.trim();
        Optional<Ticket> ticket = tRepository.findByTicketCode(trimmed);
        if (!ticket.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket by the code of " + trimmed + " does not exist");
            return "redirect:" + referer;
        } else if (ticket.get().getRedeemed()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket by the code of " + trimmed + " is already redeemed");
            redirectAttributes.addFlashAttribute("redeemedTicket", ticket);
            return "redirect:" + referer;
        }
        ticket.get().setRedeemed(true);
        ticket.get().setRedeemedTime(null);
        tRepository.save(ticket.get());
        redirectAttributes.addFlashAttribute("successMessage", "Ticket " + trimmed + " succesfully redeemed");
        redirectAttributes.addFlashAttribute("redeemedTicket", ticket);
        return "redirect:" + referer;
    }
}
