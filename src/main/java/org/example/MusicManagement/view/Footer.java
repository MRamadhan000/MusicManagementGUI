package org.example.MusicManagement.view;

import org.example.MusicManagement.Controller.MusicController;
import org.example.MusicManagement.model.Music;
import org.example.MusicManagement.model.MusicPlayer;

import javax.swing.*;
import java.awt.*;

public class Footer extends JPanel {
    private JLabel songNameLabel;  // JLabel to display the current song name
    private JButton pauseButton;   // Button to pause the music
    private JButton resumeButton;  // Button to resume the music

    public Footer(MusicPlayer musicPlayer, String songName) {
        // Set layout and make the panel transparent (no background)
        setLayout(new BorderLayout());
        setOpaque(false);  // No background color, transparent footer

        // Initialize the song name label
        songNameLabel = new JLabel(songName);
        songNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        songNameLabel.setForeground(Color.BLUE);  // Text color

        // Panel to hold the song name label at the left
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.add(songNameLabel);

        // Initialize pause and resume buttons
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");

        // Customize buttons (optional)
        pauseButton.setBackground(new Color(63, 81, 191));
        pauseButton.setForeground(Color.WHITE);
        resumeButton.setBackground(new Color(63, 81, 191));
        resumeButton.setForeground(Color.WHITE);

        // Initially, show only the pause button
        resumeButton.setVisible(false);

        // Panel to hold the buttons at the right
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.add(pauseButton);
        rightPanel.add(resumeButton);

        // Add panels to the footer (song name on the left, buttons on the right)
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        // Add actions for the buttons
        pauseButton.addActionListener(e -> {
            musicPlayer.pauseAudio();
            pauseButton.setVisible(false); // Hide pause button
            resumeButton.setVisible(true); // Show resume button
        });

        resumeButton.addActionListener(e -> {
            musicPlayer.resumeAudio();
            resumeButton.setVisible(false); // Hide resume button
            pauseButton.setVisible(true);  // Show pause button
        });
    }

    public Footer(){
        setLayout(new BorderLayout());
        setOpaque(false);  // No background color, transparent footer
    }

}
