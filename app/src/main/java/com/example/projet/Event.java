package com.example.projet;

public class Event {
    private String id;
    private String title;
    private String date;

    public Event() { }

    public Event(String id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    // setters si besoin
}
