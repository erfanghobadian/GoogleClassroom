package com.example.googleclassroom ;

import java.io.Serializable;


class User implements Serializable {
    String username ;
    String password ;
    byte[] avatar;
    User(String username , String password , byte[] avatar) {
        this.username = username;
        this.password = password;
        this.avatar =  avatar ;
    }

}