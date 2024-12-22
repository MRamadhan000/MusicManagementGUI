package org.example.MusicManagement.view;
import org.example.MusicManagement.Controller.MusicController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel header;
    private JScrollPane scrollBody; // Ganti body menjadi JScrollPane
    private JPanel footer;
    public MainFrame() {
        setTitle("Music Management App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout()); // Gunakan BorderLayout

        // Inisialisasi awal
        header = new JPanel();
        scrollBody = new JScrollPane(); // Placeholder untuk JScrollPane
        footer = new JPanel();

        // Set ukuran footer agar tidak terlalu besar
        footer.setPreferredSize(new Dimension(getWidth(), 50)); // Atur tinggi footer menjadi 50px

        // Tambahkan komponen awal
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
        remove(scrollBody); // Hapus JScrollPane sebelumnya jika ada
        scrollBody = new JScrollPane(bodyPanel); // Bungkus panel dengan JScrollPane
        scrollBody.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Scroll vertikal otomatis
        scrollBody.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Nonaktifkan scroll horizontal
        add(scrollBody, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setFooter(JPanel footerPanel) {
        remove(footer);
        footer = footerPanel;
        footer.setPreferredSize(new Dimension(getWidth(), 50)); // Ukuran footer tetap 50px
        add(footer, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
