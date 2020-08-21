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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerItemsAdapter extends RecyclerView.Adapter<CustomerItemsAdapter.MyItemsViewHolder> {
    Context itemsCt;
    ArrayList<Items> arrayList;
    int ind,CInd,c,there,order;
    String shp;
    ArrayList<String> shops=new ArrayList<> (  );
    ArrayList<ArrayList<CCartItem>> cart,orders;
    public CustomerItemsAdapter(CustomerItems customerItems, ArrayList<Items> itemsArrayList, int index, int cInd, ArrayList<ArrayList<CCartItem>> cCart, String name, ArrayList<ArrayList<CCartItem>> ordered) {
        itemsCt=customerItems;
        arrayList=itemsArrayList;
        ind=index;
        CInd=cInd;
        cart=cCart;
        shp=name;
        orders=ordered;
    }


    @NonNull
    @Override
    public MyItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( itemsCt).inflate ( R.layout.itemsdesign,parent,false );
        CustomerItemsAdapter.MyItemsViewHolder myItemsViewHolder = new CustomerItemsAdapter.MyItemsViewHolder (view);
        return myItemsViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyItemsViewHolder holder, final int position) {
        if(arrayList.size ()!=0){
            holder.item_Cname.setText ( arrayList.get ( position ).getiName () );
            holder.item_Cprice.setText ( arrayList.get ( position ).getiPrice () );
            Glide.with(itemsCt).load(arrayList.get ( position ).getiUri ()).into(holder.item_img);
            holder.item_button.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    c=-1;
                    there=0;
                    order=0;
                    if(cart.size ()!=0) {
                        for (int j = 0; j < cart.size ();j++){
                            shops.add ( cart.get ( j ).get ( 0 ).getCShopName () );
                        }
                        if(shops.size ()!=0) {
                            for (int j = 0; j < shops.size (); j++) {
                                if (shops.get ( j ).equals ( shp )) {
                                    c = j;
                                }
                            }
                        }
                        if(c!=-1) {
                            for(int j=0;j<cart.get ( c ).size ();j++){
                                if(cart.get ( c ).get ( j ).getcItName ().equals ( arrayList.get ( position ).getiName () )){
                                    there=1;
                                }
                            }
                        }
                    }
                    if(orders.size ()!=0){
                        for (int j = 0; j < orders.size ();j++){
                            shops.add ( orders.get ( j ).get ( 0 ).getCShopName () );
                        }
                        if(orders.size ()!=0) {
                            for (int j = 0; j < orders.size (); j++) {
                                if (shops.get ( j ).equals ( shp )) {
                                    order=1;
                                }
                            }
                        }
                    }

                    if(there==0 && order==0){

                        Intent i = new Intent ( itemsCt, AddToCart.class );
                        i.putExtra ( "iName", arrayList.get ( position ).getiName () );
                        i.putExtra ( "iPrice", arrayList.get ( position ).getiPrice () );
                        i.putExtra ( "index", ind );
                        i.putExtra ( "Cindex", CInd );
                        itemsCt.startActivity ( i );
                    }
                    else if(there!=0){
                        Toast.makeText ( itemsCt,"Item already added in the cart",Toast.LENGTH_SHORT ).show ();
                    }
                    else {
                        Toast.makeText ( itemsCt,"Already order is confirmed in this shop and not received yet. Sorry! Order cannot be changed.",Toast.LENGTH_SHORT ).show ();
                    }


                }
            } );


        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size ();
    }

    public class MyItemsViewHolder extends RecyclerView.ViewHolder {
        TextView item_Cname,item_Cprice;
        ImageView item_img;
        ImageButton item_button;

        public MyItemsViewHolder(@NonNull View itemView) {

            super ( itemView );
            item_Cname=itemView.findViewById ( R.id.cItem );
            item_Cprice=itemView.findViewById ( R.id.cPrice );
            item_img=itemView.findViewById ( R.id.CItemImage );
            item_button=itemView.findViewById ( R.id.cItemAdd );
            item_button.setVisibility ( View.VISIBLE );
        }
    }
}
