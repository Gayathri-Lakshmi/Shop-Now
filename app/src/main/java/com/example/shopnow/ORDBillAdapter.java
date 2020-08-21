package com.example.shopnow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ORDBillAdapter extends RecyclerView.Adapter<ORDBillAdapter.MyORDBillViewHolder>{
    Context ct;
    String[] Bill;
    int count;
    public ORDBillAdapter(ORDBill ordBill, String[] bill, int c) {
        ct=ordBill;
        Bill=bill;
        count=c;
    }


    @NonNull
    @Override
    public MyORDBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( ct).inflate ( R.layout.billdesign,parent,false );
        ORDBillAdapter.MyORDBillViewHolder myORDBillViewHolder = new ORDBillAdapter.MyORDBillViewHolder (view);
        return myORDBillViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyORDBillViewHolder holder, int position) {

        holder.del.setVisibility ( View.INVISIBLE );
        holder.name.setText ( Bill[(4*(position+1))+1] );
        holder.price.setText (  Bill[(4*(position+1))+2]  );
        holder.qty.setText ( Bill[(4*(position+1))+3]  );
        holder.total.setText ( Bill[(4*(position+1))+4]  );

    }


    @Override
    public int getItemCount() {
        return count;
    }

    public class MyORDBillViewHolder extends RecyclerView.ViewHolder {
        TextView name,qty,price,total;
        ImageButton del;
        public MyORDBillViewHolder(@NonNull View itemView) {
            super ( itemView );
            name=itemView.findViewById ( R.id.bIName );
            qty=itemView.findViewById ( R.id.bIQty );
            price=itemView.findViewById ( R.id.bIPrice );
            total=itemView.findViewById ( R.id.bITotal );
            del=itemView.findViewById ( R.id.del );
        }
    }
}
