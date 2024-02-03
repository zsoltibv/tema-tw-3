package com.lab.model.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Controller
public class CustomErrorController implements ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(WebRequest webRequest, Model model, HttpServletResponse response) {
        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        int status = response.getStatus();

        if (status == HttpServletResponse.SC_FORBIDDEN) {
            // Customize the model attributes or add custom logic here
            model.addAttribute("errorStatus", status);
            model.addAttribute("errorMessage", "Access Forbidden. You do not have the required role.");

            /* TODO: read from folder all the files */
            List<String> imageList = Arrays.asList("howling.png", "hello_kitty_house.png", "godzilla_watch.png");
            String randomImage = imageList.get(new Random().nextInt(imageList.size()));
            model.addAttribute("randomImage", randomImage);

            return "errors/403-forbidden"; // Thymeleaf template name for your custom forbidden page
        }

        // Handle other error scenarios or return a generic error page
        return "error"; // Thymeleaf template name for your generic error page
    }
}
