package com.lab.model.controller;

import com.lab.model.config.util.Status;
import com.lab.model.repository.UserRepository;
import com.lab.model.service.MenuService;
import com.lab.model.util.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    UserRepository userRepository;
    private MenuService menuService;

    @Autowired
    public HomeController(UserRepository userRepository, MenuService menuService) {
        this.userRepository = userRepository;
        this.menuService = menuService;
    }

    @GetMapping()
    public String open(Model model, Authentication authentication){
        List<MenuItem> menu = menuService.buildMenu(authentication);
        model.addAttribute("menuItems", menu);
        model.addAttribute("statusValues", Status.values());

        return "home";
    }
}
