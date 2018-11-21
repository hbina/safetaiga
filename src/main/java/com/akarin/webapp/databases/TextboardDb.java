package com.akarin.webapp.databases;

import com.akarin.webapp.managers.TextboardManager;
import com.akarin.webapp.util.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.akarin.webapp.managers.DatabaseManager.getConnection;

public class TextboardDb {

    private static final Logger logger = LoggerFactory.getLogger(TextboardDb.class);

    @SuppressWarnings("UnusedReturnValue")
    public static Map<String, Object> getAllBoards(final Map<String, Object> model) {
        try (Connection connection = getConnection()) {
            final ArrayList<Map<String, String>> arrayOfBoardsFromDatabase = new ArrayList<>();

            final String script = "SELECT * FROM boards;";
            final PreparedStatement stmt = connection.prepareStatement(script);

            logger.info(stmt.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                final Map<String, String> board = new HashMap<>();
                board.put(TextboardManager.BOARDNAME, rs.getString(TextboardManager.BOARDNAME));
                board.put(TextboardManager.BOARDLINK, rs.getString(TextboardManager.BOARDLINK));
                board.put(TextboardManager.BOARDDESCRIPTION, rs.getString(TextboardManager.BOARDDESCRIPTION));
                arrayOfBoardsFromDatabase.add(board);
            }

            model.put(Reference.VelocityVariables.BOARDLIST, arrayOfBoardsFromDatabase);
            stmt.close();
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
        return model;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Map<String, Object> getThreadsGivenBoardLink(final String boardLink, final Map<String, Object> model) {
        try (Connection connection = getConnection()) {
            final ArrayList<Map<String, String>> arrayOfThreadsFromDatabase = new ArrayList<>();
            final String script = "SELECT * FROM threads AS thread WHERE thread.boardLink = ?;";
            final PreparedStatement stmt = connection.prepareStatement(script);

            stmt.setString(1, boardLink);

            logger.info(stmt.toString());
            final ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                final Map<String, String> thread = new HashMap<>();
                thread.put(TextboardManager.THREADID, rs.getString(TextboardManager.THREADID));
                thread.put(TextboardManager.THREADTEXT, rs.getString(TextboardManager.THREADTEXT));
                arrayOfThreadsFromDatabase.add(thread);
            }

            model.put(Reference.VelocityVariables.THREADLIST, arrayOfThreadsFromDatabase);
            stmt.close();
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
        return model;
    }

    @SuppressWarnings("UnusedReturnValue")
    public static Map<String, Object> getPostsGivenThreadId(final String threadId, final Map<String, Object> model) {
        try (Connection connection = getConnection()) {
            final String scriptOpText = "SELECT threadText FROM threads WHERE threadId = ? LIMIT 1;";
            PreparedStatement stmt = connection.prepareStatement(scriptOpText);
            stmt.setInt(1, Integer.valueOf(threadId));

            logger.info(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            rs.next();
            final String threadText = rs.getString(TextboardManager.THREADTEXT);

            model.put(TextboardManager.THREADID, threadId);
            model.put(TextboardManager.THREADTEXT, threadText);

            final String scriptThreadPosts = "SELECT * FROM posts WHERE threadId = ?;";
            stmt = connection.prepareStatement(scriptThreadPosts);

            stmt.setInt(1, Integer.valueOf(threadId));

            logger.info(stmt.toString());
            rs = stmt.executeQuery();
            final ArrayList<Map<String, String>> arrayOfPostsFromDatabase = new ArrayList<>();

            while (rs.next()) {
                final Map<String, String> post = new HashMap<>();
                post.put(TextboardManager.POSTID, rs.getString(TextboardManager.POSTID));
                post.put(TextboardManager.POSTTEXT, rs.getString(TextboardManager.POSTTEXT));
                arrayOfPostsFromDatabase.add(post);
            }

            model.put(Reference.VelocityVariables.POSTLIST, arrayOfPostsFromDatabase);
            stmt.close();
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
        return model;
    }

    public static void createTableBoards() {
        try (Connection connection = getConnection()) {
            final String script = "CREATE TABLE IF NOT EXISTS boards (boardLink TEXT, boardName TEXT, boardDescription TEXT, PRIMARY KEY(boardLink));";
            final PreparedStatement stmt = connection.prepareStatement(script);
            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }
    }

    public static void createTableThreads() {
        try (Connection connection = getConnection()) {
            final String script = "CREATE TABLE IF NOT EXISTS threads (threadId SERIAL, boardLink TEXT, threadText TEXT, PRIMARY KEY (threadId), FOREIGN KEY (boardLink) REFERENCES boards(boardLink));";
            final PreparedStatement stmt = connection.prepareStatement(script);
            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }

    }

    public static void createTablePosts() {
        try (Connection connection = getConnection()) {
            final String script = "CREATE TABLE IF NOT EXISTS posts (postid SERIAL, threadId INTEGER, postText TEXT, PRIMARY KEY (postid), FOREIGN KEY (threadId) REFERENCES threads(threadId));";
            final PreparedStatement stmt = connection.prepareStatement(script);
            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        }
    }


    public static void createBoard(final String boardLink, final String boardName, final String boardDescription) {
        try (Connection connection = getConnection()) {
            final String script = "INSERT INTO boards (boardLink, boardName, boardDescription) VALUES (?,?,?);";
            final PreparedStatement stmt = connection.prepareStatement(script);

            stmt.setString(1, boardLink);
            stmt.setString(2, boardName);
            stmt.setString(3, boardDescription);

            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
    }

    public static void createThread(final String currentBoard, final String requestedThreadText) {
        try (Connection connection = getConnection()) {

            final String script = "INSERT INTO threads (boardLink, threadText) VALUES ( ?, ?);";
            final PreparedStatement stmt = connection.prepareStatement(script);

            stmt.setString(1, currentBoard);
            stmt.setString(2, requestedThreadText);
            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
    }

    public static void createPost(final String threadId, final String postText) {
        try (Connection connection = getConnection()) {

            final String script = "INSERT INTO posts (threadId, postText) VALUES (?, ?);";
            final PreparedStatement stmt = connection.prepareStatement(script);

            stmt.setInt(1, Integer.valueOf(threadId));
            stmt.setString(2, postText);
            logger.info(stmt.toString());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            logger.info(e.getSQLState());
            logger.info(e.getMessage());
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
    }
}