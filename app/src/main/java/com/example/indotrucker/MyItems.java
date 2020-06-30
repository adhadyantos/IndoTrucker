package com.example.indotrucker;

public class MyItems {

    public String name;
    public String load;
    public String from;
    public String to;
    public String address;

    public MyItems(){

    }

    public MyItems(String name, String load, String from, String to, String address) {
        this.name = name;
        this.load = load;
        this.from = from;
        this.to = to;
        this.address = address;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from){
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to){
        this.to = to;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
