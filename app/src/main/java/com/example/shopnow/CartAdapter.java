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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.shopnow.Global.scList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyCartViewHolder> {
    Context context;
    ArrayList<ArrayList<CCartItem>> cartList;
    int cInd,c;
    String stats;
    public CartAdapter(CustomerCart customerCart, ArrayList<ArrayList<CCartItem>> cCart, int CInd, int stat, int count) {
        context=customerCart;
        cartList=cCart;
        cInd=CInd;
        c=count;
        stats=String.valueOf ( stat );
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( context).inflate ( R.layout.cartdesign,parent,false );
        CartAdapter.MyCartViewHolder myCartViewHolder = new CartAdapter.MyCartViewHolder (view);
        return myCartViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyCartViewHolder holder, final int position) {
        if(c!=0) {
            if (cartList.get ( position ).get ( 0 ).getcStatus ().equals ( stats )) {
                if(stats.equals ( "3" )){
                    holder.del.setVisibility ( View.VISIBLE );
                }
                final String shopNm = cartList.get ( position ).get ( 0 ).CShopName;
                holder.cShop.setText ( shopNm );
                String dt=cartList.get ( position ).get ( 0 ).getcTime ();
                holder.date.setText ( dt.substring ( 6,8 )+"/"+dt.substring ( 4,6 )+"/"+dt.substring ( 0,4 )+"-"+dt.substring ( 9,11 )+":"+dt.substring ( 11,13 ));

                holder.itemView.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent ( context, Bill.class );
                        i.putExtra ( "iInd", position );
                        i.putExtra ( "cInd", cInd );
                        i.putExtra ( "Status", cartList.get ( position ).get ( 0 ).getcStatus () );
                        i.putExtra ( "Phone", cartList.get ( position ).get ( 0 ).CShopPhone );
                        i.putExtra ( "Loc", cartList.get ( position ).get ( 0 ).CShopLoc );
                        i.putExtra ( "Shop", shopNm );
                        context.startActivity ( i );

                    }
                } );
                holder.del.setOnClickListener ( new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        if(stats.equals ( "3" )){
                            DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ( "Cart" + scList.get ( cInd ).getCphone () );
                            reference.child ( cartList.get ( position ).get ( 0 ).getCShopName () ).removeValue ();
                            if(position==0){
                                holder.itemView.setVisibility(View.INVISIBLE);
                                Toast.makeText ( context,"No items in the cart",Toast.LENGTH_SHORT ).show ();
                            }
                            else {
                                cartList.remove ( position );
                                notifyItemRemoved ( position );
                                notifyItemRangeChanged ( position, cartList.size () );
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
        return c;
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {
        TextView cShop,date;
        ImageButton del;
        public MyCartViewHolder(@NonNull View itemView) {
            super ( itemView );
            cShop=itemView.findViewById ( R.id.cart_shopNameText );
            del=itemView.findViewById ( R.id.del );
            date=itemView.findViewById ( R.id.date );

        }
    }
}
