package it.uniba.di.easyhome;

public class User {


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


}
