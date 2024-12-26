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
    private final String PATHBASEICON = "src/main/java/org/example/MusicManagement/publics/icon/";
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
        title.setForeground(TEXTCOLOR);
        title.setFont(new Font("Cambria", Font.BOLD, 23));
        leftPanel.add(title);

//        Panel kanan untuk buttona add
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Panel dengan layout kanan
        rightPanel.setOpaque(false); // Buat transparan

        JButton addButton = new JButton();
        ImageIcon addIcon = new ImageIcon(new ImageIcon(PATHBASEICON + "add.png").getImage().getScaledInstance(26, 26, Image.SCALE_SMOOTH));
        addButton.setIcon(addIcon);
        addButton.setBackground(BGCOLOR); // Warna latar belakang
        addButton.setBorderPainted(false); // Hilangkan garis border
        addButton.setFocusPainted(false); // Hilangkan efek fokus

//        addButton.setOpaque(true); // Pastikan warna latar belakang terlihat

        rightPanel.add(addButton);

        add(leftPanel, BorderLayout.WEST); // Panel kiri
        add(rightPanel, BorderLayout.EAST); // Panel kanan

//        Button action
        addAction(addButton);
    }

    private void addAction(JButton addButton) {
        addButton.addActionListener(e -> {
            // Konfigurasi UIManager untuk mendukung warna kustom
            UIManager.put("Panel.background", BGCOLOR);
            UIManager.put("OptionPane.background", BGCOLOR);
            UIManager.put("OptionPane.messageForeground", TEXTCOLOR);
            UIManager.put("Button.background", TEXTCOLOR); // Tombol OK menggunakan TEXTCOLOR
            UIManager.put("Button.foreground", BGCOLOR); // Teks tombol menggunakan BGCOLOR

            // Panel input dengan GridLayout
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.add(createStyledLabel("Song Name:"));
            JTextField songNameField = createStyledTextField(""); // Kosongkan text field
            panel.add(songNameField);

            panel.add(createStyledLabel("Artist Name:"));
            JTextField artistNameField = createStyledTextField(""); // Kosongkan text field
            panel.add(artistNameField);

            panel.add(createStyledLabel("Album:"));
            JTextField albumField = createStyledTextField(""); // Kosongkan text field
            panel.add(albumField);

            panel.add(createStyledLabel("Path to Song:"));
            JTextField pathSongField = createStyledTextField(""); // Kosongkan text field
            panel.add(pathSongField);

            // Tampilkan dialog JOptionPane
            int option = JOptionPane.showConfirmDialog(this, panel, "Enter Song Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // Proses pembaruan jika tombol OK ditekan
            if (option == JOptionPane.OK_OPTION) {
                String songName = songNameField.getText();
                String artistName = artistNameField.getText();
                String album = albumField.getText();
                String pathSong = pathSongField.getText();

                if (!songName.isEmpty() && !artistName.isEmpty() && !album.isEmpty() && !pathSong.isEmpty()) {
                    Music music = new Music(songName, artistName, album, pathSong);
                    this.musicController.addMusic(music);
                    System.out.println("Music added successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    // Fungsi helper untuk membuat JTextField dengan gaya
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

}
