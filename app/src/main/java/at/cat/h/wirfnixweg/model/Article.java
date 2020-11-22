package at.cat.h.wirfnixweg.model;

import java.io.Serializable;

public class Article implements Serializable {

    private String id;
    private String articleName;
    private double unit;
    private double price;

    private String userName;
    private String userPhone;
    private String userAddress;
    private String userEmail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Article getUserInformation;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Article() {
    }

    public Article(String articleName, double unit, double price) {
        this.articleName = articleName;
        this.unit = unit;
        this.price = price;
    }

    public String getArticleName() {
        return articleName;
    }

    public void setArticleName(String articleName) {
        this.articleName = articleName;
    }

    public double getUnit() {
        return unit;
    }

    public void setUnit(double unit) {
        this.unit = unit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUserInformation(User user) {
        this.userName = user.getName();
        this.userAddress = user.getAddress();
        this.userPhone = user.getPhone();
        this.userEmail = user.getEmail();
    }
}
