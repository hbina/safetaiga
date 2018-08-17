package com.akarin.webapp.util;

import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.http.HttpStatus;
import spark.*;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class ViewUtil {

    public static Route notFound = (final Request request, final Response response) -> {
        response.status(HttpStatus.NOT_FOUND_404);
        final Map<String, Object> model = new HashMap<>();
        return render(request, model, Reference.Templates.NOT_FOUND, Reference.CommonStrings.ERROR, "404 NOT FOUND");
    };
    // If a user manually manipulates paths and forgets to add
    // a trailing slash, redirect the user to the correct path
    public static Filter addTrailingSlashes = (final Request request, final Response response) -> {
        // Tools.print("FROM:Filters:START:addTrailingSlashes");
        if (!request.pathInfo().endsWith("/")) {
            response.redirect(request.pathInfo() + "/");
        }
        // Tools.print("END:addTrailingSlashes");
    };
    // Enable GZIP for all responses
    public static Filter addGzipHeader = (final Request request, final Response response) -> {
        // Tools.print("FROM:Filters:START:addGzipHeader");
        response.header("Content-Encoding", "gzip");
        // Tools.print("END:addGzipHeader");
    };

    // Renders a template given a model and a request
    // The request is needed to check the user session for language settings
    // and to see if the user is logged in
    public static String render(final Request request, final Map<String, Object> model, final String templatePath,
                                final String where, final String message) {

        model.put(Reference.VelocityVariables.WEBPAGE_DEFAULT_ICON, System.getProperty("file.separator") + "other"
                + System.getProperty("file.separator") + "akarin_pic.ico");

        /** Basic links that are not dynamic like :boardlink or :threadid **/
        model.put(Reference.VelocityVariables.ROOT_LINK, Reference.Web.ROOT);
        model.put(Reference.VelocityVariables.ROOT_NAME, Reference.Names.ROOT);

        model.put(Reference.VelocityVariables.TEXTBOARD_LINK, Reference.Web.TEXTBOARD_ROOT);
        model.put(Reference.VelocityVariables.TEXTBOARD_NAME, Reference.Names.TEXTBOARD);

        model.put(Reference.VelocityVariables.IMAGEPROCESSING_LINK, Reference.Web.IMAGEPROCESSING_ROOT);
        model.put(Reference.VelocityVariables.IMAGEPROCESSING_NAME, Reference.Names.IMAGEPROCESSING);

        model.put(Reference.VelocityVariables.MANIFESTO_LINK, Reference.Web.MANIFESTO_ROOT);
        model.put(Reference.VelocityVariables.MANIFESTO_NAME, Reference.Web.MANIFESTO_ROOT);

        model.put(Reference.VelocityVariables.WHERE_NAME, where);
        model.put(Reference.VelocityVariables.WHERE_TEXT, message);
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }

    private static VelocityTemplateEngine strictVelocityEngine() {
        final VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }

    // Renders a template given a model and a request
    // The request is needed to know where the user is
    public static String renderErrorMessage(final Request request, final String errorMessage, final String returnLink,
                                            final String returnName) {
        Tools.coutln(System.lineSeparator() + "nAn error rendering page has occured" + System.lineSeparator());

        final Map<String, Object> model = new HashMap<String, Object>();
        model.put(Reference.CommonStrings.ERROR, errorMessage);
        model.put(Reference.CommonStrings.RETURNLINK, returnLink);
        model.put(Reference.CommonStrings.RETURNNAME, returnName);
        return render(request, model, Reference.Templates.ERROR, "Error", errorMessage);
    }
}
