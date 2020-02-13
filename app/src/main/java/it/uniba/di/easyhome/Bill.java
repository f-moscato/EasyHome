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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total= total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){

        return this.getType()+" - "+this.getTotal()+" - "+this.getDescription()+" - "+this.getExpiration()+" - "+this.isPayed();
    }

    public int compareTo(Bill b){
        return this.getExpiration().compareTo(b.getExpiration());
    }

}
