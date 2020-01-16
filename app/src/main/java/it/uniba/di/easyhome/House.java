package it.uniba.di.easyhome;

import java.util.ArrayList;
import java.util.HashMap;

public class House {
    private String name;
    private String owner;
    //private ArrayList<User> inquilini= new ArrayList<>();
   // private ArrayList<Bill> bollette=new ArrayList<>();

    public House(){}

    /*public void addBill(String type, double tot, String descr,String expiration,boolean pay){
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
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public House(String nome, String u){
        this.name=nome;
        this.owner=u;
    }

}
