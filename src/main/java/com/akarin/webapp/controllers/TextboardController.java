package com.akarin.webapp.controllers;

import com.akarin.webapp.managers.DatabaseManager;
import com.akarin.webapp.managers.TextboardManager;
import com.akarin.webapp.structure.Greeting;
import com.akarin.webapp.structure.IndexPage;
import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.Tools;
import com.akarin.webapp.util.ViewUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import spark.Request;
import spark.Response;
import spark.Route;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TextboardController {

    @Deprecated
    public static Route serveTextboardHome = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            final Map<String, Object> model = new HashMap<>();

            try {
                DatabaseManager.getAllBoards(model);
            } catch (SQLException | URISyntaxException e) {
                e.printStackTrace();
                return ViewUtil.renderErrorMessage(request, e.getMessage(), Reference.CommonStrings.LINK_ROOT,
                        Reference.Names.ROOT);
            }

            // Populate the rest of the html-forms
            model.put(Reference.VelocityVariables.INPUT_BOARDLINK, Reference.VelocityVariables.INPUT_BOARDLINK);
            model.put(Reference.VelocityVariables.INPUT_BOARDNAME, Reference.VelocityVariables.INPUT_BOARDNAME);
            model.put(Reference.VelocityVariables.INPUT_BOARDDESCRIPTION,
                    Reference.VelocityVariables.INPUT_BOARDDESCRIPTION);

            return ViewUtil.render(request, model, Reference.Templates.TEXTBOARD, Reference.Web.TEXTBOARD_ROOT,
                    "OK: default return");
        }
    };

    @Deprecated
    public static Route serveTextboardBoard = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            final Map<String, Object> model = new HashMap<>();

            // Obtain the request parameters
            final String boardLink = request.params(TextboardManager.BOARDLINK);

            // Put request parameters into the map
            model.put(TextboardManager.BOARDLINK, boardLink);

            // Retrieve the list of threads of a given board from the database
            try {
                DatabaseManager.getThreadsGivenBoardLink(boardLink, model);
            } catch (SQLException | URISyntaxException e) {
                e.printStackTrace();
                return ViewUtil.renderErrorMessage(request, e.getMessage(), Reference.CommonStrings.LINK_TEXTBOARD,
                        Reference.Names.TEXTBOARD);
            }

            // Populate the rest of the html-forms
            model.put(Reference.VelocityVariables.INPUT_THREADTEXT, Reference.VelocityVariables.INPUT_THREADTEXT);

            return ViewUtil.render(request, model, Reference.Templates.TEXTBOARD_BOARD, Reference.Web.TEXTBOARD_BOARD,
                    "OK: default return");
        }
    };

    @Deprecated
    public static Route serveTextboardThread = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            final Map<String, Object> model = new HashMap<>();

            // Obtain the request parameters
            final String boardLink = request.params(TextboardManager.BOARDLINK);
            final String threadId = request.params(TextboardManager.THREADID);

            // Put request parameters into the map
            model.put(TextboardManager.BOARDLINK, boardLink);
            model.put(TextboardManager.THREADID, threadId);

            try {
                DatabaseManager.getPostsGivenThreadId(threadId, model);
            } catch (SQLException | URISyntaxException e) {
                e.printStackTrace();
                return ViewUtil.renderErrorMessage(request, e.getMessage(),
                        TextboardManager.getPREVIOUSBOARDLINK(boardLink),
                        Reference.Web.TEXTBOARD_ROOT + "/" + boardLink);
            }

            // Populate html-form
            model.put(Reference.VelocityVariables.INPUT_POSTTEXT, Reference.VelocityVariables.INPUT_POSTTEXT);

            return ViewUtil.render(request, model, Reference.Templates.TEXTBOARD_BOARD_THREAD,
                    Reference.Web.TEXTBOARD_BOARD_THREAD, "OK: default return");
        }
    };

    @Deprecated
    public static Route handleCreateBoard = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {

            final String requestedBoardLink = request.queryParams(Reference.VelocityVariables.INPUT_BOARDLINK);
            final String requestedBoardName = request.queryParams(Reference.VelocityVariables.INPUT_BOARDNAME);
            final String requestedBoardDescription = request
                    .queryParams(Reference.VelocityVariables.INPUT_BOARDDESCRIPTION);

            if (TextboardManager.checkIfBoardIsAvailable(requestedBoardLink)) {
                try {
                    DatabaseManager.createBoard(requestedBoardLink, requestedBoardName, requestedBoardDescription);
                } catch (SQLException | URISyntaxException e) {
                    e.printStackTrace();
                    return ViewUtil.renderErrorMessage(request, e.getMessage(), Reference.CommonStrings.LINK_TEXTBOARD,
                            Reference.Names.TEXTBOARD);
                }
            }

            return serveTextboardHome.handle(request, response);
        }
    };
    @Deprecated
    public static Route handleCreateThread = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {

            // Retrieve data from the form
            final String currentBoard = request.params(TextboardManager.BOARDLINK);
            final String requestedThreadText = request.queryParams(Reference.VelocityVariables.INPUT_THREADTEXT);

            if (TextboardManager.checkIfTextIsAcceptable(requestedThreadText)) {
                try {
                    DatabaseManager.createThread(currentBoard, requestedThreadText);
                } catch (SQLException | URISyntaxException e) {
                    e.printStackTrace();
                    return ViewUtil.renderErrorMessage(request, e.getMessage(), Reference.CommonStrings.LINK_TEXTBOARD,
                            Reference.Names.TEXTBOARD);
                }
            }

            return serveTextboardBoard.handle(request, response);
        }
    };

    @Deprecated
    public static Route handleCreatePost = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {

            final String requestedPostText = request.queryParams(Reference.VelocityVariables.INPUT_POSTTEXT);
            final String currentBoard = request.params(TextboardManager.BOARDLINK);
            final String currentThread = request.params(TextboardManager.THREADID);

            if (TextboardManager.checkIfTextIsAcceptable(requestedPostText)) {
                try {
                    DatabaseManager.createPost(currentThread, requestedPostText);
                } catch (final Exception e) {
                    e.printStackTrace();
                    return ViewUtil.renderErrorMessage(request, e.getMessage(),
                            TextboardManager.getPREVIOUSTHREAD(currentBoard, currentThread), currentThread);
                }
            } else {
                Tools.coutln("Rejected post with the text:" + requestedPostText);
            }

            return serveTextboardThread.handle(request, response);
        }
    };

    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new Greeting());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute Greeting greeting) {
        return "result";
    }
}
