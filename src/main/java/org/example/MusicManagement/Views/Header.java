package org.example.MusicManagement.Views;

import org.example.MusicManagement.Controller.MusicController;
import org.example.MusicManagement.Models.Music;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Header extends JPanel {
    private MusicController musicController;
    private final Color BGCOLOR = Color.decode("#212529");
    private final Color TEXTCOLOR = Color.decode("#e0e1dd");
    public Header(MusicController musicController) {
        this.musicController = musicController;

        setBorder(new EmptyBorder(10, 20, 10, 65)); // Padding: top, left, bottom, right
        setLayout(new BorderLayout()); // Gunakan BorderLayout
        setBackground(BGCOLOR);
        setOpaque(true);

//        Panel kiri untuk Music Library
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel dengan layout kiri
        leftPanel.setOpaque(false); // Buat transparan

        JLabel title = new JLabel("Music Library");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Cambria", Font.BOLD, 23));
        leftPanel.add(title);

//        Panel kanan untuk buttona add
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panel dengan layout kanan
        rightPanel.setOpaque(false); // Buat transparan

        JButton addButton = new JButton("Add");
        addButton.setFont(new Font("Cambria", Font.BOLD, 17));
        addButton.setBackground(BGCOLOR); // Warna latar belakang
        addButton.setForeground(TEXTCOLOR); // Warna teks

        addButton.setBorderPainted(false); // Hilangkan garis border
        addButton.setFocusPainted(false); // Hilangkan efek fokus
        addButton.setContentAreaFilled(false); // Hilangkan area isi default tombol
        addButton.setOpaque(true); // Pastikan warna latar belakang terlihat

        rightPanel.add(addButton);

        add(leftPanel, BorderLayout.WEST); // Panel kiri
        add(rightPanel, BorderLayout.EAST); // Panel kanan

//        Button action
        addAction(addButton);
    }

    private void addAction(JButton addButton) {
        addButton.addActionListener(e -> {
            // Create panel for inputs
            JPanel panel = new JPanel(new GridLayout(4, 2));

            // Create text fields for songName, artistName, album, and pathSong
            JTextField songNameField = new JTextField();
            JTextField artistNameField = new JTextField();
            JTextField albumField = new JTextField();
            JTextField pathSongField = new JTextField();

            // Add components to the panel
            panel.add(new JLabel("Song Name:"));
            panel.add(songNameField);
            panel.add(new JLabel("Artist Name:"));
            panel.add(artistNameField);
            panel.add(new JLabel("Album Name:"));
            panel.add(albumField);
            panel.add(new JLabel("Path to Song:"));
            panel.add(pathSongField);

            // Show dialog for input
            int option = JOptionPane.showConfirmDialog(this, panel, "Enter Song Details", JOptionPane.OK_CANCEL_OPTION);

            // If user presses OK
            if (option == JOptionPane.OK_OPTION) {
                String songName = songNameField.getText();
                String artistName = artistNameField.getText();
                String album = albumField.getText();
                String pathSong = pathSongField.getText();

                if (!songName.isEmpty() && !artistName.isEmpty() && !album.isEmpty() && !pathSong.isEmpty()) {
                    Music music = new Music(songName, artistName, album, pathSong);
                    this.musicController.addMusic(music);
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
