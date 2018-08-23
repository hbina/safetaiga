package com.akarin.webapp.databases;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static com.akarin.webapp.managers.DatabaseManager.getConnection;

public class ImageProcessingDb {

    private static final Logger logger = LoggerFactory.getLogger(ImageProcessingDb.class);

    @Deprecated
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
            logger.trace(e.getMessage());
        } catch (final URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }

    @Deprecated
    public static void partitionSearch(final ArrayList<Integer> partitionHash, final Map<String, Object> model) {
        try (Connection connection = getConnection()) {

            final String query = "SELECT * FROM partition_hash WHERE hash_red = ? AND hash_green = ? AND hash_blue = ?;";
            final PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1, partitionHash.get(0));
            pstmt.setInt(2, partitionHash.get(1));
            pstmt.setInt(3, partitionHash.get(2));

            final ResultSet rs = pstmt.executeQuery();

            final ArrayList<String> partitionHashResult = new ArrayList<>();
            while (rs.next()) {
                partitionHashResult
                        .add(rs.getString("name") + " " + rs.getString("episode") + " " + rs.getString("frame") + " "
                                + rs.getInt("hash_red") + " " + rs.getInt("hash_green") + " " + rs.getInt("hash_blue"));
            }

            model.put("partitionHashResult", partitionHashResult);
        } catch (final SQLException | URISyntaxException e) {
            logger.warn(e.getMessage());
        }
    }
}
