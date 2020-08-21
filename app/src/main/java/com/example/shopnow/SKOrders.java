package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SKOrders extends Global {

    int sInd,stat;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;

    ArrayList<ArrayList<CCartItem>> skCart,sCart;
    ArrayList<Customer> cList;
    ProgressBar progressBar;
    RecyclerView rv;
    int count;
    TextView tv;
    String ph;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_s_k_orders );
        sInd = getIntent ().getIntExtra ( "Index",0 );
        stat=getIntent ().getIntExtra ( "Stat",1 );
        ph=getIntent ().getStringExtra ( "Ph" );
        skCart=new ArrayList<> (  );
        sCart=new ArrayList<> (  );
        cList=new ArrayList<> (  );
        progressBar=findViewById ( R.id.skProgress );
        rv=findViewById ( R.id.skOrders_recycler );
        tv=findViewById ( R.id.textView );
        if(stat==1){
            tv.setText ( "Orders in your shop" );
        }
        else if(stat==2){
            tv.setText ( "Orders packed" );
        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();

        for(int i=0;i<scList.size ();i++){
            if(ssList.get ( sInd ).getSKpin ().equals ( scList.get ( i ).getCpin () )){
                cList.add ( scList.get ( i ) );
            }
        }
        skCart.clear ();

        if(cList.size ()!=0) {
            for (int i = 0; i < cList.size (); i++) {
                final ArrayList<CCartItem> cCartItems = new ArrayList<> ();
                final int finalI = i;
                sReference.child ( "Orders" + ssList.get ( sInd ).getSKphone () ).child ( cList.get ( i ).getCphone () ).addValueEventListener ( new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists ()) {
                            for (DataSnapshot data : snapshot.getChildren ()) {
                                cCartItems.add ( data.getValue ( CCartItem.class ) );

                            }
                            if(!cCartItems.get ( 0 ).getcStatus ().equals ( "0" )){
                                skCart.add ( cCartItems );
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
            }
        }

        progressBar.setVisibility ( View.VISIBLE );
        Runnable r = new Runnable() {
            @Override
            public void run() {
                count=0;
                if(skCart.size ()!=0){
                    for (int i=0;i<skCart.size ();i++){
                        if(skCart.get ( i ).get ( 0 ).getcStatus ().equals ( String.valueOf ( stat ) )){
                            sCart.add ( skCart.get ( i ) );
                            count=count+1;
                        }
                    }
                }
                progressBar.setVisibility ( View.INVISIBLE );
                if(count==0){
                    Toast.makeText ( SKOrders.this,"Sorry! No orders",Toast.LENGTH_SHORT ).show ();
                }
                else {
                    rv.setLayoutManager ( new LinearLayoutManager (SKOrders.this) );
                    SKOrdersAdapter skOrdersAdapter=new SKOrdersAdapter (SKOrders.this,sCart,sInd,stat,count,ph);
                    rv.setAdapter ( skOrdersAdapter );

                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 5000);

    }

    public void backHome(View view) {
        Intent i = new Intent ( SKOrders.this, ShopkeeperHome.class );
        i.putExtra ( "Phone", ph );
        startActivity ( i );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sbill_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_orders:
                Intent i = new Intent ( SKOrders.this, SKOrders.class );
                i.putExtra ( "Ph", ph );
                i.putExtra ( "Stat", 1 );
                i.putExtra ( "Index", sInd );
                startActivity ( i );
                return true;
            case R.id.option_packed:
                Intent in = new Intent ( SKOrders.this, SKOrders.class );
                in.putExtra ( "Ph", ph );
                in.putExtra ( "Stat", 2 );
                in.putExtra ( "Index", sInd );
                startActivity ( in );
                return true;
            case R.id.option_done:
                Intent intent = new Intent ( SKOrders.this, OrderDone.class );
                intent.putExtra ( "Ph", ph );
                intent.putExtra ( "Index", sInd );
                startActivity ( intent );
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}