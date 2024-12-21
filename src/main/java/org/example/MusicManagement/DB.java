package org.example.MusicManagement;

import org.example.MusicManagement.model.Music;
import java.sql.*;
import java.util.ArrayList;

public class DB {
    static private final String URL = "jdbc:mysql://localhost:3306/musicLibrary";
    static private final String USERNAME = "root";
    static private final String PASSWORD ="";
    static private final String TABLENAME ="music";

    // Metode untuk mendapatkan koneksi
    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load JDBC driver untuk MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Membuat dan mengembalikan koneksi ke database
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static ArrayList<Music> getDataDB() {
        ArrayList<Music> arrMusic = new ArrayList<>(); // ArrayList untuk menyimpan data

        try (Connection conn = getConnection(); // Memanggil metode getConnection
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT artistName, songName, album, pathSong FROM " + TABLENAME)) {

            // Menampilkan hasil query
            while (rs.next()) {
                String artistName = rs.getString("artistName");
                String songName = rs.getString("songName");
                String album = rs.getString("album");
                String pathSong = rs.getString("pathSong");

                // Membuat objek Music dan menambahkannya ke daftar
                Music music = new Music(songName, artistName, album, pathSong);
                arrMusic.add(music);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }
        return arrMusic;
    }

    // Metode untuk menambahkan data ke database
    public static void addDataToDB(Music music) {
        String sql = "INSERT INTO " + TABLENAME + " (artistName, songName, album, pathSong) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); // Memanggil metode getConnection
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Mengatur parameter pada query
            pstmt.setString(1, music.getArtistName());
            pstmt.setString(2, music.getSongName());
            pstmt.setString(3, music.getAlbum());
            pstmt.setString(4, music.getPathSong());

            // Menjalankan query
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
        }
    }
    public static boolean editToDB(String targetSongName, String newSongName, String newArtistName, String album, String pathSong) {
        // Step 1: Check if the song with targetSongName exists in the database
        String selectQuery = "SELECT songName FROM " + TABLENAME + " WHERE songName = ?";

        try (Connection conn = getConnection();
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            // Set the parameter to the targetSongName to search for it
            selectStmt.setString(1, targetSongName);

            // Execute the SELECT query
            ResultSet rs = selectStmt.executeQuery();

            // Step 2: If the song exists, proceed with the update
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
                return false; // Song not found
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }

        return false; // If any exception occurs or the song is not found
    }

    // Method to delete a song from the database
    public static boolean deleteToDB(String songName) {
        String sql = "DELETE FROM " + TABLENAME + " WHERE songName = ?";

        try (Connection conn = getConnection(); // Calling the getConnection method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameter for the SQL query
            pstmt.setString(1, songName);

            // Execute the DELETE query
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Song successfully deleted from database.");
                return true; // Return true if the song is successfully deleted
            } else {
                System.out.println("No song found with the name: " + songName);
                return false; // Return false if no song with that name was found
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failure!");
            e.printStackTrace();
        }

        return false; // Return false if an error occurs or if the song is not found
    }
}
