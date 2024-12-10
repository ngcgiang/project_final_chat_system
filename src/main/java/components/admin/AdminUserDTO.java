package components.admin;

import java.sql.Timestamp;
import java.util.Date;

public class AdminUserDTO {
    private int userId;
    private String username;
    private String fullName;
    private String address;
    private Date dateOfBirth;
    private String gender;
    private String email;
    private String status;
    private String access;
    private Timestamp createdAt;

    // Default constructor
    public AdminUserDTO() {
        this.userId = 0;
        this.username = "";
        this.fullName = "";
        this.address = "";
        this.dateOfBirth = null;
        this.gender = "";
        this.email = "";
        this.status = "";
        this.access = "yes";
        this.access = "";
        this.createdAt = null;
    }

    // Constructor
    public AdminUserDTO(int userId, String username, String fullName, String address, 
                        Date dateOfBirth, String gender, String email, String status, Timestamp createdAt) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Timestamp getCreatedAt(){
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt){
        this.createdAt = createdAt;
    }
}

