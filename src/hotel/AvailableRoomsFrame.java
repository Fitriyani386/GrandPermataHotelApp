package hotel;

import javax.swing.*;
import java.awt.*;

public class AvailableRoomsFrame extends JFrame {
    public AvailableRoomsFrame() {
        setTitle("Status Semua Kamar");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JTextArea area = new JTextArea(DB.getRoomStatus());
        area.setFont(new Font("Consolas", Font.PLAIN, 20));
        area.setBackground(new Color(245, 255, 250));
        area.setEditable(false);
        add(new JScrollPane(area), BorderLayout.CENTER);

        ImageIcon homeIcon = new ImageIcon("src/assets/home.png");
        Image scaled = homeIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JButton btnBack = new JButton("Kembali ke Beranda", new ImageIcon(scaled));
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnBack.setBackground(new Color(220, 235, 255));
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setFocusPainted(false);
        btnBack.setHorizontalAlignment(SwingConstants.LEFT);
        btnBack.setIconTextGap(10);

        btnBack.addActionListener(e -> {
            dispose();
            new Dashboard().setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}

