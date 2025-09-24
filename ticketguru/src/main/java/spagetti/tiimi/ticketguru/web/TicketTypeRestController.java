package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.domain.TicketType;
import spagetti.tiimi.ticketguru.domain.TicketTypeRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class TicketTypeRestController {

    private TicketTypeRepository trepository;

    public  TicketTypeRestController (TicketTypeRepository trepository) {
        this.trepository = trepository;
    }

    @GetMapping("/tickettypes")
    public List<TicketType> ticketTypesRest() {
        return (List<TicketType>) trepository.findAll();
    }

    @GetMapping("/tickettypes/{id}")
    public Optional<TicketType> getTicketTypeRest(@PathVariable Long id) {
        return trepository.findById(id);
    }

    @PostMapping("/tickettypes")
    public TicketType newTicketTypeRest(@RequestBody TicketType ticketType) {
        return trepository.save(ticketType);
    }
    
    @DeleteMapping("tickettypes/{id}")
    public void deleteTicketTypeRest(@PathVariable Long id) {
        trepository.deleteById(id);
    }

    @PutMapping("tickettypes/{id}")
    public Optional<TicketType> editTicketTypeRest(@PathVariable Long id, @RequestBody TicketType updatedTicketType) {
        return trepository.findById(id)
            .map(ticketType -> {
                ticketType.setName(updatedTicketType.getName());
                ticketType.setNote(updatedTicketType.getNote());
                return trepository.save(ticketType);
            });
    }
}
