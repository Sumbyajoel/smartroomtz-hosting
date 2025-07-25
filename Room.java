/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

public class Room {
    private int roomId;
    private int ownerId;
    private String location;
    private BigDecimal price;
    private String amenities;
    private String status;
    private String description;
    private String roomName;
    private String landlordName;
    private String imageName;

    // 🧱 Constructors
    public Room() { }

    public Room(int roomId, int ownerId, String location, BigDecimal price,
                String amenities, String status, String description,
                String roomName, String landlordName, String imageName) {

        this.roomId = roomId;
        this.ownerId = ownerId;
        this.location = location;
        this.price = price;
        this.amenities = amenities;
        this.status = status;
        this.description = description;
        this.roomName = roomName;
        this.landlordName = landlordName;
        this.imageName = imageName;
    }

    // 🔎 Getters and Setters
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    // ✅ Optional: set price from double for convenience
    public void setPrice(double price) {
        this.price = BigDecimal.valueOf(price);
    }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    public String getLandlordName() { return landlordName; }
    public void setLandlordName(String landlordName) { this.landlordName = landlordName; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
}


