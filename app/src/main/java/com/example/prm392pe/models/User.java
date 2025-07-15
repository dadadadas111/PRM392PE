package com.example.prm392pe.models;

import java.io.Serializable;

/**
 * Model class representing a user
 */
public class User implements Serializable {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String profileImage;
    private boolean isActive;
    
    // Constructors
    public User() {
    }
    
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    public User(int id, String name, String email, String phone, String profileImage, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profileImage = profileImage;
        this.isActive = isActive;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getProfileImage() {
        return profileImage;
    }
    
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
