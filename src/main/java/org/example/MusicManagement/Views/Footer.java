package org.example.MusicManagement.Views;

import org.example.MusicManagement.Controller.MusicController;
import org.example.MusicManagement.Models.MusicPlayer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Footer extends JPanel {
    private JLabel songNameLabel;  // JLabel to display the current song name
    private JButton pauseButton;   // Button to pause the music
    private JButton resumeButton;  // Button to resume the music
    private final Color BGCOLOR = new Color(0x01161E);
    private final Color TEXTCOLOR = Color.decode("#e0e1dd");

    // footer when music is not playing
    public Footer(){
        setLayout(new BorderLayout());
        setBackground(BGCOLOR);
        setOpaque(true);
    }

    // footer when music is playing
    public Footer(MusicController musicController, String songName) {
        // Set layout and make the panel transparent (no background)
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 20, 10, 65)); // Padding: top, left, bottom, right
        setBackground(BGCOLOR);
        setOpaque(true);

        // Panel left to songname
        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);

        // Initialize the song name label
        songNameLabel = new JLabel(songName);
        songNameLabel.setFont(new Font("Cambria", Font.BOLD, 20));
        songNameLabel.setForeground(TEXTCOLOR);  // Text color

        leftPanel.add(songNameLabel);

        // Panel right to button
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);

        // Initialize pause and resume buttons
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");

        pauseButton.setFont( new Font ("Cambria",Font.BOLD,15));

        // Customize buttons (optional)
        pauseButton.setBackground(BGCOLOR); // Set background to BGCOLOR
        pauseButton.setForeground(TEXTCOLOR); // Set text color to TEXTCOLORHH1
        pauseButton.setBorderPainted(false); // Remove the border

        resumeButton.setBackground(BGCOLOR); // Set background to BGCOLOR
        resumeButton.setForeground(TEXTCOLOR); // Set text color to TEXTCOLORHH1
        resumeButton.setBorderPainted(false); // Remove the border

        // Initially, show only the pause button
        resumeButton.setVisible(false);

        rightPanel.add(pauseButton);
        rightPanel.add(resumeButton);

        // Add panels to the footer (song name on the left, buttons on the right)
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        pauseButton.addActionListener(e -> {
            musicController.pauseAudio();
            pauseButton.setVisible(false);
            resumeButton.setVisible(true);
        });

        resumeButton.addActionListener(e -> {
            musicController.resumeAudio();
            resumeButton.setVisible(false);
            pauseButton.setVisible(true);
        });
    }
}
