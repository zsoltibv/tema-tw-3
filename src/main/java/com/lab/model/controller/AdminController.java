package com.lab.model.controller;

import com.lab.model.config.util.Status;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.RoleRepository;
import com.lab.model.service.MenuService;
import com.lab.model.service.UserService;
import com.lab.model.util.Icon;
import com.lab.model.util.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private MenuService menuService;
    @Autowired
    private RoleRepository roleRepository;
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(UserService userService, MenuService menuService) {
        this.userService = userService;
        this.menuService = menuService;
    }

    @GetMapping()
    //@PreAuthorize("hasAnyRole('MANAGE_ACCOUNTS')")
    public String open(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<MenuItem> menu = menuService.buildMenu(authentication);
        model.addAttribute("menuItems", menu);
        model.addAttribute("statusValues", Status.values());

        model.addAttribute("menuItems", menu);
        return "admin/dashboard";
    }

    @GetMapping
    @RequestMapping("/roles")
    public String openRoles(Model model, @RequestParam(defaultValue = "0") int page){
        final int PAGE_SIZE = 3; /* At the moment there is no way to change size from GUI, so this is hardcoded */
        Page<UserEntity> userPage  = userService.findAll(page, PAGE_SIZE);
        List<UserEntity> employees = userPage.getContent();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<MenuItem> menu = menuService.buildMenu(authentication);

        /* IMPORTANT!
        *  FontAwesome finally works in the frontend, and I don't know why. Therefore, the below implementation is
        * not necessary anymore, neither is the one from the menu. I will not refactor the code, both because:
        *  - I am proud of this mess.
        *  - It's working now, but maybe it'll stop working again in the future. :)))
        * */
        HashMap<String, Icon> icons = new HashMap<>();
        icons.put("ARROW_LEFT", Icon.ARROW_LEFT);
        icons.put("ARROW_RIGHT", Icon.ARROW_RIGHT);
        icons.put("DOUBLE_ARROW_LEFT", Icon.DOUBLE_ARROW_LEFT);
        icons.put("DOUBLE_ARROW_RIGHT", Icon.DOUBLE_ARROW_RIGHT);

        //menu
        model.addAttribute("menuItems", menu);
        model.addAttribute("statusValues", Status.values());
        model.addAttribute("employees", employees);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", new UserEntity());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        // model.addAttribute("icons", icons); /* This is optional now, and it will work ony if you modify the frontend too */
        return "admin/roles";
    }

    /* PatchMapping */
    @PatchMapping("/roles") /* Partial modification */
    public String updateRole(@RequestParam("userId") Long userId, Model model, @RequestParam(value = "assignEmployee", required = false) Long assignEmployeeId, @RequestParam(value = "roles", required = false) Long... roles) {
        logger.info("update user called");
        UserEntity user = userService.findById(userId);

        if(roles != null) {
            user.getRoles().clear();
            for (Long roleId : roles) {
                RoleEntity role = roleRepository.findById(roleId).orElseThrow(); // Add appropriate error handling
                user.addRole(role);
                userService.update(user);
            }
        }

        if (assignEmployeeId != null) {
            UserEntity assignEmployee = userService.findById(assignEmployeeId);
            user.setUser(assignEmployee);
            userService.update(user);
        }

        return "redirect:/admin/roles";
    }
}
