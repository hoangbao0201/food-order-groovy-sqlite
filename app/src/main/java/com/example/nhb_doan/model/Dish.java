package com.example.nhb_doan.model; // Đảm bảo tên gói chính xác với dự án của bạn

public class Dish {
    private int id;
    private String name;
    private String description;
    private String imageUri;

    // Constructor
    public Dish(int id, String name, String description, String imageUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUri = imageUri;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    // Optional: Override toString() method to help with logging or displaying dish info
    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
