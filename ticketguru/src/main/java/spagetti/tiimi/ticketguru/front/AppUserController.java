package spagetti.tiimi.ticketguru.front;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import java.util.Map;
import java.util.Optional;

import spagetti.tiimi.ticketguru.domain.AppUser;
import spagetti.tiimi.ticketguru.domain.AppUserRepository;

@Controller
public class AppUserController {

    private final AppUserRepository uRepository;

    public AppUserController(AppUserRepository uRepository) {
        this.uRepository = uRepository;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userpage")
    public String getUserList(Model model) {
        model.addAttribute("users", uRepository.findAll());
        return "users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/edit/{id}")
    public String editUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<AppUser> userOpt = uRepository.findById(id);
        if (!userOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found with id " + id);
            return "redirect:/userpage";
        }

        AppUser appUser = userOpt.get();
        model.addAttribute("item", appUser);
        model.addAttribute("title", "Edit User");
        return "user-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user/edit/{id}")
    public String saveUser(@PathVariable Long id, @RequestParam Map<String, String> params,
            RedirectAttributes redirectAttributes) {
        Optional<AppUser> userOpt = uRepository.findById(id);
        if (!userOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found with id " + id);
            return "redirect:/userpage";
        }

        AppUser user = userOpt.get();
        user.setUsername(params.get("username"));
        user.setFirstname(params.get("firstname"));
        user.setLastname(params.get("lastname"));
        user.setUserRole(params.get("userRole"));

        uRepository.save(user);
        return "redirect:/userpage";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<AppUser> userOpt = uRepository.findById(id);
        if (!userOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found with id " + id);
            return "redirect:/userpage";
        }

        uRepository.deleteById(id);
        return "redirect:/userpage";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new AppUser());
        model.addAttribute("title", "Add User");
        return "user-add";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/user/add")
    public String saveNewUser(@ModelAttribute AppUser user, RedirectAttributes redirectAttributes) {
         // Check for username uniqueness
        if (uRepository.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Username " + user.getUsername() + " is already taken.");
            return "redirect:/user/add";
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        uRepository.save(user);
        return "redirect:/userpage";
    }
}
