/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int roomId;
    private int tenantId;
    private double amount;
    private LocalDate datePaid;
    private LocalDate expireDate;
    private String status;

    private String roomName;
    private String roomLocation;
    private String tenantName;
    private String landlordName;
    private String receiptNumber;
    private String imageName;    // ✅ Hii ndiyo field mpya ya picha ya chumba

    private Room room;

    // ▶️ Getter & Setter za core
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDate getDatePaid() { return datePaid; }
    public void setDatePaid(LocalDate datePaid) { this.datePaid = datePaid; }

    public LocalDate getExpireDate() { return expireDate; }
    public void setExpireDate(LocalDate expireDate) { this.expireDate = expireDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ▶️ Getter & Setter za meta
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getRoomLocation() { return roomLocation; }
    public void setRoomLocation(String roomLocation) { this.roomLocation = roomLocation; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getLandlordName() { return landlordName; }
    public void setLandlordName(String landlordName) { this.landlordName = landlordName; }

    public String getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(String receiptNumber) { this.receiptNumber = receiptNumber; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    // ✅ Getter & Setter mpya ya image
    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}
