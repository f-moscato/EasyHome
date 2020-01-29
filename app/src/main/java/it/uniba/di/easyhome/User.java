package it.uniba.di.easyhome;

import java.io.Serializable;

public class User implements Serializable {


    private String email;


    private String pass;
    private String role;
    private String name;
    private String surname;
    private String mac;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String email,String pass, String name, String surname, String role, String mac) {
        this.pass=pass;
        this.email = email;
        this.role=role;
        this.name=name;
        this.surname=surname;
        this.mac=mac;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }


    public String getPass() {
        return pass;
    }
}
