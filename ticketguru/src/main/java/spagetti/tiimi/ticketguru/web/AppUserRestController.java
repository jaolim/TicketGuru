package spagetti.tiimi.ticketguru.web;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;

@RestController
public class AppUserRestController {

    private AppUserRepository urepository;

    public AppUserRestController(AppUserRepository urepository) {
        this.urepository = urepository;
    }

    @GetMapping("/users")
    public List<AppUser> usersRest() {
        return (List<AppUser>) urepository.findAll();
    }

    @GetMapping("/users/{id}")
    public Optional<AppUser> getUser(@RequestParam Long id) {
        return urepository.findById(id);
    }

    @PostMapping("/users")
    public AppUser newUser(@RequestBody AppUser user) {
        return urepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        urepository.deleteById(id);
    }

    @PutMapping("users/{id}")
    public Optional<AppUser> editUser(@PathVariable Long id, @RequestBody AppUser updatedUser) {
        return urepository.findById(id)
                .map(user -> {
                    user.setFirstname(updatedUser.getFirstname());
                    user.setLastname(updatedUser.getLastname());
                    return urepository.save(user);
                });
    }

}
