package com.example.shopnow;

import java.util.ArrayList;

public class IList {
    public IList() {
    }

    ArrayList<Items> itemsArrayList;

    public IList(ArrayList<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }

    public ArrayList<Items> getItemsArrayList() {
        return itemsArrayList;
    }

    public void setItemsArrayList(ArrayList<Items> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }
}
