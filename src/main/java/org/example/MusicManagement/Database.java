package org.example.MusicManagement;
import org.example.MusicManagement.Models.Music;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    static private final String URL = "jdbc:mysql://localhost:3306/musicLibrary";
    static private final String USERNAME = "root";
    static private final String PASSWORD ="";
    static private final String TABLENAME ="music";
    private static Connection conn;
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (conn == null || conn.isClosed()) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return conn;
    }
    public static void closeSession() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database session closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close database session.");
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Music> getDataDB() {
        ArrayList<Music> arrMusic = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();

             // Execute SELECT query
             ResultSet rs = stmt.executeQuery("SELECT artistName, songName, album, pathSong FROM " + TABLENAME)) {

            while (rs.next()) {
                String artistName = rs.getString("artistName");
                String songName = rs.getString("songName");
                String album = rs.getString("album");
                String pathSong = rs.getString("pathSong");

                Music music = new Music(songName, artistName, album, pathSong);
                arrMusic.add(music);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }finally {
            closeSession();
        }
        return arrMusic;
    }

    public void addDataToDB(Music music) {
        String sql = "INSERT INTO " + TABLENAME + " (artistName, songName, album, pathSong) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); // Memanggil metode getConnection
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the UPDATE query
            pstmt.setString(1, music.getArtistName());
            pstmt.setString(2, music.getSongName());
            pstmt.setString(3, music.getAlbum());
            pstmt.setString(4, music.getPathSong());

            // Execute INSERT query
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data berhasil ditambahkan ke database!");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }finally {
            closeSession();
        }
    }
    public boolean editToDB(String targetSongName, String newSongName, String newArtistName, String album, String pathSong) {
        String selectQuery = "SELECT songName FROM " + TABLENAME + " WHERE songName = ?";

        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            // Set the parameter to the targetSongName to search for it
            selectStmt.setString(1, targetSongName);

            // Execute the SELECT query
            ResultSet rs = selectStmt.executeQuery();

            // If the song exists, proceed with the update
            if (rs.next()) {
                String updateQuery = "UPDATE " + TABLENAME + " SET songName = ?, artistName = ?, album = ?, pathSong = ? WHERE songName = ?";

                try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                    // Set the parameters for the UPDATE query
                    updateStmt.setString(1, newSongName);
                    updateStmt.setString(2, newArtistName);
                    updateStmt.setString(3, album);
                    updateStmt.setString(4, pathSong);
                    updateStmt.setString(5, targetSongName); // Condition to match the song to update

                    // Execute the UPDATE query
                    int rowsUpdated = updateStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Song successfully updated in database!");
                        return true;
                    } else {
                        System.out.println("Failed to update the song.");
                        return false;
                    }
                }

            } else {
                System.out.println("No song found with the name: " + targetSongName);
                return false;
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }finally {
            closeSession();
        }
        return false;
    }

    public boolean deleteToDB(String songName) {
        String sql = "DELETE FROM " + TABLENAME + " WHERE songName = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameter for the SQL query
            pstmt.setString(1, songName);

            // Execute the DELETE query
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Song successfully deleted from database.");
                return true;
            } else {
                System.out.println("No song found with the name: " + songName);
                return false;
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }finally {
            closeSession();
        }

        return false;
    }
}
