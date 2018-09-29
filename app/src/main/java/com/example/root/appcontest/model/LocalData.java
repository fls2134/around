package com.example.root.appcontest.model;

import java.io.Serializable;

public class LocalData implements Serializable{
    private static final long serialVersionUID = 4210431223148518967L;

    public int id;
    public String nickname;
    public String title;
    public double longtitude;
    public double latitude;
    public String img_url;
    public String tag;
    public String content;
    public int data_type;
    public int sYear, sMonth, sDay;
    public int eYear, eMonth, eDay;
    public boolean alarmed = false;
    public void LocalData(){

    }

    public void LocalData(int id,String nickname, String title, double longtitude, double latitude, String img_url, String tag, String content, int data_type, int sYear, int sMonth, int sDay, int eYear, int eMonth, int eDay){
        this.id = id;
        this.nickname = nickname;
        this.title = title;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.img_url = img_url;
        this.tag = tag;
        this.content = content;
        this.data_type = data_type;
        this.sDay = sDay;
        this.sMonth = sMonth;
        this.sYear = sYear;
        this.eDay = eDay;
        this.eMonth = eMonth;
        this.eYear = eYear;
    }

}
