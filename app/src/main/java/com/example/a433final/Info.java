package com.example.a433final;

import android.view.View;

public class Info {


    public String pName;

    public String price;



    public Info(String name, String information){
        pName=name;
        price=information;

    }



    public String getName(){
        return pName;
    }

    public String getInformation(){
        return price;
    }


}
