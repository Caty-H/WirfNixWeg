package at.cat.h.wirfnixweg.model;

public class User {

    private String name;
    private String email;
    private String address;
    private String postCode;
    private String phone;
    private String password;

    public User() {
    }

    public User(String name, String address, String postCode, String phone, String email, String password) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.postCode = postCode;
        this.phone = phone;
        this.password = password;
    }

    public User(String name, String address, String postCode, String phone) {
        this.name = name;
        this.address = address;
        this.postCode = postCode;
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
