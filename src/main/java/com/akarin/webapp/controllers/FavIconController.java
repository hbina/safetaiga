package com.akarin.webapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FavIconController {
    
    Logger logger = LoggerFactory.getLogger(FavIconController.class);

    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
        logger.debug("What are you doing here?");
    }
}