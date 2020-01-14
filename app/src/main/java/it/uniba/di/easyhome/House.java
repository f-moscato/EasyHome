package it.uniba.di.easyhome;

import java.util.ArrayList;

public class House {
    private String name;
    private User proprietario;
    private ArrayList<User> inquilini;
    private ArrayList<Bill>bollette;

    public House(){}

    public void addBill(String type, double tot, String descr,String expiration,boolean pay){
        bollette.add(new Bill(type,tot,descr,expiration,pay));
    }

    public ArrayList<User> getInquilini() {
        return inquilini;
    }

    public ArrayList<Bill> getBollette() {
        return bollette;
    }

    public void setBollette(ArrayList<Bill> bollette) {
        this.bollette = bollette;
    }

    public void setInquilini(ArrayList<User> inquilini) {
        this.inquilini = inquilini;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getProprietario() {
        return proprietario;
    }

    public void setProprietario(User proprietario) {
        this.proprietario = proprietario;
    }

    public House(String nome, User u){
        this.name=nome;
        this.proprietario=u;
        inquilini=new ArrayList<>();
        bollette=new ArrayList<>();
    }

}
