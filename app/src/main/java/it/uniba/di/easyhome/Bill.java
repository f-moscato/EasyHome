package it.uniba.di.easyhome;

public class Bill {
    enum Type{ENERGY,WATER,GAS,OTHER}
    Type tipo;
    double total;
    String description;
    String expiration;
    boolean payed;

    public Bill(String type, double tot, String descr,String expiration,boolean payed){
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
        this.total=tot;
        this.description=descr;
        this.expiration=expiration;
        this.payed=payed;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getDescrizione() {
        return description;
    }

    public void setDescrizione(String descrizione) {
        this.description = descrizione;
    }

    public double getTotale() {
        return total;
    }

    public void setTotale(double totale) {
        this.total= totale;
    }

    public String getTipo() {
        return tipo.toString();
    }

    public void setTipo(Type tipo) {
        this.tipo = tipo;
    }
}
