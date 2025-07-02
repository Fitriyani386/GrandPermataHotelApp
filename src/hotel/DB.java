package hotel;

import java.sql.*;
import java.util.*;

public class DB {

    public static void checkIn(String nama, String noKamar) {
        try {
            Connection conn = config.configDB();
            String sql = "UPDATE kamar SET nama_tamu=? WHERE no_kamar=? AND nama_tamu IS NULL";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, noKamar);
            int rows = pst.executeUpdate();

            if (rows > 0) {
                // log transaksi
                String logSql = "INSERT INTO transaksi (aksi, nama_tamu, no_kamar) VALUES ('Check-In', ?, ?)";
                PreparedStatement logPst = conn.prepareStatement(logSql);
                logPst.setString(1, nama);
                logPst.setString(2, noKamar);
                logPst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkOut(String noKamar) {
        try {
            Connection conn = config.configDB();
            String nama = getNamaTamu(noKamar);
            if (nama == null) return;

            String sql = "UPDATE kamar SET nama_tamu=NULL WHERE no_kamar=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, noKamar);
            pst.executeUpdate();

            // log transaksi
            String logSql = "INSERT INTO transaksi (aksi, nama_tamu, no_kamar) VALUES ('Check-Out', ?, ?)";
            PreparedStatement logPst = conn.prepareStatement(logSql);
            logPst.setString(1, nama);
            logPst.setString(2, noKamar);
            logPst.executeUpdate();

            ReceiptGenerator.generateReceipt(noKamar);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRoomStatus() {
        StringBuilder sb = new StringBuilder();
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM kamar";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String no = rs.getString("no_kamar");
                String nama = rs.getString("nama_tamu");
                sb.append("Kamar ").append(no).append(" - ")
                  .append(nama == null ? "Tersedia" : "Terisi oleh " + nama).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getTransactionReport() {
        StringBuilder report = new StringBuilder();
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM transaksi ORDER BY waktu DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String waktu = rs.getString("waktu");
                String aksi = rs.getString("aksi");
                String nama = rs.getString("nama_tamu");
                String kamar = rs.getString("no_kamar");

                report.append("[").append(waktu).append("] ")
                      .append(aksi).append(" - ")
                      .append(nama).append(" (Kamar ").append(kamar).append(")\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report.toString();
    }

    public static String[] getAvailableRooms() {
        List<String> rooms = new ArrayList<>();
        try {
            Connection conn = config.configDB();
            String sql = "SELECT no_kamar FROM kamar WHERE nama_tamu IS NULL";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                rooms.add(rs.getString("no_kamar"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms.toArray(new String[0]);
    }

    public static boolean isValidRoom(String room) {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT * FROM kamar WHERE no_kamar=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, room);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAvailable(String room) {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT nama_tamu FROM kamar WHERE no_kamar=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, room);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getString("nama_tamu") == null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getNamaTamu(String noKamar) {
        try {
            Connection conn = config.configDB();
            String sql = "SELECT nama_tamu FROM kamar WHERE no_kamar=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, noKamar);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getString("nama_tamu");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
