/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBConnection;

import java.sql.*;
import java.util.*;
import model.User;

public class UserDB {

    // 1️⃣ Chukua users wote
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY user_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ getAllUsers() error: " + e.getMessage());
        }

        return users;
    }

    // 2️⃣ Hesabu users kwa role
    public static int countUsersByRole(String role) {
        String sql = "SELECT COUNT(*) FROM users WHERE role = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.out.println("❌ countUsersByRole() error: " + e.getMessage());
        }

        return 0;
    }

    // 3️⃣ Badilisha role ya user
    public static boolean updateUserRole(int userId, String newRole) {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newRole);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ updateUserRole() error: " + e.getMessage());
        }

        return false;
    }

    // 4️⃣ Futa user (isipokuwa admin)
    public static boolean deleteUser(int userId) {
        String roleCheck = "SELECT role FROM users WHERE user_id = ?";
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(roleCheck)) {

            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && "admin".equalsIgnoreCase(rs.getString("role"))) {
                System.out.println("🚫 deleteUser() → admin haifutiki");
                return false;
            }

            try (PreparedStatement deleteStmt = conn.prepareStatement(sql)) {
                deleteStmt.setInt(1, userId);
                return deleteStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            System.out.println("❌ deleteUser() error: " + e.getMessage());
        }

        return false;
    }

    // 5️⃣ Ongeza user mpya (kinga ya email duplicate)
    public static boolean addUser(User newUser) {
        if (emailExists(newUser.getEmail())) {
            System.out.println("⚠️ addUser() → email tayari ipo: " + newUser.getEmail());
            return false;
        }

        String sql = "INSERT INTO users (full_name, email, phone, password, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newUser.getFullName());
            stmt.setString(2, newUser.getEmail());
            stmt.setString(3, newUser.getPhone());
            stmt.setString(4, newUser.getPassword());
            stmt.setString(5, newUser.getRole());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("❌ addUser() error: " + e.getMessage());
        }

        return false;
    }

    // 6️⃣ Login kwa email + password
    public static User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            stmt.setString(2, password.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }

        } catch (SQLException e) {
            System.out.println("❌ login() error: " + e.getMessage());
        }

        return null;
    }

    // 7️⃣ Usajili mpya
    public static boolean register(User newUser) {
        return addUser(newUser);
    }

    // 8️⃣ Check kama email ipo tayari
    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.out.println("❌ emailExists() error: " + e.getMessage());
        }

        return false;
    }

    // 9️⃣ Chukua user kwa email
    public static User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email.trim());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }

        } catch (SQLException e) {
            System.out.println("❌ getUserByEmail() error: " + e.getMessage());
        }

        return null;
    }

    // 🔄 Utility: resultSet → User
    private static User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
