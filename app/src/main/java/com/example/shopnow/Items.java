package com.example.shopnow;

import android.net.Uri;

public class Items {
    String iName,iPrice,iUri;

    public Items() {
    }

    public Items(String IName, String IPrice, String IUri) {
        iName = IName;
        iPrice = IPrice;
        iUri=IUri;

    }

    public String getiUri() {
        return iUri;
    }

    public void setiUri(String iUri) {
        this.iUri = iUri;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }

    public String getiPrice() {
        return iPrice;
    }

    public void setiPrice(String iPrice) {
        this.iPrice = iPrice;
    }

}
