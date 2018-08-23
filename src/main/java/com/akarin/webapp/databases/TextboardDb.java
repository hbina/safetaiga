package com.akarin.webapp.databases;

import com.akarin.webapp.managers.TextboardManager;
import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.ScriptCreator;

import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.akarin.webapp.managers.DatabaseManager.getConnection;

public class TextboardDb {


    public static Map<String, Object> getAllBoards(final Map<String, Object> model) throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        @SuppressWarnings("rawtypes") final ArrayList<Map> arrayOfBoardsFromDatabase = new ArrayList<>();

        final Statement stmt = connection.createStatement();
        final ResultSet rs = stmt.executeQuery(ScriptCreator.SELECT_ALL_FROM_BOARDS);

        while (rs.next()) {
            final Map<String, String> board = new HashMap<>();

            // populate board with the appropriate description of a board
            board.put(TextboardManager.BOARDNAME, rs.getString(TextboardManager.BOARDNAME));
            board.put(TextboardManager.BOARDLINK, rs.getString(TextboardManager.BOARDLINK));
            board.put(TextboardManager.BOARDDESCRIPTION, rs.getString(TextboardManager.BOARDDESCRIPTION));

            arrayOfBoardsFromDatabase.add(board);
        }

        // Populate with list of boards
        model.put(Reference.VelocityVariables.BOARDLIST, arrayOfBoardsFromDatabase);

        stmt.close();

        return model;
    }

    public static Map<String, Object> getThreadsGivenBoardLink(final String boardLink, final Map<String, Object> model)
            throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        // Prepare arraylist for output from database
        @SuppressWarnings("rawtypes") final ArrayList<Map> arrayOfThreadsFromDatabase = new ArrayList<>();

        // Select all thread based on the given boardlink
        final String script = "SELECT * FROM threads AS thread WHERE thread.boardlink = ?;";
        final PreparedStatement pstmt = connection.prepareStatement(script);

        pstmt.setString(1, boardLink);

        final ResultSet rs = pstmt.executeQuery();

        // this is how you get a column given the colum name in string
        while (rs.next()) {
            // Prepare the map for threadid
            final Map<String, String> thread = new HashMap<>();

            // populate board with the appropriate description of a board
            thread.put(TextboardManager.THREADID, rs.getString(TextboardManager.THREADID));
            thread.put(TextboardManager.THREADTEXT, rs.getString(TextboardManager.THREADTEXT));
            // put board into the arrayOfThreadsFromDatabase
            arrayOfThreadsFromDatabase.add(thread);
        }

        // Populate with list of threads
        model.put(Reference.VelocityVariables.THREADLIST, arrayOfThreadsFromDatabase);

        pstmt.close();

        return model;
    }

    public static Map<String, Object> getPostsGivenThreadId(final String threadId, final Map<String, Object> model)
            throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        final String scriptOpText = "SELECT threadtext FROM threads WHERE threadid = ? LIMIT 1;";
        PreparedStatement pstmt = connection.prepareStatement(scriptOpText);

        pstmt.setInt(1, Integer.valueOf(threadId));

        ResultSet rs = pstmt.executeQuery();

        rs.next();

        final String threadText = rs.getString(TextboardManager.THREADTEXT);

        model.put(TextboardManager.THREADID, threadId);
        model.put(TextboardManager.THREADTEXT, threadText);

        final String scriptThreadPosts = "SELECT * FROM posts WHERE threadid = ?;";
        pstmt = connection.prepareStatement(scriptThreadPosts);

        pstmt.setInt(1, Integer.valueOf(threadId));

        rs = pstmt.executeQuery();

        // Prepare arraylist for output from database
        @SuppressWarnings("rawtypes") final ArrayList<Map> arrayOfPostsFromDatabase = new ArrayList<>();

        while (rs.next()) {
            final Map<String, String> post = new HashMap<>();

            // populate board with the appropriate description of a board
            post.put(TextboardManager.POSTID, rs.getString(TextboardManager.POSTID));
            post.put(TextboardManager.POSTTEXT, rs.getString(TextboardManager.POSTTEXT));

            arrayOfPostsFromDatabase.add(post);
        }

        // Populate with list of posts
        model.put(Reference.VelocityVariables.POSTLIST, arrayOfPostsFromDatabase);

        pstmt.close();

        return model;
    }

    public static void createTableBoards() throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        final String script = "CREATE TABLE IF NOT EXISTS boards ( boardlink TEXT, boardname TEXT, boarddescription TEXT, PRIMARY KEY(boardlink));";
        final Statement stmt = connection.createStatement();

        stmt.executeUpdate(script);

        stmt.close();
    }

    public static void createTableThreads() throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        final String script = "CREATE TABLE IF NOT EXISTS threads (threadid SERIAL, boardlink TEXT, threadtext TEXT, PRIMARY KEY (threadid), FOREIGN KEY (boardlink) REFERENCES boards(boardlink));";
        final Statement stmt = connection.createStatement();

        stmt.executeUpdate(script);

        stmt.close();
    }

    public static void createTablePosts() throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        final String script = "CREATE TABLE IF NOT EXISTS posts (postid SERIAL, threadid INTEGER, posttext TEXT, PRIMARY KEY (postid), FOREIGN KEY (threadid) REFERENCES threads(threadid));";
        final Statement stmt = connection.createStatement();

        stmt.executeUpdate(script);

        stmt.close();
    }

    public static void createBoard(final String boardLink, final String boardName, final String boardDescription)
            throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        final String script = "INSERT INTO boards (boardlink, boardname, boarddescription) VALUES (?,?,?);";
        final PreparedStatement pstmt = connection.prepareStatement(script);

        pstmt.setString(1, boardLink);
        pstmt.setString(2, boardName);
        pstmt.setString(3, boardDescription);

        pstmt.executeUpdate();

        pstmt.close();
    }

    public static void createThread(final String currentBoard, final String requestedThreadText)
            throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        final String script = "INSERT INTO threads (boardlink, threadtext) VALUES ( ?, ?);";
        final PreparedStatement pstmt = connection.prepareStatement(script);

        pstmt.setString(1, currentBoard);
        pstmt.setString(2, requestedThreadText);

        pstmt.executeUpdate();

        pstmt.close();
    }

    public static void createPost(final String threadId, final String postText)
            throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        final String script = "INSERT INTO posts (threadid, posttext) VALUES (?, ?);";
        final PreparedStatement pstmt = connection.prepareStatement(script);

        pstmt.setInt(1, Integer.valueOf(threadId));
        pstmt.setString(2, postText);

        pstmt.executeUpdate();

        pstmt.close();
    }
}
