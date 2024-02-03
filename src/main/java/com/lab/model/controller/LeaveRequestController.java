package com.lab.model.controller;

import com.lab.model.config.util.Status;
import com.lab.model.model.DaysOff;
import com.lab.model.model.UserEntity;
import com.lab.model.service.MenuService;
import com.lab.model.service.UserService;
import com.lab.model.util.Icon;
import com.lab.model.util.MenuItem;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.lab.model.service.LeaveRequestService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/leave-requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final MenuService menuService;
    private final UserService userService;

    public LeaveRequestController(LeaveRequestService leaveRequestService, UserService userService, MenuService menuService) {
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

        //populate model
        if (authentication != null) {
            String username = authentication.getName();
            Long loggedInUserId = userService.findByEmail(username).get().getId();
            List<UserEntity> userList = userService.getAllUsersWithSameSuperior(loggedInUserId);
            model.addAttribute("userList", userList);
        }

        return "common/leave-requests";
    }

    @PutMapping("/update/{userId}")
    public String updateLeaveStatus(@PathVariable Long userId,
                                    @RequestParam Long leaveRequestId,
                                    @RequestParam String status,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        // Retrieve the leave request from your service or repository
        leaveRequestService.updateLeaveStatusAndDates(leaveRequestId, status, startDate, endDate);

        // Redirect back to the original page
        return "redirect:/leave-requests";  // Update this URL to match your original page URL
    }
}
