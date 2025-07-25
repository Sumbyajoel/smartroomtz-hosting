/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.sql.*;
import DBConnection.DBConnection;

public class ApproveBookingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        int bookingId = Integer.parseInt(request.getParameter("bookingId"));

        String sql = "UPDATE bookings SET status = 'approved' WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                // Optional communicator — simulate SMS/email
                System.out.println("📢 Booking #" + bookingId + " imekubaliwa.");
            }

            response.sendRedirect("landlord_dashboard.jsp");

        } catch (Exception e) {
            response.sendRedirect("error.jsp");
        }
    }
}

