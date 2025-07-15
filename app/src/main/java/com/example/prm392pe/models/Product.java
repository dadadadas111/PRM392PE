package com.example.prm392pe.models;

import java.io.Serializable;

/**
 * Model class representing a product item
 */
public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private int quantity;
    private boolean isAvailable;
    
    // Constructors
    public Product() {
    }
    
    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
    public Product(int id, String name, String description, double price, String imageUrl, String category, int quantity, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.quantity = quantity;
        this.isAvailable = isAvailable;
    }
    
    // Getters and Setters
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
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", isAvailable=" + isAvailable +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Product product = (Product) obj;
        return id == product.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
