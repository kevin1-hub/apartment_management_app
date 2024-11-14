package com.example.apartmentmanagementapp;

public class User {
    private int id;
    private int userId; // Added userId field
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String passportUri;
    private String idFrontUri;
    private String idBackUri;

    // Default constructor
    public User() {
    }

    // Constructor for creating a new user
    public User(String firstName, String lastName, String email, String password, String role, String passportUri, String idFrontUri, String idBackUri) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.passportUri = passportUri;
        this.idFrontUri = idFrontUri;
        this.idBackUri = idBackUri;
    }

    // Constructor for updating an existing user
    public User(int id, String firstName, String lastName, String email, String password, String role, String passportUri, String idFrontUri, String idBackUri) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.passportUri = passportUri;
        this.idFrontUri = idFrontUri;
        this.idBackUri = idBackUri;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassportUri() {
        return passportUri;
    }

    public void setPassportUri(String passportUri) {
        this.passportUri = passportUri;
    }

    public String getIdFrontUri() {
        return idFrontUri;
    }

    public void setIdFrontUri(String idFrontUri) {
        this.idFrontUri = idFrontUri;
    }

    public String getIdBackUri() {
        return idBackUri;
    }

    public void setIdBackUri(String idBackUri) {
        this.idBackUri = idBackUri;
    }

    public String getPassportPhoto() {
        return "";
    }
}
