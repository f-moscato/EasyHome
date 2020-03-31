package it.uniba.di.easyhome;

public class Cleaning {
    String day;
    String turn_1;
    String turn_2;
    String descrizione;

    public Cleaning() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTurn_1() {
        return turn_1;
    }

    public void setTurn_1(String turn_1) {
        this.turn_1 = turn_1;
    }

    public String getTurn_2() {
        return turn_2;
    }

    public void setTurn_2(String turn_2) {
        this.turn_2 = turn_2;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Cleaning(String day, String turn_1, String turn_2, String descrizione) {
        this.day = day;
        this.turn_1 = turn_1;
        this.turn_2 = turn_2;
        this.descrizione = descrizione;
    }
}
