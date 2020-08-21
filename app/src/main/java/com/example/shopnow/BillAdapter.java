package com.example.shopnow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.shopnow.Global.scList;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyBillViewHolder>  {

    Context ct;
    int CInd,IInd;
    ArrayList<CCartItem> Citems;
    String shopName,sPhn,sLoc,sStatus;

    public BillAdapter(Bill bill, ArrayList<CCartItem> sBill, int cInd, String shop, int iInd, String shopPhn, String shopLoc, String status) {
        Citems=sBill;
        ct=bill;
        CInd =cInd;
        IInd=iInd;
        sPhn=shopPhn;
        sLoc=shopLoc;
        sStatus=status;
        shopName=shop;
    }

    @NonNull
    @Override
    public MyBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( ct).inflate ( R.layout.billdesign,parent,false );
        BillAdapter.MyBillViewHolder myBillViewHolder = new BillAdapter.MyBillViewHolder (view);
        return myBillViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyBillViewHolder holder, final int position) {
        if(Citems.size ()!=0) {
            if (!Citems.get ( 0 ).getcStatus ().equals ( "0" )) {
                holder.del.setVisibility ( View.INVISIBLE );
            }

            final String iName = Citems.get ( position ).getcItName ();
            final String iPrice = Citems.get ( position ).getcItPrice ();
            final String iQty = Citems.get ( position ).getcItemQty ();
            final String iTotal = Citems.get ( position ).getcItTotal ();

            if (Citems.size () != 0) {
                holder.name.setText ( iName );
                holder.price.setText ( iPrice );
                holder.qty.setText ( iQty );
                holder.total.setText ( iTotal );
            }
            if (Citems.get ( 0 ).getcStatus ().equals ( "0" )) {
                holder.del.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ( "Cart" + scList.get ( CInd ).getCphone () );
                        reference.child ( shopName ).child ( Citems.get ( position ).getcItName () ).removeValue ();
                        if(Citems.size ()==1){
                            holder.itemView.setVisibility(View.INVISIBLE);
                            Toast.makeText ( ct,"No items in the cart",Toast.LENGTH_SHORT ).show ();
                        }
                        else {
                            Citems.remove ( position );
                            notifyItemRemoved ( position );
                            notifyItemRangeChanged ( position, Citems.size () );
                        }
                        DatabaseReference reference1 = FirebaseDatabase.getInstance ().getReference ( "Orders" + Citems.get ( position ).getCShopPhone () );
                        reference1.child ( Citems.get ( position ).getcCustPhone () ).child ( Citems.get ( position ).getcItName () ).removeValue ();

                    }
                } );
            }
        }
        else {
            Toast.makeText ( ct,"No items in the cart",Toast.LENGTH_SHORT ).show ();
        }
    }

    @Override
    public int getItemCount() {
        return Citems.size ();
    }

    public class MyBillViewHolder extends RecyclerView.ViewHolder {
        TextView name,qty,price,total;
        ImageButton del;
        public MyBillViewHolder(@NonNull View itemView) {
            super ( itemView );
            name=itemView.findViewById ( R.id.bIName );
            qty=itemView.findViewById ( R.id.bIQty );
            price=itemView.findViewById ( R.id.bIPrice );
            total=itemView.findViewById ( R.id.bITotal );
            del=itemView.findViewById ( R.id.del );


        }
    }
}
