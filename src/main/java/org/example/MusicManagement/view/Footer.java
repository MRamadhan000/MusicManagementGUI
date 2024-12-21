package org.example.MusicManagement.view;

import org.example.MusicManagement.Controller.MusicController;
import org.example.MusicManagement.model.Music;

import javax.swing.*;
import java.awt.*;

public class Footer extends JPanel {
    private MusicController musicController;

    public Footer(MusicController musicController) {
        this.musicController = musicController;  // Store controller reference

        setLayout(new BorderLayout());  // Use BorderLayout for positioning components
        setOpaque(false);  // Transparent panel

        // Create button with icon
        ImageIcon originalIcon = new ImageIcon("src/main/java/org/example/MusicManagement/assets/icon/add.png");

        // Resize the icon to a smaller size (e.g., 24x24 pixels)
        ImageIcon scaledIcon = new ImageIcon(originalIcon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH));
        JButton addButton = new JButton(scaledIcon);

        // Set button to be transparent
        addButton.setContentAreaFilled(false);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);

        // Add button to footer at the bottom-right
        add(addButton, BorderLayout.SOUTH);  // Position at the bottom-right corner

        // Button action
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
                // Get data from text fields
                String songName = songNameField.getText();
                String artistName = artistNameField.getText();
                String album = albumField.getText();
                String pathSong = pathSongField.getText();

                // Check if all fields are filled
                if (!songName.isEmpty() && !artistName.isEmpty() && !album.isEmpty() && !pathSong.isEmpty()) {
                    // Call addMusic method from controller
                    Music music = new Music(songName, artistName, album, pathSong);
                    this.musicController.addMusic(music);
                } else {
                    // If fields are empty, show error message
                    JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
