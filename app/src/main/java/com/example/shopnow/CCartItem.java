package com.example.shopnow;

public class CCartItem {
    String cItName,cItPrice,cItTotal,CShopName,CShopPhone,cCustPhone,CShopLoc,cItemQty,cStatus,cTime;

    public CCartItem() {
    }

    public CCartItem(String cItName, String cItPrice,String cItTotal, String CShopName, String CShopPhone, String CShopLoc, String cItemQty,String cStatus,String cCustPhone,String cTime) {
        this.cItName = cItName;
        this.cItPrice = cItPrice;
        this.CShopName = CShopName;
        this.CShopPhone = CShopPhone;
        this.CShopLoc = CShopLoc;
        this.cItemQty = cItemQty;
        this.cStatus=cStatus;
        this.cItTotal=cItTotal;
        this.cCustPhone=cCustPhone;
        this.cTime=cTime;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getcCustPhone() {
        return cCustPhone;
    }

    public void setcCustPhone(String cCustPhone) {
        this.cCustPhone = cCustPhone;
    }

    public String getcItTotal() {
        return cItTotal;
    }

    public void setcItTotal(String cItTotal) {
        this.cItTotal = cItTotal;
    }

    public String getcStatus() {
        return cStatus;
    }

    public void setcStatus(String cStatus) {
        this.cStatus = cStatus;
    }

    public String getcItName() {
        return cItName;
    }

    public void setcItName(String cItName) {
        this.cItName = cItName;
    }

    public String getcItPrice() {
        return cItPrice;
    }

    public void setcItPrice(String cItPrice) {
        this.cItPrice = cItPrice;
    }

    public String getCShopName() {
        return CShopName;
    }

    public void setCShopName(String CShopName) {
        this.CShopName = CShopName;
    }

    public String getCShopPhone() {
        return CShopPhone;
    }

    public void setCShopPhone(String CShopPhone) {
        this.CShopPhone = CShopPhone;
    }

    public String getCShopLoc() {
        return CShopLoc;
    }

    public void setCShopLoc(String CShopLoc) {
        this.CShopLoc = CShopLoc;
    }

    public String getcItemQty() {
        return cItemQty;
    }

    public void setcItemQty(String cItemQty) {
        this.cItemQty = cItemQty;
    }
}
