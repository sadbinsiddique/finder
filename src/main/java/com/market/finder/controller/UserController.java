package com.market.finder.controller;

import com.market.finder.dao.UserRepository;
import com.market.finder.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Show all users
    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users/list";
    }

    // 2. Show the form to create a new user
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }

    // 3. Show the form to edit an existing user
    @GetMapping("/edit/{username}")
    public String showEditForm(@PathVariable String username, Model model) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username: " + username));
        model.addAttribute("user", user);
        return "users/form";
    }

    // 4. Save the user (Handles both Create and Update from the HTML form)
    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    // 5. Delete a user
    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) {
        if (userRepository.existsById(username)) {
            userRepository.deleteById(username);
        }
        return "redirect:/users";
    }
}