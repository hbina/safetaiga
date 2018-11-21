package com.akarin.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@SuppressWarnings("unused")
//org.springframework.boot.autoconfigure.web.ErrorController
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private static final String PATH = "/error";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @SuppressWarnings("SameReturnValue")
    @RequestMapping("/error")
    protected String error(final RedirectAttributes redirectAttributes) {
        logger.error("Handling error");
        redirectAttributes.addFlashAttribute("error", true);
        logger.info(redirectAttributes.toString());
        return "redirect:/";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}