package com.example.flight_booking_app.Activity;

public class User {
    private String username;
    private String email;

    public User() { } // Default constructor required for calls to DataSnapshot.getValue(User.class)

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() { return username; }
    public String getEmail() { return email; }
}