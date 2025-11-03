package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import spagetti.tiimi.ticketguru.Views;
import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "*")
@RestController
public class TicketTypeRestController {

    private TicketTypeRepository trepository;

    public TicketTypeRestController(TicketTypeRepository trepository) {
        this.trepository = trepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickettypes")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public List<TicketType> getAllTicketTypes() {
        return (List<TicketType>) trepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/tickettypes/{id}")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public Optional<TicketType> getTicketTypeById(@PathVariable Long id) {
        if (!trepository.findById(id).isPresent()) {
            throw new NotFoundException("TicketType does not exist");
        }
        return trepository.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/tickettypes")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public TicketType createTicketType(@RequestBody TicketType ticketType) {
        if (ticketType.getTypeid() != null) {
            throw new BadRequestException("Do not include typeid");
        }
        if (ticketType.getName() == null || ticketType.getName().isEmpty()) {
            throw new BadRequestException("TicketType name is missing");
        }
        return trepository.save(ticketType);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("tickettypes/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.OK)
    public void deleteTicketType(@PathVariable Long id) {
        if (!trepository.findById(id).isPresent()) {
            throw new NotFoundException("TicketType does not exist");
        }
        trepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("tickettypes/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<TicketType> editTicketType(@PathVariable Long id, @RequestBody TicketType updatedTicketType) {
        if (!trepository.findById(id).isPresent()) {
            throw new NotFoundException("TicketType does not exist");
        } else if (updatedTicketType.getName().isEmpty()) {
            throw new BadRequestException("TicketType name is missing");
        }
        return trepository.findById(id)
                .map(ticketType -> {
                    ticketType.setName(updatedTicketType.getName());
                    ticketType.setNote(updatedTicketType.getNote());
                    return trepository.save(ticketType);
                });
    }
}
