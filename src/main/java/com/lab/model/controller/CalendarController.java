package com.lab.model.controller;
import com.lab.model.config.util.Status;
import com.lab.model.model.DaysOff;
import com.lab.model.model.UserEntity;
import com.lab.model.service.LeaveRequestService;
import com.lab.model.service.MenuService;
import com.lab.model.service.UserService;
import com.lab.model.util.Icon;
import com.lab.model.util.MenuItem;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
    private LeaveRequestService leaveRequestService;
    private UserService userService;
    private MenuService menuService;

    @Autowired
    public CalendarController(LeaveRequestService leaveRequestService, UserService userService, MenuService menuService) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
        this.menuService = menuService;
    }

    @GetMapping
    public String open(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<MenuItem> menu = menuService.buildMenu(authentication);
        model.addAttribute("menuItems", menu);
        model.addAttribute("statusValues", Status.values());

        if (authentication != null) {
            String username = authentication.getName();
            Long loggedInUserId = userService.findByEmail(username).get().getId();
            List<DaysOff> daysOffList = leaveRequestService.getDaysOffForEmployeeId(loggedInUserId);
            model.addAttribute("user", userService.findByEmail(username).get());
            model.addAttribute("daysOffList", daysOffList);
        }

        return "employee/calendar";
    }

    @PostMapping("/addDayOff")
    public String addDayOff(@RequestParam("startDate") String startDate,
                            @RequestParam("endDate") String endDate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            UserEntity loggedInUser = userService.findByEmail(username).get();
            leaveRequestService.addDayOff(loggedInUser, startDate, endDate);
        }

        // Redirect to a page after successful submission
        return "redirect:/calendar"; // You can change the URL as needed
    }
}
