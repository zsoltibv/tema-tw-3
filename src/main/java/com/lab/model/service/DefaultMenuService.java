package com.lab.model.service;

import com.lab.model.util.Icon;
import com.lab.model.util.MenuItem;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class DefaultMenuService implements MenuService {

    @Override
    public List<MenuItem> buildMenu(Authentication authentication) {
        List<MenuItem> menu = new ArrayList<>();

        menu.add(createMenuItem("Home", "/home", Icon.HOME, Icon.IconColor.INDIGO));

        if (userHasRole(authentication, "MANAGE_ACCOUNTS")) {
            menu.add(createMenuItem("Roles", "/admin/roles", Icon.ROLE, Icon.IconColor.INDIGO));
        }
        menu.add(createMenuItem("Calendar", "/calendar", Icon.CALENDAR, Icon.IconColor.INDIGO));

        if(userHasRole(authentication, "APPROVE_DAYS_OFF_REQUEST")){
            menu.add(createMenuItem("Requests", "/leave-requests", Icon.ROLE, Icon.IconColor.INDIGO));
        }

        return menu;
    }

    private MenuItem createMenuItem(String name, String url, Icon icon, Icon.IconColor color) {
        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setUrl(url);

        icon.setColor(color);
        menuItem.setIcon(icon);

        return menuItem;
    }

    private boolean userHasRole(Authentication authentication, String targetRole) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream().anyMatch(authority -> authority.getAuthority().equals(targetRole));
    }
}
