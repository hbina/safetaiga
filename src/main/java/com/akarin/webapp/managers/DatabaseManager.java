package com.akarin.webapp.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//import processing.ImageProcessing;

public class DatabaseManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static String DATABASE_USERNAME = "postgres";
    private static String DATABASE_PASSWORD = "1234";

    public static void setDbLogin() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("database_login.txt"));
            StringBuilder sb = new StringBuilder();
            DATABASE_USERNAME = br.readLine();
            DATABASE_PASSWORD = br.readLine();
            logger.info("Using db with username:" + DATABASE_USERNAME + " password:" + DATABASE_PASSWORD);
            br.close();
        } catch (FileNotFoundException e) {
            logger.info("Please provide the necessary files for database setup");
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("An IO exception error have occurred");
            e.printStackTrace();
        } finally {
            logger.info("Database setup successful");
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
}
