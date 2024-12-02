package com.example.nhb_doan.model;

public class Order {
    private int id; // Mã đơn hàng
    private int userId; // Mã người dùng
    private int dishId; // Mã món ăn
    private int quantity; // Số lượng đặt
    private String status; // Trạng thái đơn hàng
    private String shippingAddress; // Địa chỉ nhận hàng
    private String phoneNumber; // Số điện thoại

    // Constructor
    public Order(int id, int userId, int dishId, int quantity, String status, String shippingAddress, String phoneNumber) {
        this.id = id;
        this.userId = userId;
        this.dishId = dishId;
        this.quantity = quantity;
        this.status = status;
        this.shippingAddress = shippingAddress;
        this.phoneNumber = phoneNumber;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getDishId() {
        return dishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

