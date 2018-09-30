package com.example.root.appcontest.model;

import java.io.Serializable;

public class UserData implements Serializable {
    String email;
    String password;
    String nick;
    String favorite;
    public UserData(){}
    public UserData(String email, String password, String nick){
        this.email = email;
        this.password = password;
        this.nick = nick;
    }
}
