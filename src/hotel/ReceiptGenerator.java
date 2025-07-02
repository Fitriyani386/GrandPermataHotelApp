package hotel;

import java.io.FileWriter;
import java.io.IOException;

public class ReceiptGenerator {
    public static void generateReceipt(String room) {
        try (FileWriter writer = new FileWriter("struk_" + room + ".txt")) {
            writer.write("Grand Permata Hotel\n");
            writer.write("Struk Check-Out\n");
            writer.write("Kamar: " + room + "\n");
            writer.write("Terima kasih telah menginap!\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}