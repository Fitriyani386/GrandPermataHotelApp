package hotel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class QRLoginFrame extends JFrame {
    private Webcam webcam;
    private WebcamPanel panel;

    public QRLoginFrame() {
        setTitle("Login QR - Grand Permata Hotel");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        webcam = Webcam.getDefault();
        webcam.setViewSize(new java.awt.Dimension(320, 240)); // Gunakan awt.Dimension
        panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        add(panel, BorderLayout.CENTER);

        webcam.open();

        // Jalankan pemindaian QR di thread terpisah
        Thread scanThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Delay 1 detik
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                        Map<DecodeHintType, Object> hints = new HashMap<>();
                        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

                        Result result = new MultiFormatReader().decode(bitmap, hints);
                        if (result != null) {
                            String scannedData = result.getText();
                            if (isValidKaryawan(scannedData)) {
                                webcam.close();
                                JOptionPane.showMessageDialog(this, "Login Berhasil!\nSelamat datang, " + scannedData);
                                new Dashboard().setVisible(true);
                                dispose();
                                break;
                            } else {
                                JOptionPane.showMessageDialog(this, "ID Karyawan tidak dikenal: " + scannedData);
                            }
                        }
                    }
                } catch (NotFoundException e) {
                    // QR tidak ditemukan di frame, tidak perlu ditampilkan
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        scanThread.setDaemon(true);
        scanThread.start();
    }

    private boolean isValidKaryawan(String id) {
        try {
            File file = new File("src/karyawan/karyawan.txt");
if (!file.exists()) {
    JOptionPane.showMessageDialog(null, "File tidak ditemukan: " + file.getAbsolutePath());
}

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase(id.trim())) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
