// CheckInFrame.java
package hotel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.time.LocalDateTime;

public class CheckInFrame extends JFrame {
    public CheckInFrame() {
        setTitle("Check-In Tamu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        JLabel instruksi = new JLabel(" Pilih kamar tersedia", JLabel.CENTER);
        instruksi.setFont(new Font("Segoe UI", Font.BOLD, 30));
        add(instruksi, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(2, 5, 30, 30));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(40, 100, 60, 100));

        for (int i = 101; i <= 110; i++) {
            String room = String.valueOf(i);
            JButton roomButton = new JButton("Kamar " + room);
            roomButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));

            ImageIcon rawIcon = new ImageIcon("src/assets/hotel.png");
            Image scaled = rawIcon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            roomButton.setIcon(new ImageIcon(scaled));
            roomButton.setHorizontalTextPosition(SwingConstants.CENTER);
            roomButton.setVerticalTextPosition(SwingConstants.BOTTOM);
            roomButton.setFocusPainted(false);

            if (DB.isAvailable(room)) {
                roomButton.setBackground(new Color(144, 238, 144));
                roomButton.setToolTipText("Tersedia");
            } else {
                roomButton.setBackground(new Color(255, 99, 71));
                roomButton.setToolTipText("Terisi oleh " + DB.getNamaTamu(room));
                roomButton.setEnabled(false);
            }

            roomButton.addMouseListener(new MouseAdapter() {
                int clickCount = 0;
                Timer clickTimer = new Timer(400, null);

                {
                    clickTimer.setRepeats(false);
                    clickTimer.addActionListener(e -> clickCount = 0);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    clickCount++;
                    if (clickCount == 2) {
                        showCheckInDialog(room);
                        clickCount = 0;
                        clickTimer.stop();
                    } else {
                        clickTimer.restart();
                    }
                }
            });

            gridPanel.add(roomButton);
        }

        add(gridPanel, BorderLayout.CENTER);

        // Tombol kembali ke dashboard dengan ikon home
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
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void showCheckInDialog(String selectedRoom) {
        String nama = JOptionPane.showInputDialog(this, "Masukkan Nama Tamu untuk kamar " + selectedRoom + ":");

        if (nama != null && !nama.trim().isEmpty()) {
            DB.checkIn(nama.trim(), selectedRoom);
            JOptionPane.showMessageDialog(this, "Check-In berhasil untuk kamar " + selectedRoom);

            // Tampilkan struk dan opsi cetak
            String struk = "GRAND PERMATA HOTEL\n"
                         + "=====================\n"
                         + "Nama Tamu : " + nama + "\n"
                         + "Nomor Kamar : " + selectedRoom + "\n"
                         + "Waktu : " + LocalDateTime.now() + "\n"
                         + "=====================\n";

            int opsi = JOptionPane.showConfirmDialog(this, struk + "\nCetak struk?", "Struk Check-In", JOptionPane.YES_NO_OPTION);

            if (opsi == JOptionPane.YES_OPTION) {
                try (FileWriter writer = new FileWriter("struk_checkin_" + selectedRoom + ".txt")) {
                    writer.write(struk);
                    JOptionPane.showMessageDialog(this, "Struk berhasil disimpan!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Gagal menyimpan struk.");
                }
            }

            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Nama tidak boleh kosong.");
        }
    }
}
