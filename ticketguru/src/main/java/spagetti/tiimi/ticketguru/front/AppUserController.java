package spagetti.tiimi.ticketguru.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import java.util.Map;
import spagetti.tiimi.ticketguru.Exception.NotFoundException;

import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;

@Controller
public class AppUserController {
    
    private final AppUserRepository uRepository;

    public AppUserController(AppUserRepository uRepository) {
        this.uRepository = uRepository;
    }

    @GetMapping("/userpage")
    public String getUserList(Model model) {
        model.addAttribute("users", uRepository.findAll());
        return "users";
    }

    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        AppUser user = uRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        model.addAttribute("item", user);
        model.addAttribute("title", "Edit User");
        return "user-edit";
    }

    @PostMapping("/user/edit/{id}")
    public String saveUser(@PathVariable Long id, @RequestParam Map<String, String> params) {
        AppUser user = uRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setUsername(params.get("username"));
        user.setFirstname(params.get("firstname"));
        user.setLastname(params.get("lastname"));
        user.setUserRole(params.get("userRole"));

        uRepository.save(user);
        return "redirect:/userpage";
    }

    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        AppUser user = uRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        uRepository.delete(user);
        return "redirect:/userpage";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new AppUser());
        model.addAttribute("title", "Add User");
        return "user-add";
    }

    @PostMapping("/user/add")
    public String saveNewUser(@ModelAttribute AppUser user) {
        uRepository.save(user);
        return "redirect:/userpage";
    }
}
