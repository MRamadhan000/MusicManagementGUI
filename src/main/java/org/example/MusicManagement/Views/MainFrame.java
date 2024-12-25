package org.example.MusicManagement.Views;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel header;
    private JScrollPane scrollBody;
    private JPanel footer;
    public MainFrame() {
        setTitle("Music Management App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // initial initialization
        header = new JPanel();
        scrollBody = new JScrollPane();
        footer = new JPanel();

        // Set size footer
        footer.setPreferredSize(new Dimension(getWidth(), 50)); // Set the footer height to 50px

        // Add components
        add(header, BorderLayout.NORTH);
        add(scrollBody, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    public void setHeader(JPanel headerPanel) {
        remove(header);
        header = headerPanel;
        add(header, BorderLayout.NORTH);
        revalidate();
        repaint();
    }

    public void setBody(JPanel bodyPanel) {
        remove(scrollBody); // remove JScrollPane if exist
        scrollBody = new JScrollPane(bodyPanel); // wrap with JScrollPane
        scrollBody.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Scroll vertical
        scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scrolling
        add(scrollBody, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setFooter(JPanel footerPanel) {
        remove(footer);
        footer = footerPanel;
        footer.setPreferredSize(new Dimension(getWidth(), 50)); // Footer height 50 px
        add(footer, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
