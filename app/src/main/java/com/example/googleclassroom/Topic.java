package com.example.googleclassroom;

import java.io.Serializable;
import java.util.ArrayList;

public class Topic implements  Serializable {
        private static final long serialVersionUID = 7829136421241571165L;
        String name ;
        ArrayList<Assignment> assignments = new ArrayList<>();
        Topic(String name) {
                this.name = name ;
        }
}
