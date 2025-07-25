/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBConnection;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import model.Payment;
import model.Room;

public class PaymentDB {

    // ✅ Rekodi malipo mpya (real or simulated)
    public static boolean addPayment(Payment payment) {
        if ("simulated".equals(payment.getStatus())) {
            payment.setReceiptNumber("SIM-" + System.currentTimeMillis());
            System.out.println("ℹ️ Simulated payment accepted visually: " + payment.getReceiptNumber());
            return true;
        }

        String sql = "INSERT INTO payments (room_id, tenant_id, amount, date_paid, expire_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getRoomId());
            stmt.setInt(2, payment.getTenantId());
            stmt.setDouble(3, payment.getAmount());
            stmt.setDate(4, Date.valueOf(payment.getDatePaid()));
            stmt.setDate(5, Date.valueOf(payment.getExpireDate()));
            stmt.setString(6, payment.getStatus());

            System.out.println("✅ addPayment() success for tenant ID: " + payment.getTenantId());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ addPayment() error: " + e.getMessage());
            return false;
        }
    }

    // 📦 Malipo ya tenant mmoja
    public static List<Payment> getPaymentsForTenant(int tenantId) {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT p.*, r.room_name, r.location, r.image_name, u.full_name AS landlord_name " +
                     "FROM payments p " +
                     "JOIN rooms r ON p.room_id = r.room_id " +
                     "JOIN users u ON r.owner_id = u.user_id " +
                     "WHERE p.tenant_id = ? ORDER BY p.date_paid DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tenantId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payment p = mapPaymentWithRoom(rs);
                p.setTenantId(tenantId);  // context
                payments.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ getPaymentsForTenant() error: " + e.getMessage());
        }

        return payments;
    }

    // 👀 Malipo ya landlord (rooms zake tu)
    public static List<Payment> getPaymentsForLandlord(int landlordId) {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT p.*, r.room_name, r.location, u.full_name AS tenant_name " +
                     "FROM payments p " +
                     "JOIN rooms r ON p.room_id = r.room_id " +
                     "JOIN users u ON p.tenant_id = u.user_id " +
                     "WHERE r.owner_id = ? ORDER BY p.date_paid DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, landlordId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setRoomId(rs.getInt("room_id"));
                p.setTenantId(rs.getInt("tenant_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setDatePaid(rs.getDate("date_paid").toLocalDate());
                p.setExpireDate(rs.getDate("expire_date").toLocalDate());
                p.setStatus(rs.getString("status"));
                p.setRoomName(rs.getString("room_name"));
                p.setRoomLocation(rs.getString("location"));
                p.setTenantName(rs.getString("tenant_name"));
                payments.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ getPaymentsForLandlord() error: " + e.getMessage());
        }

        return payments;
    }

    // 🧑‍💼 Malipo yote ya mfumo (admin view)
    public static List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT p.*, r.room_name, r.location, u.full_name AS tenant_name, o.full_name AS landlord_name " +
                     "FROM payments p " +
                     "JOIN rooms r ON p.room_id = r.room_id " +
                     "JOIN users u ON p.tenant_id = u.user_id " +
                     "JOIN users o ON r.owner_id = o.user_id " +
                     "ORDER BY p.payment_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Payment p = new Payment();
                p.setPaymentId(rs.getInt("payment_id"));
                p.setRoomId(rs.getInt("room_id"));
                p.setTenantId(rs.getInt("tenant_id"));
                p.setAmount(rs.getDouble("amount"));
                p.setDatePaid(rs.getDate("date_paid").toLocalDate());
                p.setExpireDate(rs.getDate("expire_date").toLocalDate());
                p.setStatus(rs.getString("status"));
                p.setRoomName(rs.getString("room_name"));
                p.setRoomLocation(rs.getString("location"));
                p.setTenantName(rs.getString("tenant_name"));
                p.setLandlordName(rs.getString("landlord_name"));
                payments.add(p);
            }

        } catch (SQLException e) {
            System.out.println("❌ getAllPayments() error: " + e.getMessage());
        }

        return payments;
    }

    // 📊 Jumla ya malipo yote — kwa dashboard ya admin
    public static double getTotalAmount() {
        String sql = "SELECT SUM(amount) AS total FROM payments";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            System.out.println("❌ getTotalAmount() error: " + e.getMessage());
        }

        return 0.0;
    }

    // 🧩 Convert ResultSet → Payment + Room Info
    private static Payment mapPaymentWithRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomName(rs.getString("room_name"));
        room.setLocation(rs.getString("location"));
        room.setImageName(rs.getString("image_name"));
        room.setLandlordName(rs.getString("landlord_name"));

        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setRoomId(rs.getInt("room_id"));
        p.setTenantId(rs.getInt("tenant_id"));
        p.setAmount(rs.getDouble("amount"));
        p.setDatePaid(rs.getDate("date_paid").toLocalDate());
        p.setExpireDate(rs.getDate("expire_date").toLocalDate());
        p.setStatus(rs.getString("status"));
        p.setRoom(room);
        p.setRoomName(room.getRoomName());
        p.setRoomLocation(room.getLocation());
        p.setLandlordName(room.getLandlordName());

        return p;
    }
}


