package DBConnection;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class AdminDB {

    private static int countQuery(String sql) {
        int count = 0;
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
        }
        return count;
    }

    // 👥 Count users
    public static int countAllUsers() {
        return countQuery("SELECT COUNT(*) FROM users");
    }

    public static int countUsersByRole(String role) {
        return countQuery("SELECT COUNT(*) FROM users WHERE role = '" + role + "'");
    }

    // 🏠 Count rooms
    public static int countAllRooms() {
        return countQuery("SELECT COUNT(*) FROM rooms");
    }

    // 📆 Count bookings
    public static int countAllBookings() {
        return countQuery("SELECT COUNT(*) FROM bookings");
    }

    public static int countPendingBookings() {
        return countQuery("SELECT COUNT(*) FROM bookings WHERE status = 'pending'");
    }

    public static int countApprovedBookings() {
        return countQuery("SELECT COUNT(*) FROM bookings WHERE status = 'approved'");
    }

    // 💳 Count payments
    public static int countAllPayments() {
        return countQuery("SELECT COUNT(*) FROM payments");
    }

    public static int totalPaymentAmount() {
        int total = 0;
        String sql = "SELECT SUM(amount) FROM payments";
        try (java.sql.Connection conn = DBConnection.getConnection();
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
        }
        return total;
    }

    // 📅 Optional: count bookings today
    public static int countBookingsToday() {
        return countQuery("SELECT COUNT(*) FROM bookings WHERE DATE(start_date) = CURRENT_DATE");
    }

    public static int countPaymentsToday() {
        return countQuery("SELECT COUNT(*) FROM payments WHERE DATE(date_paid) = CURRENT_DATE");
    }



}

