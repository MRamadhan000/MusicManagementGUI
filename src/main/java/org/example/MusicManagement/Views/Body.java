package org.example.MusicManagement.Views;

import org.example.MusicManagement.Controller.MusicController;
import org.example.MusicManagement.Models.Music;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class Body extends JPanel {
    private ArrayList<Music> arrMusic;
    private MusicController musicController;
    private final String PATHBASEIMAGE = "src/main/java/org/example/MusicManagement/publics/album/";
    private final Color BGCOLOR = Color.decode("#212529");
    private final Color BGCOLOR2 = Color.decode("#343a40");
    private final Color TEXTCOLOR = Color.decode("#e0e1dd");
    private final Color TEXTCOLOR2 = Color.decode("#e9ecef");
    public Body(MusicController controller) {
        this.musicController = controller;
        this.arrMusic = controller.getArrMusic();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Mengatur layout vertikal
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding: top, left, bottom, right
        setBackground(BGCOLOR);
        setOpaque(true);
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

        // Mengatur agar cardPanel memiliki background putih
        cardPanel.setOpaque(true); // Set agar background panel terlihat (misal berwarna putih)
        cardPanel.setBackground(BGCOLOR2); // Warna latar belakang putih

        // Set fixed width and height for the card
        cardPanel.setPreferredSize(new Dimension(500, 120)); // Set width to 400px and height to 120px
        cardPanel.setMaximumSize(new Dimension(1500, 120)); // Ensure maximum size is the same

        // Panel untuk menampilkan gambar album
        JLabel albumImage = createAlbumImage(music);
        if (albumImage != null) {
            // Align the album image to the left
            JPanel imagePanel = new JPanel();
            imagePanel.setBackground(BGCOLOR2);
            imagePanel.setOpaque(true);
            imagePanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align to left
            imagePanel.add(albumImage);
            cardPanel.add(imagePanel, BorderLayout.WEST); // Menambahkan gambar di sebelah kiri
        }

        // Panel untuk menampilkan teks informasi musik
        JPanel textPanel = new JPanel();
        textPanel.setBorder(new EmptyBorder(20, 20, 20, 10)); // Padding: top, left, bottom, right
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS)); // Layout vertikal
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align text to the left
        textPanel.setBackground(BGCOLOR2);
        textPanel.setOpaque(true);

        JLabel songNameLabel = new JLabel(music.getSongName());
        songNameLabel.setFont(new Font("Cambria", Font.BOLD, 20)); // Set font untuk song name
        songNameLabel.setForeground(TEXTCOLOR); // Set warna teks

        JLabel artistNameLabel = new JLabel(music.getArtistName());
        artistNameLabel.setFont(new Font("Cambria", Font.ITALIC, 16)); // Set font untuk artist name
        artistNameLabel.setForeground(TEXTCOLOR2); // Set warna teks

        // Menambahkan label ke textPanel
        textPanel.add(songNameLabel);
        textPanel.add(artistNameLabel);

        cardPanel.add(textPanel, BorderLayout.CENTER); // Menambahkan teks di tengah

        // Panel untuk tombol Update dan Delete
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BGCOLOR2);
        buttonPanel.setOpaque(true);

        buttonPanel.setLayout(new GridLayout(2, 1, 5, 5)); // 2 baris, 1 kolom, 5px horizontal dan vertical gap
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 12, 15, 12)); // 10px padding di semua sisi

//        Membuat tombol
        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(100, 50));
        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 50));

        updateButton.setBackground(BGCOLOR); // Set background to BGCOLOR
        updateButton.setForeground(TEXTCOLOR); // Set text color to TEXTCOLORHH1
        updateButton.setBorderPainted(false); // Remove the border

        deleteButton.setBackground(BGCOLOR); // Set background to BGCOLOR
        deleteButton.setForeground(TEXTCOLOR); // Set text color to TEXTCOLORHH1
        deleteButton.setBorderPainted(false); // Remove the border

//        Menambahkan tombol ke dalam panel
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

//        Menambahkan buttonPanel ke dalam cardPanel (di posisi kanan)
        cardPanel.add(buttonPanel, BorderLayout.EAST);


        updateButton.addActionListener(e -> {
            updateAction(music, cardPanel);
        });

        // Tombol Delete
        deleteButton.addActionListener(e -> {
            deleteAction(music, cardPanel);
        });

        // Menambahkan MouseListener ke panel, sehingga seluruh card bisa diklik
        cardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Ubah kursor menjadi "tangan"
        cardPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Logika saat card diklik
                System.out.println("Selected Music: " + music.getPathSong() + ", song name: " + music.getSongName());
                musicController.startPlayMusic(music);
            }
        });

        return cardPanel;
    }

    private void updateAction(Music music, JPanel cardPanel) {
        // Konfigurasi UIManager untuk mendukung warna kustom
        UIManager.put("Panel.background", BGCOLOR);
        UIManager.put("OptionPane.background", BGCOLOR);
        UIManager.put("OptionPane.messageForeground", TEXTCOLOR);
        UIManager.put("Button.background", TEXTCOLOR); // Tombol OK menggunakan TEXTCOLOR
        UIManager.put("Button.foreground", BGCOLOR); // Teks tombol menggunakan BGCOLOR

        // Panel input dengan GridLayout
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(createStyledLabel("Song Name:"));
        panel.add(createStyledTextField(music.getSongName()));
        panel.add(createStyledLabel("Artist Name:"));
        panel.add(createStyledTextField(music.getArtistName()));
        panel.add(createStyledLabel("Album:"));
        panel.add(createStyledTextField(music.getAlbum()));
        panel.add(createStyledLabel("Path:"));
        panel.add(createStyledTextField(music.getPathSong()));

        // Tampilkan dialog JOptionPane
        int option = JOptionPane.showConfirmDialog(
                cardPanel,
                panel,
                "Update Music",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        // Proses pembaruan
        if (option == JOptionPane.OK_OPTION) {
            String newSongName = ((JTextField) panel.getComponent(1)).getText();
            String newArtistName = ((JTextField) panel.getComponent(3)).getText();
            String newAlbum = ((JTextField) panel.getComponent(5)).getText();
            String newPath = ((JTextField) panel.getComponent(7)).getText();

            boolean isUpdated = musicController.updateMusic(music.getSongName(), newSongName, newArtistName, newAlbum, newPath);
            System.out.println(isUpdated ? "Music updated successfully." : "Failed to update music.");
        } else {
            System.out.println("Update cancelled.");
        }
    }
    private void deleteAction(Music music, JPanel cardPanel) {
        // Konfigurasi UIManager untuk mendukung warna kustom
        UIManager.put("Panel.background", BGCOLOR);
        UIManager.put("OptionPane.background", BGCOLOR);
        UIManager.put("OptionPane.messageForeground", TEXTCOLOR);
        UIManager.put("Button.background", TEXTCOLOR); // Tombol OK menggunakan TEXTCOLOR
        UIManager.put("Button.foreground", BGCOLOR); // Teks tombol menggunakan BGCOLOR

        // Panel konfirmasi dengan GridLayout
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JLabel label = new JLabel("Are you sure you want to delete the song: " + music.getSongName() + "?");
        label.setForeground(TEXTCOLOR);
        label.setFont(new Font("Cambria", Font.BOLD, 14));
        panel.add(label);

        // Tampilkan dialog konfirmasi dengan tombol OK dan Cancel
        int option = JOptionPane.showConfirmDialog(
                cardPanel,
                panel,
                "Delete Music",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Proses penghapusan
        if (option == JOptionPane.YES_OPTION) {
            // Logic untuk menghapus lagu
            if (musicController.deleteMusic(music.getSongName())) {
                System.out.println("Music successfully deleted");
            } else {
                System.out.println("Failed to delete music");
            }
        } else {
            System.out.println("No song was deleted.");
        }
    }

    private JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text, 20);
        textField.setBackground(BGCOLOR);
        textField.setForeground(TEXTCOLOR);
        textField.setCaretColor(TEXTCOLOR);
        textField.setBorder(BorderFactory.createLineBorder(TEXTCOLOR, 1, true));
        return textField;
    }


    // Fungsi helper untuk membuat JLabel dengan gaya
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXTCOLOR);
        label.setFont(new Font("Cambria", Font.BOLD, 14));
        return label;
    }

    private JLabel createAlbumImage(Music music) {
        String albumPath = music.getAlbum();
        if (albumPath != null && !albumPath.isEmpty()) {
            String imagePath = PATHBASEIMAGE + albumPath + ".jpg"; // Path for album image
            File imageFile = new File(imagePath);

            // Check if the specific album image exists
            if (imageFile.exists()) {
                ImageIcon albumIcon = new ImageIcon(imagePath);
                Image image = albumIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Resize the image if necessary
                return new JLabel(new ImageIcon(image)); // Return JLabel with the album image
            }
        }

        // If the album image does not exist, load the default image
        String defaultImagePath = PATHBASEIMAGE + "default.png";
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
