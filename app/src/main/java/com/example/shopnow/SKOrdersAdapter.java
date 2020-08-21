package com.example.shopnow;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.shopnow.Global.scList;

public class SKOrdersAdapter extends RecyclerView.Adapter<SKOrdersAdapter.MyOrdersViewHolder> {

    Context context;
    ArrayList<ArrayList<CCartItem>> arrayLists;
    int SInd,c;
    String stats,phn;


    public SKOrdersAdapter(SKOrders skOrders, ArrayList<ArrayList<CCartItem>> skCart, int sInd, int stat, int count, String ph) {
        context=skOrders;
        arrayLists=skCart;
        SInd=sInd;
        c=count;
        phn=ph;
        stats=String.valueOf ( stat );
    }


    @NonNull
    @Override
    public MyOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( context).inflate ( R.layout.cartdesign,parent,false );
        SKOrdersAdapter.MyOrdersViewHolder myOrdersViewHolder = new MyOrdersViewHolder ( view );
        return myOrdersViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyOrdersViewHolder holder, final int position) {
        if(c!=0) {
            if(arrayLists.get ( position ).get ( 0 ).getcStatus ().equals ( stats )) {
                final String shopNm = "Customer:" + arrayLists.get ( position ).get ( 0 ).cCustPhone;
                holder.skCust.setText ( shopNm );
                String dt=arrayLists.get ( position ).get ( 0 ).getcTime ();
                holder.date.setText ( dt.substring ( 6,8 )+"/"+dt.substring ( 4,6 )+"/"+dt.substring ( 0,4 )+"-"+dt.substring ( 9,11 )+":"+dt.substring ( 11,13 ));
                holder.itemView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent ( context, sBill.class );
                        i.putExtra ( "iInd", position );
                        i.putExtra ( "sInd", SInd );
                        i.putExtra ( "Status", arrayLists.get ( position ).get ( 0 ).getcStatus () );
                        i.putExtra ( "Phone", arrayLists.get ( position ).get ( 0 ).cCustPhone );
                        i.putExtra ( "Shop", shopNm );
                        i.putExtra ( "ph",phn );
                        context.startActivity ( i );

                    }
                } );
                holder.del.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        if(stats.equals ( "3" )){
                            DatabaseReference reference1 = FirebaseDatabase.getInstance ().getReference ( "Orders" + arrayLists.get ( position ).get ( 0 ).getCShopPhone () );
                            reference1.child ( arrayLists.get ( position ).get ( 0 ).getcCustPhone () ).removeValue ();
                            if(position==0){
                                holder.itemView.setVisibility(View.INVISIBLE);
                                Toast.makeText ( context,"No items in the cart",Toast.LENGTH_SHORT ).show ();
                            }
                            else {
                                arrayLists.remove ( position );
                                notifyItemRemoved ( position );
                                notifyItemRangeChanged ( position, arrayLists.size () );
                            }

                        }
                    }
                } );
            }
        }
        else {
            Toast.makeText ( context,"No items",Toast.LENGTH_SHORT ).show ();
        }

    }

    @Override
    public int getItemCount() {
        return arrayLists.size ();
    }

    public static class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        TextView skCust,date;
        ImageButton del;
        public MyOrdersViewHolder(@NonNull View itemView) {
            super ( itemView );
            skCust=itemView.findViewById ( R.id.cart_shopNameText );
            del=itemView.findViewById ( R.id.del );
            date=itemView.findViewById ( R.id.date );

        }
    }
}
