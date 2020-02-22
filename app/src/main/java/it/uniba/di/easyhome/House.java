package it.uniba.di.easyhome;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class House {
    private String name;
    private String owner;
    private HashMap<String,String> inquilini= new HashMap<>();
   private HashMap<String,Bill> bills=new HashMap<>();
   private String ssid;
   private String id;

    public House() {
    }

    public HashMap<String, Bill> getBills() {
        return bills;
    }

    public void setBills(HashMap<String, Bill> bills) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public House(String name, String owner, HashMap<String, String> inquilini, HashMap<String, Bill> bills, String ssid) {
        this.name = name;
        this.owner = owner;
        this.inquilini = inquilini;
        this.bills = bills;
        this.ssid = ssid;
    }
    public House(String name, String owner, HashMap<String, String> inquilini, HashMap<String, Bill> bills, String ssid,String id) {
        this.name = name;
        this.owner = owner;
        this.inquilini = inquilini;
        this.bills = bills;
        this.ssid = ssid;
        this.id=id;
    }
    public String getSsid() {
        return ssid;
    }

    public House(String name,String owner,String id){
        this.owner=owner;
        this.name=name;
        this.id=id;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Bill> ordinamentoTemporaleBollette(){
        ArrayList<Bill> ret=new ArrayList<>();
        DateFormat format = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);

        ret.addAll(this.bills.values());
        Collections.sort(ret,new Comparator<Bill>() {
            @Override
            public int compare(Bill o1, Bill o2) {
                Date data1 = null;
                Date data2= null;
                try {
                    data1=format.parse(o1.getExpiration());
                    Log.v(TAG,o1.getExpiration()+" "+o2.getExpiration());
                    data2=format.parse(o2.getExpiration());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert data1 != null;
                if(data1.compareTo(data2)<=0){
                    Log.v(TAG, String.valueOf(data1.compareTo(data2)));
                    return -1;
                }else{
                    return 1;
                }
            }
        });

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Bill> ordinamentoTemporaleBolletteStorico(){
        DateFormat format = new SimpleDateFormat("dd/mm/yyyy", Locale.ENGLISH);

        ArrayList<Bill> ret = new ArrayList<>(this.bills.values());
        Collections.sort(ret,new Comparator<Bill>() {
            @Override
            public int compare(Bill o1, Bill o2) {
                Date data1 = null;
                Date data2= null;
                try {
                    data1=format.parse(o1.getExpiration());
                    data2=format.parse(o2.getExpiration());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                assert data1 != null;
                if(data1.compareTo(data2)<=0){
                    Log.v(TAG, String.valueOf(data1.compareTo(data2)));
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        return ret;
    }
}
