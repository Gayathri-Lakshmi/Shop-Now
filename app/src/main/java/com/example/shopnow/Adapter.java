package com.example.shopnow;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {



    Context ct;
    int cInd;
    ArrayList<Shopkeeper> arrayList=new ArrayList<Shopkeeper>();;

    public Adapter(CustomerHome customerHome, ArrayList<Shopkeeper> skcList, int c) {
        ct=customerHome;
        arrayList=skcList;
        cInd=c;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( ct).inflate ( R.layout.design,parent,false );

        return new MyViewHolder ( view );
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if(arrayList.size ()!=0){
            String[] parts = arrayList.get ( position ).getSKcat ().split("\t");
            String Cat;
            if(parts.length==1){
                Cat=parts[0];
            }
            else if (parts.length==2){
                Cat=parts[0]+","+parts[1];
            }
            else if (parts.length==3){
                Cat=parts[0]+","+parts[1]+","+parts[2];
            }
            else {
                Cat=parts[0]+","+parts[1]+","+parts[2]+","+parts[3];
            }


            holder.cat.setText ( Cat );
            holder.shop.setText ( arrayList.get ( position ).getSKshop () );

            holder.itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    Intent i =new Intent ( ct,CustomerItems.class );
                    i.putExtra ( "Name",arrayList.get ( position ).getSKshop () );
                    i.putExtra ( "CIndex",cInd );
                    ct.startActivity ( i );

                }
            } );






        }



    }


    @Override
    public int getItemCount() {
        return arrayList.size ();

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView shop,cat;
        ImageButton ib;
        public MyViewHolder(@NonNull View itemView) {
            super ( itemView );
            shop=itemView.findViewById ( R.id.shopNameText );
            cat=itemView.findViewById ( R.id.cat );
            ib=itemView.findViewById ( R.id.image );

        }
    }
}
