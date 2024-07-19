package net.bughandlers.bugtracker.controller;

import net.bughandlers.bugtracker.model.User;
import net.bughandlers.bugtracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {


    @Autowired
    private UserService userService;

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> all = userService.getAll();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/create-new-admin")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.saveAdmin(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}