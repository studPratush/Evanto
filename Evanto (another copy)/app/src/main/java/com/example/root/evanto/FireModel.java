package com.example.root.evanto;
public class FireModel {

    public String EventName;
    public String City;
    public String Organizer;
    public String StartDate;


    public String getName() {
        return EventName;
    }

    public void setName(String name) {
        this.EventName = name;
    }

    public String getEmail() {
        return City;
    }

    public void setEmail(String email) {
        this.City = email;
    }

    public String getAddress() {
        return Organizer;
    }

    public void setAddress(String address) {
        this.Organizer = address;
    }
    public String getDate() {
        return StartDate;
    }

    public void setDate(String date) {
        this.StartDate = date;
}
}