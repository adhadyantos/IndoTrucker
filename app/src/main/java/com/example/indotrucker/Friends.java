package com.example.indotrucker;

public class Friends {

    public String date;
    public String name;
    public String image;
    public String thumb_image;

    public Friends(){

    }

    public Friends(String date, String name, String image) {
        this.date = date;
        this.name = name;
        this.image = image;
        this.thumb_image = thumb_image;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image){
        this.thumb_image = thumb_image;
    }
}
