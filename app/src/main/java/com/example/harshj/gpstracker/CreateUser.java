package com.example.harshj.gpstracker;

/**
 * Created by harshjasani on 24/06/2018 AD.
 */

public class CreateUser {

    public String name;

    public CreateUser(){

    }
    public CreateUser(String name, String email, String password, String code, String issharing, String lng, String lat, String imageUri,String userid) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.issharing = issharing;
        this.lng = lng;
        this.lat = lat;
        this.imageUri = imageUri;
        this.userid = userid;
    }
    public String email;
    public String password;
    public String code;
    public String issharing;
    public String lng;
    public String lat;
    public String imageUri;
    public String userid;




}
