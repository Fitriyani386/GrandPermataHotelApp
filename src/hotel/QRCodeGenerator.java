package hotel;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Hashtable;

public class QRCodeGenerator {

    public static void generateQRCode(String data, String filePath) {
        try {
            int width = 300;
            int height = 300;
            String fileType = "png";

            Hashtable<EncodeHintType, Object> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height, hintMap);

            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, fileType, path);

            System.out.println("QR Code berhasil dibuat: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Contoh penggunaan
    public static void main(String[] args) {
        String idKaryawan = "admin"; // Sesuai dengan isi karyawan.txt
        String outputPath = "admin.png";
        generateQRCode(idKaryawan, outputPath);
    }
}
