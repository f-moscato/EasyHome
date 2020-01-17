package it.uniba.di.easyhome;

public class Bill {
    public Bill() {

    }

    enum Type{ENERGY,WATER,GAS,OTHER}
    String type;
    String total;
    String description;
    String expiration;
    String payed;

    public Bill(String type, String tot, String descr, String expiration, String payed){
        /*switch(type.toUpperCase()){
            case "ENERGY":
                type=Type.ENERGY;
                break;
            case "WATER" :
                type=Type.WATER;
                break;
            case "GAS" :
                type=Type.GAS;
                break;
            case "OTHER" :
                type=Type.OTHER;
                break;
        }*/
        this.type=type;
        this.total=tot;
        this.description=descr;
        this.expiration=expiration;
        this.payed=payed;
    }

    public String isPayed() {
        return payed;
    }

    public void setPayed(String payed) {
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

    public String getTotale() {
        return total;
    }

    public void setTotale(String totale) {
        this.total= totale;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){

        return this.getType()+" - "+this.getTotale()+" - "+this.getDescrizione()+" - "+this.getExpiration()+" - "+this.isPayed();
    }

}
