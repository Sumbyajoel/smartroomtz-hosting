/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import DBConnection.UserDB;

public class ManageUsersServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("changeRole".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String newRole = request.getParameter("newRole");

            boolean success = UserDB.updateUserRole(userId, newRole);
            if (!success) {
                request.setAttribute("error", "Imeshindikana kubadilisha role.");
            }
        }

        if ("delete".equals(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean success = UserDB.deleteUser(userId);
            if (!success) {
                request.setAttribute("error", "Imeshindikana kufuta mtumiaji.");
            }
        }

        if ("addUser".equals(action)) {
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            User newUser = new User();
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setPassword(password);
            newUser.setRole(role);

            boolean success = UserDB.addUser(newUser);
            if (!success) {
                request.setAttribute("error", "Kuongeza user kumeshindikana.");
            }
        }

        // Redirect to admin_users.jsp regardless of action
        response.sendRedirect("admin_users.jsp");
    }
}
