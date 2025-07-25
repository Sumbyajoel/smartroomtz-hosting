/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private int roomId;
    private String roomName;       // Jina la chumba kilicho-bookiwa
private String landlordName;   // Jina la landlord (kwa admin view au logs)

    private int tenantId;
    private LocalDate bookingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    // ✅ Nyongeza kwa JSP
    private String tenantName;
    private String roomLocation;
    private Room room; // kwa booking.getRoom()

    // --- Getters and Setters ---

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenantName() {
        return tenantName;
    }
    public String getRoomName() {
    return roomName;
}

public void setRoomName(String roomName) {
    this.roomName = roomName;
}

public String getLandlordName() {
    return landlordName;
}

public void setLandlordName(String landlordName) {
    this.landlordName = landlordName;
}


    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getRoomLocation() {
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        this.roomLocation = roomLocation;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}


