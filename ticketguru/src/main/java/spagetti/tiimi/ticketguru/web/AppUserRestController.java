package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.Exception.BadRequestException;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;
import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;

@RestController
public class AppUserRestController {

    private AppUserRepository urepository;

    public AppUserRestController(AppUserRepository urepository) {
        this.urepository = urepository;
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<AppUser> usersRest() {
        return (List<AppUser>) urepository.findAll();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<AppUser> getUser(@PathVariable Long id) {
        Optional<AppUser> user = urepository.findById(id);
        if (!user.isPresent()) {
            throw new NotFoundException("User does not exist");
        }
        return urepository.findById(id);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUser newUser(@RequestBody AppUser user) {
        if (user.getFirstname() == null || user.getLastname() == null) {
            throw new BadRequestException("Missing required fields: firstname or lastname");
        }
        return urepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable Long id) {
        if (!urepository.findById(id).isPresent()) {
            throw new NotFoundException("User does not exist");
        }
        urepository.deleteById(id);
    }

    @PutMapping("users/{id}")
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
