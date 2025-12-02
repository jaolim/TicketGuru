package spagetti.tiimi.ticketguru.rest;

import java.util.List;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.Valid;
import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Views;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.Venue;
import spagetti.tiimi.ticketguru.domain.VenueRepository;

@CrossOrigin(originPatterns = "*")
@RestController
public class VenueRestController {

    private VenueRepository vrepository;

    public VenueRestController(VenueRepository vrepository) {
        this.vrepository = vrepository;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/venues")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public List<Venue> getAllVenues() {
        return (List<Venue>) vrepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/venues/{id}")
    @JsonView(Views.Public.class)
    @ResponseStatus(HttpStatus.OK)
    public Optional<Venue> getVenueById(@PathVariable Long id) {
        if (!vrepository.findById(id).isPresent()) {
            throw new NotFoundException("Venue does not exist");
        }
        return vrepository.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("/venues")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Venue createVenue(@Valid @RequestBody Venue venue) {
        if (venue.getName() == null || venue.getAddress() == null) {
            throw new BadRequestException("Missing name or address");
        }
        return vrepository.save(venue);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @DeleteMapping("venues/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.OK)
    public void deleteVenue(@PathVariable Long id) {
        if (!vrepository.findById(id).isPresent()) {
            throw new NotFoundException("Venue does not exist");
        }
        vrepository.deleteById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PutMapping("venues/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Venue> editVenue(@PathVariable Long id, @Valid @RequestBody Venue updatedVenue) {
        if (!vrepository.findById(id).isPresent()) {
            throw new NotFoundException("Venue does not exist");
        } else if (updatedVenue.getName() == null || updatedVenue.getAddress() == null) {
            throw new BadRequestException("Missing name or address");
        }
        return vrepository.findById(id)
                .map(venue -> {
                    venue.setName(updatedVenue.getName());
                    venue.setAddress(updatedVenue.getAddress());
                    return vrepository.save(venue);
                });
    }

}
