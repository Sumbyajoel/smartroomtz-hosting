/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBConnection;

import java.sql.*;
import java.util.*;
import model.Booking;
import model.Room;

public class BookingDB {

    // ✅ 1. Rekodi booking mpya
    public static boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (room_id, tenant_id, booking_date, status) " +
                     "VALUES (?, ?, CURDATE(), ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getRoomId());
            stmt.setInt(2, booking.getTenantId());
            stmt.setString(3, booking.getStatus());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ addBooking() error: " + e.getMessage());
            return false;
        }
    }

    // 📋 2. Bookings za tenant
    public static List<Booking> getBookingsForTenant(int tenantId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.room_name, r.location, r.price, r.image_name, " +
                     "u.full_name AS landlord_name " +
                     "FROM bookings b " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "JOIN users u ON r.owner_id = u.user_id " +
                     "WHERE b.tenant_id = ? ORDER BY b.booking_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tenantId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapBooking(rs, true));
            }

        } catch (SQLException e) {
            System.out.println("❌ getBookingsForTenant() error: " + e.getMessage());
        }

        return bookings;
    }

    // 🧑‍💼 3. Bookings zote kwa admin
    public static List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.room_name, r.location, r.price, r.image_name, " +
                     "u.full_name AS landlord_name, t.full_name AS tenant_name " +
                     "FROM bookings b " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "JOIN users u ON r.owner_id = u.user_id " +
                     "JOIN users t ON b.tenant_id = t.user_id " +
                     "ORDER BY b.booking_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapBooking(rs, true));
            }

        } catch (SQLException e) {
            System.out.println("❌ getAllBookings() error: " + e.getMessage());
        }

        return bookings;
    }

    // 🏠 4. Bookings za landlord
    public static List<Booking> getBookingsForLandlord(int landlordId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.room_name, r.location, r.price, r.image_name, " +
                     "t.full_name AS tenant_name " +
                     "FROM bookings b " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "JOIN users t ON b.tenant_id = t.user_id " +
                     "WHERE r.owner_id = ? ORDER BY b.booking_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, landlordId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(mapBooking(rs, false));
            }

        } catch (SQLException e) {
            System.out.println("❌ getBookingsForLandlord() error: " + e.getMessage());
        }

        return bookings;
    }

    // 🧩 Utility: Convert ResultSet → Booking
    private static Booking mapBooking(ResultSet rs, boolean includeLandlord) throws SQLException {
        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomName(rs.getString("room_name"));
        room.setLocation(rs.getString("location"));
        room.setPrice(rs.getDouble("price"));
        room.setImageName(rs.getString("image_name"));

        if (includeLandlord && hasColumn(rs, "landlord_name")) {
            room.setLandlordName(rs.getString("landlord_name"));
        }

        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setRoomId(rs.getInt("room_id"));
        b.setTenantId(rs.getInt("tenant_id"));
        b.setBookingDate(rs.getDate("booking_date").toLocalDate());
        b.setStatus(rs.getString("status"));
        b.setRoom(room);

        if (hasColumn(rs, "tenant_name")) {
            b.setTenantName(rs.getString("tenant_name"));
        }

        return b;
    }

    // 🧠 Check kama column ipo
    private static boolean hasColumn(ResultSet rs, String columnName) {
        try {
            rs.findColumn(columnName);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean confirmBooking(int bookingId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

