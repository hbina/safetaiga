package com.akarin.webapp.controllers;

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

/**
 * TODO: How do large enterprises handle links? What if an intern drops a random link handler? or override others?? What about the hassle maintaining them??
 */

@Controller
class RootController {

    @Deprecated
    public static Route serveRootPage = (request, response) -> {
        Map<String, Object> model = new HashMap<>();
        return ViewUtil.render(request, model, Reference.Templates.ROOT, "ROOT PAGE", "OK");
    };
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/textboard")
    public static String getTextboardIndex() {
        return "textboard_index";
    }

    @RequestMapping(path = "/")
    public String getIndex(Map<String, Object> model) {
        model.put("visitor_counter", String.valueOf(counter.incrementAndGet()));
        return "index";
    }

    // TODO: Show user statistics in here
    @RequestMapping(path = "/yaapos")
    public String yaaposIndexPage(Model model) {
        model.addAttribute("ExpenditureItem", new ExpenditureItem()); // TODO: This should be in yaapos/submit or something
        return "yaapos_index";
    }
}