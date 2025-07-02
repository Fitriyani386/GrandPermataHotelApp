package hotel;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Dashboard - Grand Permata Hotel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Grand Permata Hotel", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(5, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 200, 80, 200));
        panel.setBackground(new Color(230, 240, 250));

        panel.add(createButton("Check-In Tamu", "src/assets/checkin.png", () -> new CheckInFrame().setVisible(true)));
        panel.add(createButton("Status Semua Kamar", "src/assets/status.png", () -> new AvailableRoomsFrame().setVisible(true)));
        panel.add(createButton("Check-Out Tamu", "src/assets/checkout.png", () -> new CheckOutFrame().setVisible(true)));
        panel.add(createButton("Laporan Transaksi", "src/assets/report.png", () -> new ReportFrame().setVisible(true)));
        panel.add(createButton("Logout", "src/assets/logout.png", () -> {
            dispose();
            new QRLoginFrame().setVisible(true);
        }));

        add(panel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String iconPath, Runnable onClick) {
        ImageIcon rawIcon = new ImageIcon(iconPath);
        Image scaled = rawIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaled);

        JButton btn = new JButton(text, resizedIcon);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(20);
        btn.setBackground(new Color(173, 216, 230));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        btn.addActionListener(e -> onClick.run());
        return btn;
    }
}

