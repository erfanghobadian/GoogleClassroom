package com.example.googleclassroom ;

import java.io.Serializable;
import java.util.ArrayList;


class User implements Serializable {
    private static final long serialVersionUID = 7829136421241571165L;
    String username ;
    String password ;
    byte[] avatar;
    ArrayList <Class> classes = new ArrayList<>() ;
    User(String username , String password , byte[] avatar) {
        this.username = username;
        this.password = password;
        this.avatar =  avatar ;
    }

}
