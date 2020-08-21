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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerCart extends Global {

    RecyclerView rv_cart;
    int CInd,stat;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;

    ArrayList<ArrayList<CCartItem>> cCart;
    ArrayList<Shopkeeper> shopList;

    ProgressBar progressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_customer_cart );
        rv_cart=findViewById ( R.id.cart_recycler );
        cCart=new ArrayList<> (  );
        shopList=new ArrayList<> (  );
        progressBar=findViewById ( R.id.cartProgress );

        CInd=getIntent ().getIntExtra ( "CInd",0 );
        stat=getIntent ().getIntExtra ( "Stat",0 );

        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();

        if(ssList.size ()!=0){
            for(int i=0;i<ssList.size ();i++){
                if(scList.get ( CInd ).getCpin ().equals ( ssList.get ( i ).getSKpin () )){
                    shopList.add ( ssList.get ( i ) );
                }
            }
        }

        cCart.clear ();

        if(shopList.size ()!=0) {
            for (int i = 0; i < shopList.size (); i++) {
                final ArrayList<CCartItem> cCartItems = new ArrayList<> ();
                final int finalI = i;
                sReference.child ( "Cart" + scList.get ( CInd ).getCphone () ).child ( shopList.get ( i ).getSKshop () ).addValueEventListener ( new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists ()) {
                            for (DataSnapshot data : snapshot.getChildren ()) {
                                cCartItems.add ( data.getValue ( CCartItem.class ) );

                            }
                            cCart.add ( cCartItems );
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
                progressBar.setVisibility ( View.INVISIBLE );
                int count=0;
                if(cCart.size ()!=0){

                    for (int i=0;i<cCart.size ();i++){
                        if(cCart.get ( i ).get ( 0 ).getcStatus ().equals ( String.valueOf (stat) )){
                            count=count+1;
                        }
                    }
                    if(count!=0) {
                        rv_cart.setLayoutManager ( new LinearLayoutManager ( CustomerCart.this ) );
                        CartAdapter cartAdapter = new CartAdapter ( CustomerCart.this, cCart, CInd, stat, count );
                        rv_cart.setAdapter ( cartAdapter );
                    }else {
                        Toast.makeText ( CustomerCart.this,"No items in the cart",Toast.LENGTH_SHORT ).show ();
                    }

                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 5000);

    }
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.
        return;
    }

    public void back(View view) {
        Intent in =new Intent ( CustomerCart.this,CustomerHome.class );
        in.putExtra ( "Phone",scList.get ( CInd ).getCphone () );
        startActivity ( in );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_pickups:
                Intent in =new Intent ( CustomerCart.this,CustomerCart.class );
                in.putExtra ( "CInd",CInd );
                in.putExtra ( "Stat",2 );
                startActivity ( in );
                return true;
            case R.id.option_ordered:
                Intent i =new Intent ( CustomerCart.this,CustomerCart.class );
                i.putExtra ( "CInd",CInd );
                i.putExtra ( "Stat", 1);
                startActivity ( i );
                return true;

            case R.id.option_inCart:
                Intent it =new Intent ( CustomerCart.this,CustomerCart.class );
                it.putExtra ( "CInd",CInd );
                it.putExtra ( "Stat", 0);
                startActivity ( it );

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}