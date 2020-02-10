package it.uniba.di.easyhome;

import java.io.Serializable;

public class User implements Serializable {


    private String email;
    private String pass;
    private String role;
    private String name;
    private String surname;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User( String email,String pass, String name, String surname, String role) {
        this.pass=pass;
        this.email = email;
        this.role=role;
        this.name=name;
        this.surname=surname;

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
