package com.akarin.webapp.controllers;

import com.akarin.webapp.storage.StorageService;
import com.akarin.webapp.structure.ExpenditureItem;
import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.ViewUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import spark.Route;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Controller
class RootController {

    private static final String template = "Hello, %s!";
    @Deprecated
    public static Route serveRootPage = (request, response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Reference.Templates.ROOT, "ROOT PAGE", "OK");
    };
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/")
    public String getIndex(Map<String, Object> model) {
        model.put("visitor_counter", String.valueOf(counter.incrementAndGet()));
        return "index";
    }

    @RequestMapping("/yaapos")
    public String yaaposIndexPage(Model model) {
        model.addAttribute("ExpenditureItem", new ExpenditureItem());
        return "yaapos_index";
    }
}