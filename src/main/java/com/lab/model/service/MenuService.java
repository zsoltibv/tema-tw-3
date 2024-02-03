package com.lab.model.service;
import org.springframework.security.core.Authentication;

import com.lab.model.util.MenuItem;
import java.util.List;

public interface MenuService {
    List<MenuItem> buildMenu(Authentication authentication);
}
