package com.akarin.webapp.managers;

import com.akarin.webapp.Main;
import com.akarin.webapp.util.Reference;
import com.akarin.webapp.util.ScriptCreator;
import com.akarin.webapp.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//import processing.ImageProcessing;

public class DatabaseManager {

    private static Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static String DATABASE_USERNAME = "postgres";
    private static String DATABASE_PASSWORD = "1234";

    public static void setDbLogin() {
        logger.info("Current dir:" + System.getProperty("user.dir"));
        try {
            BufferedReader br = new BufferedReader(new FileReader("database_login.txt"));
            StringBuilder sb = new StringBuilder();
            DATABASE_USERNAME = br.readLine();
            DATABASE_PASSWORD = br.readLine();
            logger.info("Using db with" + System.getProperty("line.separator") + "username:" + DATABASE_USERNAME
                    + System.getProperty("line.separator") + "password:" + DATABASE_PASSWORD);
            br.close();
        } catch (FileNotFoundException e) {
            logger.info("Please provide the necessary files for database setup");
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("An IO exception error have occured");
            e.printStackTrace();
        } finally {
            logger.info("Database setup successfull");
        }
    }

    public static Connection getConnection() throws URISyntaxException, SQLException {
        String username = DATABASE_USERNAME;
        String password = DATABASE_PASSWORD;
        String dbUrl;

        if (System.getenv("DATABASE_URL") != null) {
            final URI dbUri = new URI(System.getenv("DATABASE_URL"));
            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
            dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        } else {
            // If an .env file is not provided, try to use a local psql database instead
            dbUrl = "jdbc:postgresql://" + "localhost" + ":" + "5432" + "/" + username;
        }

        return DriverManager.getConnection(dbUrl, username, password);
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

    public static void getPostsGivenThreadId(final String threadId, final Map<String, Object> model)
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
        @SuppressWarnings("rawtypes") final ArrayList<Map> arrayOfPostsFromDatabase = new ArrayList<Map>();

        while (rs.next()) {
            final Map<String, String> post = new HashMap<String, String>();

            // populate board with the appropriate description of a board
            post.put(TextboardManager.POSTID, rs.getString(TextboardManager.POSTID));
            post.put(TextboardManager.POSTTEXT, rs.getString(TextboardManager.POSTTEXT));

            arrayOfPostsFromDatabase.add(post);
        }

        // Populate with list of posts
        model.put(Reference.VelocityVariables.POSTLIST, arrayOfPostsFromDatabase);

        pstmt.close();
    }

    public static void getAllBoards(final Map<String, Object> model) throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        @SuppressWarnings("rawtypes") final ArrayList<Map> arrayOfBoardsFromDatabase = new ArrayList<Map>();

        final Statement stmt = connection.createStatement();
        final ResultSet rs = stmt.executeQuery(ScriptCreator.SELECT_ALL_FROM_BOARDS);

        while (rs.next()) {
            final Map<String, String> board = new HashMap<String, String>();

            // populate board with the appropriate description of a board
            board.put(TextboardManager.BOARDNAME, rs.getString(TextboardManager.BOARDNAME));
            board.put(TextboardManager.BOARDLINK, rs.getString(TextboardManager.BOARDLINK));
            board.put(TextboardManager.BOARDDESCRIPTION, rs.getString(TextboardManager.BOARDDESCRIPTION));

            arrayOfBoardsFromDatabase.add(board);
        }

        // Populate with list of boards
        model.put(Reference.VelocityVariables.BOARDLIST, arrayOfBoardsFromDatabase);

        stmt.close();
    }

    public static void getThreadsGivenBoardLink(final String boardLink, final Map<String, Object> model)
            throws SQLException, URISyntaxException {
        final Connection connection = getConnection();

        // Prepare arraylist for output from database
        @SuppressWarnings("rawtypes") final ArrayList<Map> arrayOfThreadsFromDatabase = new ArrayList<Map>();

        // Select all thread based on the given boardlink
        final String script = "SELECT * FROM threads AS thread WHERE thread.boardlink = ?;";
        final PreparedStatement pstmt = connection.prepareStatement(script);

        pstmt.setString(1, boardLink);

        final ResultSet rs = pstmt.executeQuery();

        // this is how you get a column given the colum name in string
        while (rs.next()) {
            // Prepare the map for threadid
            final Map<String, String> thread = new HashMap<String, String>();

            // populate board with the appropriate description of a board
            thread.put(TextboardManager.THREADID, rs.getString(TextboardManager.THREADID));
            thread.put(TextboardManager.THREADTEXT, rs.getString(TextboardManager.THREADTEXT));
            // put board into the arrayOfThreadsFromDatabase
            arrayOfThreadsFromDatabase.add(thread);
        }

        // Populate with list of threads
        model.put(Reference.VelocityVariables.THREADLIST, arrayOfThreadsFromDatabase);

        pstmt.close();
    }

    /**
     * public static void getImageDbAverageRGB() { try (Connection connection =
     * SettingUp.getConnection()) { Statement stmt = connection.createStatement();
     * ResultSet rs = stmt.executeQuery(ScriptCreator.selectAverageOfImageDb());
     *
     * rs.next(); String averageOfRGB = ""; String result = ""; for (int a = 1; a <=
     * ImageProcessing.DIVISOR_VALUE; a++) { for (int b = 1; b <=
     * ImageProcessing.DIVISOR_VALUE; b++) { for (int c = 1; c <= 3; c++) { result =
     * "{" + rs.getString("" + a + ":" + b + ":" + c) + "}"; averageOfRGB += result;
     * } averageOfRGB += System.lineSeparator(); } } } catch (SQLException e) {
     * e.printStackTrace(); } catch (URISyntaxException e) { e.printStackTrace(); }
     * }
     **/

    /**
     * public static void getImageDbMinMax() { try (Connection connection =
     * SettingUp.getConnection()) { Statement stmt = connection.createStatement();
     * ResultSet rs = stmt.executeQuery(ScriptCreator.getMinMaxOfImageDb());
     * <p>
     * rs.next(); String minMaxOfRGB = ""; String result = ""; for (int a = 1; a <=
     * ImageProcessing.DIVISOR_VALUE; a++) { for (int b = 1; b <=
     * ImageProcessing.DIVISOR_VALUE; b++) { for (int c = 1; c <= 3; c++) { result =
     * "{" + rs.getString("MIN:" + a + ":" + b + ":" + c) + "," +
     * rs.getString("MAX:" + a + ":" + b + ":" + c) + "}"; minMaxOfRGB += result; }
     * } } } catch (SQLException e) { e.printStackTrace(); } catch
     * (URISyntaxException e) { e.printStackTrace(); } }
     **/
    public static void insertPartitionHash(final String animeName, final int episode, final int frame,
                                           final int[] partitionHash) {
        try (Connection connection = getConnection()) {

            final String query = "INSERT INTO partition_hash (name, episode, frame, hash_red, hash_green, hash_blue) VALUES (?,?,?,?,?,?);";
            final PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setString(1, animeName);
            pstmt.setInt(2, episode);
            pstmt.setInt(3, frame);
            pstmt.setInt(4, partitionHash[0]);
            pstmt.setInt(5, partitionHash[1]);
            pstmt.setInt(6, partitionHash[2]);

            pstmt.executeUpdate();
        } catch (final SQLException e) {
            logger.warn("duplicate key ");
            logger.trace(e.getMessage());
        } catch (final URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    public static void partitionSearch(final ArrayList<Integer> partitionHash, final Map<String, Object> model) {
        try (Connection connection = getConnection()) {

            final String query = "SELECT * FROM partition_hash WHERE hash_red = ? AND hash_green = ? AND hash_blue = ?;";
            final PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1, partitionHash.get(0));
            pstmt.setInt(2, partitionHash.get(1));
            pstmt.setInt(3, partitionHash.get(2));

            final ResultSet rs = pstmt.executeQuery();

            final ArrayList<String> partitionHashResult = new ArrayList<String>();
            while (rs.next()) {
                partitionHashResult
                        .add(rs.getString("name") + " " + rs.getString("episode") + " " + rs.getString("frame") + " "
                                + rs.getInt("hash_red") + " " + rs.getInt("hash_green") + " " + rs.getInt("hash_blue"));
            }

            model.put("partitionHashResult", partitionHashResult);
        } catch (final SQLException e) {
            logger.warn(e.getMessage());
        } catch (final URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }
}
