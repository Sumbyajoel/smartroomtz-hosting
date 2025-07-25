/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;
import java.io.*;
import java.nio.file.Paths;

import DBConnection.RoomDB;
import model.Room;

@MultipartConfig
public class AddRoomServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String location = request.getParameter("location");
        double price = Double.parseDouble(request.getParameter("price"));
        String status = request.getParameter("status");
        int ownerId = Integer.parseInt(request.getParameter("ownerId"));

        // 🖼️ Handle image
        Part imagePart = request.getPart("imageFile");
        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
        String uploadPath = getServletContext().getRealPath("") + "images";

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        imagePart.write(uploadPath + File.separator + fileName);

        Room room = new Room();
        room.setLocation(location);
        room.setPrice(price);
        room.setStatus(status);
        room.setOwnerId(ownerId);
        room.setImageName(fileName); // 🧠 Save filename in DB

        boolean success = RoomDB.addRoom(room);
        if (success) {
            response.sendRedirect("landlord_dashboard.jsp");
        } else {
            response.getWriter().println("⛔ Kushindwa kuongeza chumba.");
        }
    }
}

