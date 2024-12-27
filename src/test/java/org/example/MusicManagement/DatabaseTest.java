package org.example.MusicManagement;

import org.example.MusicManagement.Models.Music;
import org.junit.jupiter.api.Test;
import org.example.MusicManagement.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    Database database = new Database();
    @Test
    void testGetAndCloseConn() throws SQLException, ClassNotFoundException {
        // Mendapatkan koneksi dari class Database
        Connection connection = database.getConnection();

        // Validasi bahwa koneksi tidak null
        assertNotNull(connection, "Connection should not be null");

        // Validasi bahwa koneksi masih aktif
        assertTrue(connection.isValid(2), "Connection should be valid");

        // Menutup koneksi setelah selesai
        connection.close();

        // Validasi bahwa koneksi sudah ditutup
        assertTrue(connection.isClosed(), "Connection should be closed");
    }

    @Test
    void testAddDataToDB() throws SQLException, ClassNotFoundException {
        // Data yang akan ditambahkan
        Music music = new Music("a", "b", "c", "d");

        // Memanggil metode addDataToDB
        database.addDataToDB(music);

        // Query untuk memverifikasi data telah ditambahkan
        String query = "SELECT * FROM music WHERE artistName = ? AND songName = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set parameter query
            pstmt.setString(1, music.getArtistName());
            pstmt.setString(2, music.getSongName());

            // Eksekusi query dan validasi hasil
            try (ResultSet rs = pstmt.executeQuery()) {
                assertTrue(rs.next(), "Data harus ada di database."); // Verifikasi data ditemukan

                // Validasi kolom lain (opsional)
                assertEquals(music.getAlbum(), rs.getString("album"), "Album harus sesuai.");
                assertEquals(music.getPathSong(), rs.getString("pathSong"), "Path song harus sesuai.");
            }
        }
    }

    @Test
    void testUpdateAndVerifyAlbum() throws SQLException, ClassNotFoundException {
        // Setup: Tambahkan data awal ke database
        String originalSongName = "e";
        String originalArtistName = "f";
        String originalAlbum = "g";
        String originalPathSong = "h";

        Music originalMusic = new Music(originalSongName, originalArtistName, originalAlbum, originalPathSong);
        database.addDataToDB(originalMusic);

        String newSongName = "i";
        String newArtistName = "j";
        String newAlbum = "k";
        String newPathSong = "l";

        boolean isUpdated = database.editToDB(originalSongName, newSongName, newArtistName, newAlbum, newPathSong);

        String query = "SELECT * FROM music WHERE songName = ?";

        boolean dataExistsBeforeDelete = false;

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Menyiapkan parameter untuk query
            pstmt.setString(1, newSongName);

            // Menjalankan query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Verifikasi setiap baris hasil query sebelum dihapus
                while (rs.next()) {
                    String songName = rs.getString("songName");
                    if (newSongName.equals(songName)) {
                        dataExistsBeforeDelete = true;
                        break;
                    }
                }
            }
        }
        assertTrue(dataExistsBeforeDelete, "Gagal");
    }


    @Test
    void testDeleteAndVerify() throws SQLException, ClassNotFoundException {
        // Setup: Tambahkan data awal ke database
        String originalSongName = "m";
        String originalArtistName = "n";
        String originalAlbum = "o"; // Album spesifik untuk tes
        String originalPathSong = "p";

        Music originalMusic = new Music(originalSongName, originalArtistName,originalAlbum, originalPathSong);
        database.addDataToDB(originalMusic);

        boolean delete = database.deleteToDB(originalSongName);

        String query = "SELECT * FROM music WHERE songName = ?";

        boolean dataExistsBeforeDelete = false;

        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Menyiapkan parameter untuk query
            pstmt.setString(1, originalSongName);

            // Menjalankan query
            try (ResultSet rs = pstmt.executeQuery()) {
                // Verifikasi setiap baris hasil query sebelum dihapus
                while (rs.next()) {
                    String songName = rs.getString("songName");
                    // Pastikan data ada sebelum dihapus
                    if (originalSongName.equals(songName)) {
                        dataExistsBeforeDelete = true;
                        break;
                    }
                }
            }
        }
        assertFalse(dataExistsBeforeDelete, "Ggagal");
    }

}