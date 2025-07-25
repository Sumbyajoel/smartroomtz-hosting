/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import DBConnection.BookingDB;
import model.Booking;
import model.User;

public class BookingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loggedUser");

        if (user == null || !"tenant".equals(user.getRole())) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int roomId = Integer.parseInt(request.getParameter("roomId"));
            LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
            LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));

            // 🧠 Validate tarehe
            if (endDate.isBefore(startDate)) {
                request.setAttribute("error", "⛔ Tarehe ya kutoka haiwezi kuwa kabla ya ya kuingia.");
                request.getRequestDispatcher("booking.jsp?roomId=" + roomId).forward(request, response);
                return;
            }

            Booking booking = new Booking();
            booking.setRoomId(roomId);
            booking.setTenantId(user.getUserId());
            booking.setStartDate(startDate);
            booking.setEndDate(endDate);
            booking.setStatus("pending");

            boolean success = BookingDB.addBooking(booking);

            if (success) {
                response.sendRedirect("tenant_bookings.jsp"); // ✅ Tumpeleke hapa baada ya booking
            } else {
                request.setAttribute("error", "⛔ Booking haikufaulu. Jaribu tena.");
                request.getRequestDispatcher("booking.jsp?roomId=" + roomId).forward(request, response);
            }

        } catch (NumberFormatException | DateTimeParseException e) {
            request.setAttribute("error", "⛔ Taarifa za tarehe au chumba si sahihi.");
            request.getRequestDispatcher("booking.jsp").forward(request, response);
        }
    }
}


