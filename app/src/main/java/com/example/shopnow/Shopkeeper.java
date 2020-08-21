package com.example.shopnow;

import android.os.Parcelable;

public class Shopkeeper {
    String SKname,SKshop,SKphone,SKstreet,SKdist,SKstate,SKpin,SKcat,SKid;

    public Shopkeeper() {
    }

    public Shopkeeper(String name, String shop, String phone, String add, String dist, String state, String zip, String category,String skId) {
        SKname=name;
        SKphone=phone;
        SKshop=shop;
        SKstreet=add;
        SKdist=dist;
        SKstate=state;
        SKpin=zip;
        SKcat=category;
        SKid=skId;
    }

    public String getSKid() {
        return SKid;
    }

    public void setSKid(String SKid) {
        this.SKid = SKid;
    }

    public String getSKname() {
        return SKname;
    }

    public void setSKname(String SKname) {
        this.SKname = SKname;
    }


    public String getSKshop() {
        return SKshop;
    }

    public void setSKshop(String SKshop) {
        this.SKshop = SKshop;
    }

    public String getSKphone() {
        return SKphone;
    }

    public void setSKphone(String SKphone) {
        this.SKphone = SKphone;
    }

    public String getSKstreet() {
        return SKstreet;
    }

    public void setSKstreet(String SKstreet) {
        this.SKstreet = SKstreet;
    }

    public String getSKdist() {
        return SKdist;
    }

    public void setSKdist(String SKdist) {
        this.SKdist = SKdist;
    }

    public String getSKstate() {
        return SKstate;
    }

    public void setSKstate(String SKstate) {
        this.SKstate = SKstate;
    }

    public String getSKpin() {
        return SKpin;
    }

    public void setSKpin(String SKpin) {
        this.SKpin = SKpin;
    }

    public String getSKcat() {
        return SKcat;
    }

    public void setSKcat(String SKcat) {
        this.SKcat = SKcat;
    }
}
