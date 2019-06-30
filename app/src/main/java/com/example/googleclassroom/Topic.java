package com.example.googleclassroom;

import java.io.Serializable;
import java.util.ArrayList;

public class Topic implements  Serializable {
        String name ;
        ArrayList<Assignment> assignments = new ArrayList<>();
}
