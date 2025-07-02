package hotel;

import javax.swing.*;
import java.awt.*;

public class CheckOutFrame extends JFrame {
    public CheckOutFrame() {
        setTitle("Check-Out Tamu");
        setSize(600, 400); // Ukuran normal
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(255, 248, 220));

        JPanel inputPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 0, 40));
        inputPanel.setBackground(new Color(255, 248, 220));

        JLabel label = new JLabel("Nomor Kamar:");
        JTextField roomField = new JTextField();

        inputPanel.add(label);
        inputPanel.add(roomField);

        ImageIcon rawIcon = new ImageIcon("src/assets/checkout.png");
        Image scaled = rawIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaled);

        JButton submit = new JButton("Check-Out", resizedIcon);
        submit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submit.setBackground(new Color(255, 182, 193));
        submit.setCursor(new Cursor(Cursor.HAND_CURSOR));

        submit.addActionListener(e -> {
            String room = roomField.getText();
            if (!DB.isValidRoom(room)) {
                JOptionPane.showMessageDialog(this, "Nomor kamar tidak valid!");
                return;
            }
            if (DB.isAvailable(room)) {
                JOptionPane.showMessageDialog(this, "Kamar ini sudah kosong.");
                return;
            }
            DB.checkOut(room);
            JOptionPane.showMessageDialog(this, "Tamu berhasil check-out dan struk dicetak.");
            dispose();
        });

        add(inputPanel, BorderLayout.CENTER);
        add(submit, BorderLayout.SOUTH);

        ImageIcon homeIcon = new ImageIcon("src/assets/home.png");
        Image scaledHome = homeIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        JButton btnBack = new JButton("Kembali ke Beranda", new ImageIcon(scaledHome));
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
        add(bottomPanel, BorderLayout.NORTH);
    }
}
