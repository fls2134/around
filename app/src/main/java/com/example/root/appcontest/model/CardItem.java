package com.example.root.appcontest.model;

/**
 * Created by sks on 2018. 9. 28..
 */
public class CardItem {
    /**
     * profile information variables
     */
    public int profileImage;
    public String nickName;

    /**
     * item information variables
     */
    public String imgurl;
    public String tilte;

    public int id;


    public CardItem(int profileImage, String nickName, String imgurl, String tilte, int id) {
        this.profileImage = profileImage;
        this.nickName = nickName;
        this.imgurl = imgurl;
        this.tilte = tilte;
        this.id = id;
    }


    public int getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }

    public int getId() { return id;};
}
