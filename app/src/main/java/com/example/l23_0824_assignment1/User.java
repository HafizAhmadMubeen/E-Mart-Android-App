package com.example.l23_0824_assignment1;

public class User {
    public String fullName, phone, gender, country, address, accountType;

    public User() {} // Required for Firebase

    public User(String fullName, String phone, String gender, String country, String address, String accountType) {
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.country = country;
        this.address = address;
        this.accountType = accountType;
    }
}