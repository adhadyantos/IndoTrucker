package com.example.indotrucker;

public class Requests {

    public String teks;
    public String name;
    public String image;
    public String thumb_image;

    public Requests(){

    }

    public Requests(String teks, String name, String image) {
        this.teks = teks;
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

    public String getTeks() {
        return teks;
    }

    public void setTeks(String teks) {
        this.teks = teks;

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
