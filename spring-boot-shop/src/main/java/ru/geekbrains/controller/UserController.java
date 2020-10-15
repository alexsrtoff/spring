package ru.geekbrains.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.persist.entity.Role;
import ru.geekbrains.persist.entity.User;
import ru.geekbrains.persist.repo.RoleRepository;
import ru.geekbrains.persist.repo.UserRepository;
import ru.geekbrains.persist.repo.UserService;
import ru.geekbrains.persist.repo.UserSpecification;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    public String allUsers(Model model,
                           @RequestParam(value = "name", required = false) String name,
                           @RequestParam(value = "email", required = false) String email,
                           @RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size) {
        logger.info("Filtering by name: {} email: {}", name, email);

        PageRequest pageRequest = PageRequest.of(page.orElse(1) - 1, size.orElse(5));

        Specification<User> spec = UserSpecification.trueLiteral();

        if (name != null && !name.isEmpty()) {
            spec = spec.and(UserSpecification.loginLike(name));
        }

        model.addAttribute("usersPage", userRepository.findAll(spec, pageRequest));
        return "users";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model) {
        User user = userRepository.findById(id).orElseThrow(NotFoundExeption::new);
        model.addAttribute("user", user);
        return "user";
    }


    @GetMapping("/new")
    public String newUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user";
    }

    @PostMapping("/update")
    public String updateUser(@Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user";
        }
        String password = userService.encodePassword(user.getPassword());
        user.setPassword(password);
        Role role = new Role();
        if (user.isAdmin()) {
            role = roleRepository.findByRole("ROLE_ADMIN");
            user.getRoles().add(role);
        }
        role = roleRepository.findByRole("ROLE_USER");
        user.getRoles().add(role);
        userRepository.save(user);
        return "redirect:/user";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Integer id) {
        userRepository.deleteById(id);
        return "redirect:/user";
    }
}
