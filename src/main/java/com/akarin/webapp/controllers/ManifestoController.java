package com.akarin.webapp.controllers;

import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;
import java.util.Map;

public class ManifestoController {

    public static Route serveManifestoPage = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            final Map<String, Object> model = new HashMap<>();

            return ViewUtil.render(request, model, Reference.Templates.MANIFESTO_MAINPAGE, Reference.Web.MANIFESTO_ROOT,
                    "OK: default return");
        }
    };
}