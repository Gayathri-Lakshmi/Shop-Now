package com.example.shopnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SBillAdapter extends RecyclerView.Adapter<SBillAdapter.MySBillViewHolder> {

    Context ct;
    ArrayList<CCartItem> Oitems;

    public SBillAdapter(sBill sBill, ArrayList<CCartItem> sBill1) {
        ct=sBill;
        Oitems=sBill1;
    }

    @NonNull
    @Override
    public MySBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( ct).inflate ( R.layout.sbilldesign,parent,false );
        SBillAdapter.MySBillViewHolder mySBillViewHolder = new SBillAdapter.MySBillViewHolder (view);
        return mySBillViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MySBillViewHolder holder, int position) {
        if(Oitems.size ()!=0){
            holder.name.setText ( Oitems.get ( position ).getcItName () );
            holder.price.setText ( Oitems.get ( position ).getcItPrice () );
            holder.qty.setText ( Oitems.get ( position ).getcItemQty () );
            holder.total.setText ( Oitems.get ( position ).getcItTotal () );
        }

    }

    @Override
    public int getItemCount() {
        return Oitems.size ();
    }

    public class MySBillViewHolder extends RecyclerView.ViewHolder {
        TextView name,qty,price,total;
        public MySBillViewHolder(@NonNull View itemView) {
            super ( itemView );
            name=itemView.findViewById ( R.id.sbIName );
            qty=itemView.findViewById ( R.id.sbIQty );
            price=itemView.findViewById ( R.id.sbIPrice );
            total=itemView.findViewById ( R.id.sbITotal );
        }
    }
}
