package org.example.MusicManagement.Views;

import org.example.MusicManagement.Controller.MusicController;
import org.example.MusicManagement.Models.Music;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    private MusicController musicController;
    public Header(MusicController musicController) {
        this.musicController = musicController;

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(63, 81, 191));

        // Title label
        JLabel title = new JLabel("Music Library");
        title.setForeground(Color.white);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);

        // Create the "Add" button
        JButton addButton = new JButton("Add");

        // Customize the button's appearance
        addButton.setBackground(new Color(63, 81, 191));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);

        // Add button to the header
        add(addButton);

        // Button action
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
