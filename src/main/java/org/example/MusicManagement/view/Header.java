package org.example.MusicManagement.view;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {
    public Header(){
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(63,81,191));

        JLabel title = new JLabel("Music Library");
        title.setForeground(Color.white);
        title.setFont(new Font("Arial",Font.BOLD,18));
        add(title);
    }
}
