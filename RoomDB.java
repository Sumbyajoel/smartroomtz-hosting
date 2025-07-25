/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBConnection;

import model.Room;
import java.sql.*;
import java.util.*;

public class RoomDB {

    // 1️⃣ Vyumba vyote (admin)
    public static List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name AS landlord_name FROM rooms r JOIN users u ON r.owner_id = u.user_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        } catch (Exception e) {
            System.out.println("❌ getAllRooms() error: " + e.getMessage());
        }

        return rooms;
    }

    // 2️⃣ Vyumba vinavyopatikana (tenant view)
    public static List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name AS landlord_name FROM rooms r JOIN users u ON r.owner_id = u.user_id WHERE r.status = 'available' ORDER BY r.room_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        } catch (Exception e) {
            System.out.println("❌ getAvailableRooms() error: " + e.getMessage());
        }

        return rooms;
    }

    // 3️⃣ Vyumba vya landlord mmoja (dashboard ya landlord)
    public static List<Room> getRoomsByLandlord(int landlordId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name AS landlord_name FROM rooms r JOIN users u ON r.owner_id = u.user_id WHERE r.owner_id = ? ORDER BY r.room_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, landlordId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        } catch (Exception e) {
            System.out.println("❌ getRoomsByLandlord() error: " + e.getMessage());
        }

        return rooms;
    }

    // 4️⃣ Chumba kimoja kwa ID
    public static Room getRoomById(int roomId) {
        String sql = "SELECT r.*, u.full_name AS landlord_name FROM rooms r JOIN users u ON r.owner_id = u.user_id WHERE r.room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRoom(rs);
            }
        } catch (Exception e) {
            System.out.println("❌ getRoomById() error: " + e.getMessage());
        }

        return null;
    }

    public static boolean addRoom(Room room) {
    String sql = "INSERT INTO rooms (room_name, location, price, image_name, amenities, description, status, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, room.getRoomName());
        stmt.setString(2, room.getLocation());
        stmt.setBigDecimal(3, room.getPrice());
        stmt.setString(4, room.getImageName());
        stmt.setString(5, room.getAmenities());
        stmt.setString(6, room.getDescription());
        stmt.setString(7, room.getStatus());
        stmt.setInt(8, room.getOwnerId());

        int affected = stmt.executeUpdate();

        if (affected > 0) {
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                int roomId = keys.getInt(1);
                room.setRoomId(roomId);
                System.out.println("✅ Room added → ID: " + roomId);
            }
            return true;
        }

    } catch (Exception e) {
        System.out.println("❌ addRoom() error: " + e.getMessage());
    }

    return false;
}


    // 6️⃣ Badilisha room info
    public static boolean updateRoom(int roomId, String location, double price, String status) {
        String sql = "UPDATE rooms SET location = ?, price = ?, status = ? WHERE room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location);
            stmt.setDouble(2, price);
            stmt.setString(3, status);
            stmt.setInt(4, roomId);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("❌ updateRoom() error: " + e.getMessage());
        }

        return false;
    }

    // 7️⃣ Futa room
    public static boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            int affected = stmt.executeUpdate();
            System.out.println("🗑️ deleteRoom() → affected rows = " + affected);
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("❌ deleteRoom() error: " + e.getMessage());
            return false;
        }
    }

    // 8️⃣ Converter → Room Object
    private static Room mapRoom(ResultSet rs) throws SQLException {
        Room r = new Room();
        r.setRoomId(rs.getInt("room_id"));
        r.setOwnerId(rs.getInt("owner_id"));
        r.setLocation(rs.getString("location"));
        r.setPrice(rs.getBigDecimal("price"));
        r.setStatus(rs.getString("status"));
        r.setAmenities(rs.getString("amenities"));
        r.setDescription(rs.getString("description"));
        r.setRoomName(rs.getString("room_name"));
        r.setImageName(rs.getString("image_name"));
        r.setLandlordName(rs.getString("landlord_name"));
        return r;
    }

    // 9️⃣ Badilisha room ya landlord mwenyewe
    public static boolean updateRoomByLandlord(int roomId, int landlordId, String location, double price, String status) {
        String sql = "UPDATE rooms SET location = ?, price = ?, status = ? WHERE room_id = ? AND owner_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location);
            stmt.setDouble(2, price);
            stmt.setString(3, status);
            stmt.setInt(4, roomId);
            stmt.setInt(5, landlordId);

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("❌ updateRoomByLandlord() error: " + e.getMessage());
        }

        return false;
    }

    // 🔟 Futa room ya landlord mwenyewe
    public static boolean deleteRoomByLandlord(int roomId, int landlordId) {
        String sql = "DELETE FROM rooms WHERE room_id = ? AND owner_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            stmt.setInt(2, landlordId);
            int affected = stmt.executeUpdate();
            System.out.println("🗑️ deleteRoomByLandlord() → affected rows = " + affected);
            return affected > 0;
        } catch (Exception e) {
            System.out.println("❌ deleteRoomByLandlord() error: " + e.getMessage());
        }

        return false;
    }
}
