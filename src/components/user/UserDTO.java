package components.user;

import java.util.Date;

public class UserDTO {
    private String username, password, fullName, address;
    private Date dob;
    private String email, phone, gender;
    private String status;

    // Default constructor
    public UserDTO() {
        this.username = "";
        this.password = "";
        this.fullName = "";
        this.address = "";
        this.dob = null;
        this.email = "";
        this.phone = "";
        this.gender = "";
    }

    // Constructor with username and password
    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
        this.fullName = "";
        this.address = "";
        this.dob = null;
        this.email = "";
        this.phone = "";
        this.gender = "";
    }

    // // Full parameterized constructor
    // public UserDTO(String username, String password, String fullName, String
    // address, Date dob, String email, String phone, String gender) {
    // this.username = username;
    // this.password = password;
    // this.fullName = fullName;
    // this.address = address;
    // this.dob = dob;
    // this.email = email;
    // this.phone = phone;
    // this.gender = gender;
    // }

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for fullName
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // Getter and Setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Getter and Setter for dob
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and Setter for gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter and Setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
