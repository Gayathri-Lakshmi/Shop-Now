package com.example.shopnow;

public class Customer {
    String Cname,Cphone,Cstreet,Cdist,Cstate,Cpin,Cid;

    public Customer() {
    }

    public Customer(String cName, String cPhone, String cStreet, String cDist, String cState, String cPin,String cId) {
        Cname=cName;

        Cphone=cPhone;
        Cstreet=cStreet;
        Cstate=cState;
        Cdist=cDist;
        Cpin=cPin;
        Cid=cId;
    }

    public String getCid() {
        return Cid;
    }

    public void setCid(String cid) {
        Cid = cid;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getCphone() {
        return Cphone;
    }

    public void setCphone(String cphone) {
        Cphone = cphone;
    }

    public String getCstreet() {
        return Cstreet;
    }

    public void setCstreet(String cstreet) {
        Cstreet = cstreet;
    }

    public String getCdist() {
        return Cdist;
    }

    public void setCdist(String cdist) {
        Cdist = cdist;
    }

    public String getCstate() {
        return Cstate;
    }

    public void setCstate(String cstate) {
        Cstate = cstate;
    }

    public String getCpin() {
        return Cpin;
    }

    public void setCpin(String cpin) {
        Cpin = cpin;
    }
}
