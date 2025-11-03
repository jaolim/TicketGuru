package spagetti.tiimi.ticketguru.web;

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

import spagetti.tiimi.ticketguru.Views;
import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;

@CrossOrigin(origins = "*")
@RestController
public class AppUserRestController {

    private AppUserRepository urepository;

    public AppUserRestController(AppUserRepository urepository) {
        this.urepository = urepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    @JsonView(Views.Limited.class)
    @ResponseStatus(HttpStatus.OK)
    public List<AppUser> getAllUsers() {
        return (List<AppUser>) urepository.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{id}")
    @JsonView(Views.Limited.class)
    @ResponseStatus(HttpStatus.OK)
    public Optional<AppUser> getUserById(@PathVariable Long id) {
        Optional<AppUser> user = urepository.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException("User does not exist");
        }
        return urepository.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/users")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser createUser(@RequestBody AppUser user) {
        if (user.getFirstname() == null || user.getLastname() == null) {
            throw new BadRequestException("Missing required fields: firstname or lastname");
        }
        return urepository.save(user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/users/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        if (!urepository.findById(id).isPresent()) {
            throw new NotFoundException("User does not exist");
        }
        urepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("users/{id}")
    @JsonView(Views.Internal.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<AppUser> editUser(@PathVariable Long id, @RequestBody AppUser updatedUser) {
        if (updatedUser.getFirstname() == null || updatedUser.getLastname() == null) {
            throw new BadRequestException("Missing required fields: firstname or lastname");
        } else if (!urepository.findById(id).isPresent()) {
            throw new NotFoundException("User does not exist");
        }
        return urepository.findById(id)
                .map(user -> {
                    user.setFirstname(updatedUser.getFirstname());
                    user.setLastname(updatedUser.getLastname());
                    return urepository.save(user);
                });
    }

}
