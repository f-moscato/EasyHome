package it.uniba.di.easyhome;

import java.util.HashMap;

public class House {
    private String name;
    private String owner;
    private HashMap<String,String> inquilini= new HashMap<>();
   private HashMap<String,HashMap<String,String>> bills=new HashMap<>();
   private String ssid;

    public House() {
    }

    public HashMap<String, HashMap<String, String>> getBills() {
        return bills;
    }

    public void setBills(HashMap<String, HashMap<String, String>> bills) {
        this.bills = bills;
    }

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

    public HashMap<String, String> getInquilini() {
        return inquilini;
    }

    public void setInquilini(HashMap<String, String> inquilini) {
        this.inquilini = inquilini;
    }

    public House(HashMap<String,String> inq){
        this.setInquilini(inq);
    }

    public House(String name, String owner, HashMap<String, String> inquilini, HashMap<String, HashMap<String, String>> bills, String ssid) {
        this.name = name;
        this.owner = owner;
        this.inquilini = inquilini;
        this.bills = bills;
        this.ssid = ssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
}
