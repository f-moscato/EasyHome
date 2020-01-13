package it.uniba.di.easyhome;

import java.util.ArrayList;

public class House {
    private String name;
    private User proprietario;
    private ArrayList<User> inquilini;
    private ArrayList<Bill>bollette;

    public House(String nome, User u){
        this.name=nome;
        this.proprietario=u;
        inquilini=new ArrayList<>();
        bollette=new ArrayList<>();
    }

    private static class Bill{
       enum Type{ENERGY,WATER,GAS,OTHER}
       Type tipo;
       double totale;
       String descrizione;

       public Bill(String type, double tot, String descr){
           switch(type.toUpperCase()){
               case "ENERGY":
                   tipo=Type.ENERGY;
                   break;
               case "WATER" :
                   tipo=Type.WATER;
                   break;
               case "GAS" :
                   tipo=Type.GAS;
                   break;
               case "OTHER" :
                   tipo=Type.OTHER;
                   break;
           }
           this.totale=tot;
           this.descrizione=descr;
       }

        public String getDescrizione() {
            return descrizione;
        }

        public void setDescrizione(String descrizione) {
            this.descrizione = descrizione;
        }

        public double getTotale() {
            return totale;
        }

        public void setTotale(double totale) {
            this.totale = totale;
        }

        public Type getTipo() {
            return tipo;
        }

        public void setTipo(Type tipo) {
            this.tipo = tipo;
        }
    }
}
