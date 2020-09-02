package ru.geekbrains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.persistance.User;
import ru.geekbrains.persistance.UserRepository;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String allUsers(Model model) throws SQLException {
        List<User> allUsers = userRepository.getAllUsers();
        model.addAttribute("users", allUsers);
        return "users";
    }

    @GetMapping("/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model) throws SQLException {
        User user = userRepository.findById(id);
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
    public String updateUser(@Valid User user, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "user";
        }

        // TODO реализовать проверку повторного ввода пароля.
        // TODO Использовать метод bindingResult.rejectValue();
        if (!user.getPassword().equals(user.getMatchingPassword())){
            bindingResult.rejectValue("matchingPassword", "matchingPassword.error", "Пароли не совпадают");
            System.out.println("not match");
            return "user";
        }
        if (user.getId() != null) {
            userRepository.update(user);
        } else {
            userRepository.insert(user);
        }
        return "redirect:/user";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Integer id) throws SQLException {
        userRepository.delete(id);
        return "redirect:/user";
    }
}
