package model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String username;
    private String password;
    private String role;
    private int balance = 116000000;
    private List<Basket> basketList;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public List<Basket> getBasketList() {
        return basketList;
    }

    public void setBasketList(List<Basket> basketList) {
        this.basketList = basketList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int addBalance(int inputMoney) {
        this.balance = balance + inputMoney;
        return this.balance;
    }

    @Override
    public String toString() {
        return "----------------------------------------" +
                "\nThông tin tài khoản :" +
                "\nUsername = " + username +
                "\nPassword = " + password +
                "\nRole = " + role +
                "\nBalance = " + balance +
                "\nBasketList = " + basketList
                ;
    }
}