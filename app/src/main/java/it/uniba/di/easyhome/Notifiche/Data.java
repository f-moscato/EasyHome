package it.uniba.di.easyhome.Notifiche;


import java.util.ArrayList;

public class Data {
    private String sender;
    private String title;
    private String body;
    private String dest;

    public Data() {
    }

    public Data(String sender, String title, String body, String dest) {
        this.sender = sender;
        this.title = title;
        this.body = body;
        this.dest = dest;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
