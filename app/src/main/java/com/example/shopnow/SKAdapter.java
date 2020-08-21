package com.example.shopnow;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SKAdapter extends RecyclerView.Adapter<SKAdapter.MySKViewHolder> {
    Context skContext;
    ArrayList<Items> iArrayList;
    String pNo;

    public SKAdapter(ShopkeeperHome shopkeeperHome, ArrayList<Items> itemsArrayList, String sPh) {
        skContext = shopkeeperHome;
        iArrayList=itemsArrayList;
        pNo=sPh;
    }

    @NonNull
    @Override
    public MySKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( skContext ).inflate ( R.layout.skitemsdesign, parent, false );
        return new MySKViewHolder ( view );
    }


    @Override
    public void onBindViewHolder(@NonNull MySKViewHolder holder, final int position) {
        if(iArrayList.size ()!=0){
            holder.item_SKname.setText ( iArrayList.get ( position ).getiName () );
            holder.item_SKprice.setText ( iArrayList.get ( position ).getiPrice () );
            Glide.with(skContext).load(iArrayList.get ( position ).getiUri ()).into(holder.item_skimg);
            holder.item_skdel.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ("Items"+pNo);
                    reference.child ( iArrayList.get ( position ).getiName () ).removeValue ();
                    iArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, iArrayList.size());



                }
            } );


        }

    }


    @Override
    public int getItemCount() {

        return iArrayList.size ();
    }

    public static class MySKViewHolder extends RecyclerView.ViewHolder {

        TextView item_SKname, item_SKprice;
        ImageView item_skimg;
        ImageButton item_skdel;

        public MySKViewHolder(@NonNull View itemView) {
            super ( itemView );
            item_SKname = itemView.findViewById ( R.id.skItem );
            item_SKprice = itemView.findViewById ( R.id.skPrice );
            item_skimg = itemView.findViewById ( R.id.SKItemImage );
            item_skdel=itemView.findViewById ( R.id.skItemDelete );





        }

    }


}
