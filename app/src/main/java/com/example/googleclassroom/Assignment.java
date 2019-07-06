package com.example.googleclassroom;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;

public class Assignment implements Serializable {
    private static final long serialVersionUID = 7829136421241571165L;
    String title ;
    String des ;
    int points ;
    byte[] attach ;
    Calendar due ;
    Topic topic;
    HashMap<String,HomeWork> works = new HashMap<>();
}
