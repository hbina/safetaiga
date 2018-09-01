package com.akarin.webapp.controllers;

import com.akarin.webapp.databases.TextboardDb;
import com.akarin.webapp.managers.TextboardManager;
import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.Tools;
import com.akarin.webapp.util.ViewUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spark.Route;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
class TextboardController {

    @Deprecated
    private static final Route serveTextboardHome = (request, response) -> {
        final Map<String, Object> model = new HashMap<>();

        TextboardDb.getAllBoards(model);

        // Populate the rest of the html-forms
        model.put(Reference.VelocityVariables.INPUT_BOARDLINK, Reference.VelocityVariables.INPUT_BOARDLINK);
        model.put(Reference.VelocityVariables.INPUT_BOARDNAME, Reference.VelocityVariables.INPUT_BOARDNAME);
        model.put(Reference.VelocityVariables.INPUT_BOARDDESCRIPTION,
                Reference.VelocityVariables.INPUT_BOARDDESCRIPTION);

        return ViewUtil.render(request, model, Reference.Templates.TEXTBOARD, Reference.Web.TEXTBOARD_ROOT,
                "OK: default return");
    };

    @Deprecated
    private static final Route serveTextboardBoard = (request, response) -> {
        final Map<String, Object> model = new HashMap<>();

        // Obtain the request parameters
        final String boardLink = request.params(TextboardManager.BOARDLINK);

        // Put request parameters into the map
        model.put(TextboardManager.BOARDLINK, boardLink);

        TextboardDb.getThreadsGivenBoardLink(boardLink, model);

        // Populate the rest of the html-forms
        model.put(Reference.VelocityVariables.INPUT_THREADTEXT, Reference.VelocityVariables.INPUT_THREADTEXT);

        return ViewUtil.render(request, model, Reference.Templates.TEXTBOARD_BOARD, Reference.Web.TEXTBOARD_BOARD,
                "OK: default return");
    };

    @Deprecated
    private static final Route serveTextboardThread = (request, response) -> {
        final Map<String, Object> model = new HashMap<>();

        // Obtain the request parameters
        final String boardLink = request.params(TextboardManager.BOARDLINK);
        final String threadId = request.params(TextboardManager.THREADID);

        // Put request parameters into the map
        model.put(TextboardManager.BOARDLINK, boardLink);
        model.put(TextboardManager.THREADID, threadId);

        TextboardDb.getPostsGivenThreadId(threadId, model);

        // Populate html-form
        model.put(Reference.VelocityVariables.INPUT_POSTTEXT, Reference.VelocityVariables.INPUT_POSTTEXT);

        return ViewUtil.render(request, model, Reference.Templates.TEXTBOARD_BOARD_THREAD,
                Reference.Web.TEXTBOARD_BOARD_THREAD, "OK: default return");
    };

    @Deprecated
    public static Route handleCreateBoard = (request, response) -> {

        final String requestedBoardLink = request.queryParams(Reference.VelocityVariables.INPUT_BOARDLINK);
        final String requestedBoardName = request.queryParams(Reference.VelocityVariables.INPUT_BOARDNAME);
        final String requestedBoardDescription = request
                .queryParams(Reference.VelocityVariables.INPUT_BOARDDESCRIPTION);

        if (TextboardManager.checkIfBoardIsAvailable(requestedBoardLink)) {
            TextboardDb.createBoard(requestedBoardLink, requestedBoardName, requestedBoardDescription);
        }
        return serveTextboardHome.handle(request, response);
    };

    @Deprecated
    public static Route handleCreateThread = (request, response) -> {

        // Retrieve data from the form
        final String currentBoard = request.params(TextboardManager.BOARDLINK);
        final String requestedThreadText = request.queryParams(Reference.VelocityVariables.INPUT_THREADTEXT);

        if (TextboardManager.checkIfTextIsAcceptable(requestedThreadText)) {
            TextboardDb.createThread(currentBoard, requestedThreadText);
        }

        return serveTextboardBoard.handle(request, response);
    };

    @Deprecated
    public static Route handleCreatePost = (request, response) -> {

        final String requestedPostText = request.queryParams(Reference.VelocityVariables.INPUT_POSTTEXT);
        final String currentBoard = request.params(TextboardManager.BOARDLINK);
        final String currentThread = request.params(TextboardManager.THREADID);

        if (TextboardManager.checkIfTextIsAcceptable(requestedPostText)) {
            try {
                TextboardDb.createPost(currentThread, requestedPostText);
            } catch (final Exception e) {
                e.printStackTrace();
                return ViewUtil.renderErrorMessage(request, e.getMessage(),
                        TextboardManager.getPREVIOUSTHREAD(currentBoard, currentThread), currentThread);
            }
        } else {
            Tools.coutln("Rejected post with the text:" + requestedPostText);
        }

        return serveTextboardThread.handle(request, response);
    };
}
