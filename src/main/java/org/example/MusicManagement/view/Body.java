package org.example.MusicManagement.view;

import org.example.MusicManagement.Controller.MusicController;
import org.example.MusicManagement.model.Music;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Body extends JPanel {
    private ArrayList<Music> arrMusic; // Menyimpan daftar musik
    private MusicController musicController;

    public Body(MusicController controller, ArrayList<Music> arrMusic) {
        this.arrMusic = arrMusic;
        this.musicController = controller;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Mengatur layout vertikal
        displayMusicList();
    }

    private void displayMusicList() {
        removeAll(); // Hapus semua komponen sebelumnya (jika ada)

        // Menambahkan detail untuk setiap lagu dalam daftar musik
        for (Music music : arrMusic) {
            // Panggil createCard untuk membuat panel kartu dan tampilkan musik
            JPanel card = createCard(music);

            // Tambahkan card ke Body
            add(card);

            // Tambahkan space (margin) setelah setiap card musik
            add(Box.createVerticalStrut(10)); // Menambahkan jarak 10px antara card musik
        }

        revalidate(); // Refresh UI
        repaint(); // Redraw UI
    }

    // Helper method untuk membuat card-like panel yang bisa diklik
    private JPanel createCard(Music music) {
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Ruang kosong di dalam card

        // Set fixed width and height for the card
        cardPanel.setPreferredSize(new Dimension(500, 120)); // Set width to 400px and height to 120px
        cardPanel.setMaximumSize(new Dimension(1500, 120)); // Ensure maximum size is the same

        // Panel untuk menampilkan gambar album
        JLabel albumImage = createAlbumImage(music);
        if (albumImage != null) {
            // Align the album image to the left
            JPanel imagePanel = new JPanel();
            imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align to left
            imagePanel.add(albumImage);
            cardPanel.add(imagePanel, BorderLayout.WEST); // Menambahkan gambar di sebelah kiri
        }

        // Panel untuk menampilkan teks informasi musik
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Layout vertikal
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align text to the left

        JLabel songNameLabel = new JLabel(music.getSongName());
        songNameLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Set font untuk song name
        songNameLabel.setForeground(Color.BLACK); // Set warna teks

        JLabel artistNameLabel = new JLabel(music.getArtistName());
        artistNameLabel.setFont(new Font("Arial", Font.ITALIC, 14)); // Set font untuk artist name
        artistNameLabel.setForeground(Color.GRAY); // Set warna teks

        // Menambahkan label ke textPanel
        textPanel.add(songNameLabel);
        textPanel.add(artistNameLabel);

        cardPanel.add(textPanel, BorderLayout.CENTER); // Menambahkan teks di tengah

        // Panel untuk tombol Update dan Delete
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Layout tombol di sebelah kanan

        // Tombol Update
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            updateAction(music, cardPanel);
        });

        // Tombol Delete
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            deleteAction(music, cardPanel);
        });

        // Menambahkan tombol ke buttonPanel
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Menambahkan buttonPanel ke kanan cardPanel
        cardPanel.add(buttonPanel, BorderLayout.EAST);

        // Mengatur agar cardPanel memiliki background putih
        cardPanel.setOpaque(true); // Set agar background panel terlihat (misal berwarna putih)
        cardPanel.setBackground(Color.WHITE); // Warna latar belakang putih

        // Menambahkan MouseListener ke panel, sehingga seluruh card bisa diklik
        cardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Ubah kursor menjadi "tangan"
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Logika saat card diklik
                System.out.println("Selected Music: " + music.getPathSong() + ", song name: " + music.getSongName());
                musicController.startPlayMusic(music.getPathSong(), music.getSongName());
                // Anda bisa menambahkan aksi lain yang diinginkan ketika kartu dipilih
            }
        });

        return cardPanel;
    }

    private void updateAction(Music music, JPanel cardPanel) {
        // Show a JOptionPane with 4 input fields for updating music details
        JTextField songNameField = new JTextField(music.getSongName(), 20); // Prefill with current song name
        JTextField artistNameField = new JTextField(music.getArtistName(), 20); // Prefill with current artist name
        JTextField albumField = new JTextField(music.getAlbum(), 20); // Prefill with current album
        JTextField pathField = new JTextField(music.getPathSong(), 20); // Prefill with current path

        // Create a panel to hold the fields
        JPanel panel = new JPanel(new GridLayout(4, 2)); // 4 rows, 2 columns (labels + input fields)
        panel.add(new JLabel("Song Name:"));
        panel.add(songNameField);
        panel.add(new JLabel("Artist Name:"));
        panel.add(artistNameField);
        panel.add(new JLabel("Album:"));
        panel.add(albumField);
        panel.add(new JLabel("Path:"));
        panel.add(pathField);

        // Show the dialog with the panel
        int option = JOptionPane.showConfirmDialog(cardPanel, panel, "Update Music", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        // If the user clicks OK, get the updated values from the text fields
        if (option == JOptionPane.OK_OPTION) {
            String newSongName = songNameField.getText();
            String newArtistName = artistNameField.getText();
            String newAlbum = albumField.getText();
            String newPath = pathField.getText();

            boolean isUpdated = musicController.updateMusic(music.getSongName(),newSongName,newArtistName,newAlbum,newPath);  // Add your actual update logic here
            if (isUpdated) {
                System.out.println("Music updated successfully: " + music.getSongName());
            } else {
                System.out.println("Failed to update music.");
            }
        } else {
            System.out.println("Update cancelled.");
        }
    }

    private void deleteAction(Music music, JPanel cardPanel) {
        // Display a confirmation dialog to verify the deletion
        int response = JOptionPane.showConfirmDialog(cardPanel,
                "Do you want to delete: " + music.getSongName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // If the user clicks "Yes"
        if (response == JOptionPane.YES_OPTION) {
            // Logic to search for and delete the song
            if (musicController.deleteMusic(music.getSongName())) {
                System.out.println("Music successfully deleted");

            } else {
                System.out.println("Failed to delete music");
            }
        } else {
            // If the user clicks "No" or closes the dialog
            System.out.println("No song was deleted.");
        }
    }

    private JLabel createAlbumImage(Music music) {
        String albumPath = music.getAlbum();
        if (albumPath != null && !albumPath.isEmpty()) {
            String imagePath = "src/main/java/org/example/MusicManagement/assets/img/" + albumPath + ".jpg"; // Path for album image
            File imageFile = new File(imagePath);

            // Check if the specific album image exists
            if (imageFile.exists()) {
                ImageIcon albumIcon = new ImageIcon(imagePath);
                Image image = albumIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize the image if necessary
                return new JLabel(new ImageIcon(image)); // Return JLabel with the album image
            }
        }

        // If the album image does not exist, load the default image
        String defaultImagePath = "src/main/java/org/example/MusicManagement/assets/img/default.png";
        File defaultImageFile = new File(defaultImagePath);

        // Check if the default image exists
        if (defaultImageFile.exists()) {
            ImageIcon defaultIcon = new ImageIcon(defaultImagePath);
            Image defaultImage = defaultIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize the default image
            return new JLabel(new ImageIcon(defaultImage)); // Return JLabel with the default image
        }

        return null; // If neither album nor default image exists, return null
    }
}
