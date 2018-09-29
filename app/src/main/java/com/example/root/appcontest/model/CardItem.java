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
    public int posterImage;
    public String tilte;



    public CardItem(int profileImage, String nickName, int posterImage, String tilte) {
        this.profileImage = profileImage;
        this.nickName = nickName;
        this.posterImage = posterImage;
        this.tilte = tilte;
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

    public int getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(int posterImage) {
        this.posterImage = posterImage;
    }

    public String getTilte() {
        return tilte;
    }

    public void setTilte(String tilte) {
        this.tilte = tilte;
    }
}
