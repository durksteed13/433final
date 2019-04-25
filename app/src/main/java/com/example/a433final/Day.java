package com.example.a433final;


import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable {

    //Parcelable
//Serializable
    public String date;

    public Double pPrice;



    public Day(String DAY_OF_MONTH, Double price){
        date=DAY_OF_MONTH;
        pPrice=price;

    }



    public String getDate(){
        return date;
    }

    public Double getpPrice(){
        return pPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
