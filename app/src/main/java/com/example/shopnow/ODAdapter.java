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

public class ODAdapter extends RecyclerView.Adapter<ODAdapter.MyODViewHolder>{
    ArrayList<String > bill;
    String[] strings;
    Context ct;
    String phn;
    int skInd;
    public ODAdapter(OrderDone orderDone, ArrayList<String> skBill, String ph, int sInd) {
        ct=orderDone;
        bill=skBill;
        phn=ph;
        skInd=sInd;
    }


    @NonNull
    @Override
    public MyODViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from ( ct).inflate ( R.layout.orddesign,parent,false );
        ODAdapter.MyODViewHolder myODViewHolder = new ODAdapter.MyODViewHolder (view);
        return myODViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyODViewHolder holder, final int position) {
        strings=bill.get ( position ).split ( ";" );
        if(bill.size ()!=0){
            holder.name.setText ("Customer:"+"\n"+strings[1] );
            final String dt=strings[0];
            holder.date.setText ( dt.substring ( 6,8 )+"/"+dt.substring ( 4,6 )+"/"+dt.substring ( 0,4 )+"-"+dt.substring ( 9,11 )+":"+dt.substring ( 11,13 ));
            holder.total.setText ( strings[4] );

            holder.del.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {

                    DatabaseReference reference = FirebaseDatabase.getInstance ().getReference (  "Done"+phn );
                    reference.child (dt).removeValue ();
                    if(position==0){
                        holder.itemView.setVisibility(View.INVISIBLE);
                        Toast.makeText ( ct,"No items in the cart",Toast.LENGTH_SHORT ).show ();
                    }
                    else {
                        bill.remove ( position );
                        notifyItemRemoved ( position );
                        notifyItemRangeChanged ( position, bill.size () );
                    }


                }
            } );
            holder.itemView.setOnClickListener ( new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent ( ct, ORDBill.class );
                    i.putExtra ( "type","sk" );
                    i.putExtra ( "Ind",skInd );
                    i.putExtra ( "Ph",phn );
                    i.putExtra ( "pos",position );
                    ct.startActivity ( i );

                }
            } );
        }


    }


    @Override
    public int getItemCount() {
        return bill.size ();
    }

    public class MyODViewHolder extends RecyclerView.ViewHolder {
        TextView name,date,total;
        ImageButton del;
        public MyODViewHolder(@NonNull View itemView) {
            super ( itemView );
            name=itemView.findViewById ( R.id.name );
            date=itemView.findViewById ( R.id.date );
            total=itemView.findViewById ( R.id.total );
            del=itemView.findViewById ( R.id.del );
        }
    }
}
