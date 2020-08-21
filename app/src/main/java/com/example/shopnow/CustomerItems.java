package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;

public class CustomerItems extends Global {

    RecyclerView items_rv;
    int index;
    int d;
    int start;
    List<Integer> indices;
    ArrayList<Items> itemsArrayList;
    TextView tv;
    int end;
    int c;
    int cInd;
    String loc;
    ProgressBar iProgressBar;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;

    ArrayList<ArrayList<CCartItem>> cCart,cart,ordered;
    ArrayList<Shopkeeper> shopList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_customer_items );
        final String name=getIntent ().getStringExtra ( "Name" );
        for(int i=0;i<ssList.size ();i++){
            if(ssList.get ( i ).getSKshop ().equals ( name )){
                c=i;
            }
        }
        loc=ssList.get ( c ).getSKstreet ()+","+ssList.get ( c ).getSKdist ()+","+ssList.get ( c ).getSKstate ()+", India-"+ssList.get ( c ).getSKpin ();
        cInd=getIntent ().getIntExtra ( "CIndex",0 );
        tv=findViewById ( R.id.phone );
        indices=new ArrayList<Integer> (  );
        itemsArrayList=new ArrayList<> (  );
        iProgressBar=findViewById ( R.id.iProgress );
        items_rv=findViewById ( R.id.items_recycler );
        cCart=new ArrayList<> (  );
        cart=new ArrayList<> (  );
        ordered=new ArrayList<> (  );
        shopList=new ArrayList<> (  );
        index=c;
        if(c==0){
            if(sizes.get ( c )!=0) {
                end = sizes.get ( 0 );
                start = 0;
            }

        }else{
            if(sizes.get ( c )!=0) {
                end= sizes.get ( c );
                d=c-1;
                while(sizes.get ( d )==0 & d>0 ){
                    d=d-1;
                }

                start= sizes.get ( d );
            }
        }
        for(int i=start;i<end;i++){
            indices.add(i);
        }
        for(int i:indices){
            itemsArrayList.add ( itemsList.get ( i ) );
        }
        tv.setText ( ssList.get ( index ).getSKphone () );
        Toast.makeText ( CustomerItems.this,"Pick your items here!",Toast.LENGTH_SHORT ).show ();
        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();

        for(int i=0;i<ssList.size ();i++){
            if(scList.get ( cInd ).getCpin ().equals ( ssList.get ( i ).getSKpin () )){
                shopList.add ( ssList.get ( i ) );
            }
        }
        cCart.clear ();
        if(shopList.size ()!=0) {
            for (int i = 0; i < shopList.size (); i++) {
                final ArrayList<CCartItem> cCartItems = new ArrayList<> ();
                final int finalI = i;
                sReference.child ( "Cart" + scList.get ( cInd ).getCphone () ).child ( shopList.get ( i ).getSKshop () ).addValueEventListener ( new ValueEventListener () {
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

        iProgressBar.setVisibility ( View.VISIBLE );
        Runnable r = new Runnable() {
            @Override
            public void run(){

                iProgressBar.setVisibility ( View.INVISIBLE );
                if(cCart.size ()!=0){
                    for(int i=0;i<cCart.size ();i++){
                        if(cCart.get ( i ).get ( 0 ).getcStatus ().equals ( "0" )){
                            cart.add ( cCart.get ( i ) );
                        }
                        else if(cCart.get ( i ).get ( 0 ).getcStatus ().equals ( "1" ) || cCart.get ( i ).get ( 0 ).getcStatus ().equals ( "2" )){
                            ordered.add ( cCart.get ( i ) );
                        }

                    }
                }

                if(itemsArrayList.size ()==0){
                    Toast.makeText ( CustomerItems.this,"Sorry! No items added in this shop" ,Toast.LENGTH_SHORT ).show ();
                }
                else{
                    items_rv.setLayoutManager ( new LinearLayoutManager (CustomerItems.this) );
                    CustomerItemsAdapter customerItemsAdapter=new CustomerItemsAdapter (CustomerItems.this,itemsArrayList,index,cInd,cart,name,ordered);
                    items_rv.setAdapter ( customerItemsAdapter );
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

    public void call(View view) {
        Toast.makeText ( this, "Calling", Toast.LENGTH_SHORT ).show ();
        Uri u = Uri.parse ( "tel:"+tv.getText() );
        Intent intent = new Intent ( Intent.ACTION_CALL, u );
        final int REQUEST_PHONE_CALL = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission ( CustomerItems.this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions ( CustomerItems.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL );
            } else {
                startActivity ( intent );
            }
        } else {
            startActivity ( intent );
        }
    }

    public void location(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+loc);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void back(View view) {
        Intent in =new Intent ( CustomerItems.this,CustomerHome.class );
        in.putExtra ( "Phone",scList.get ( cInd ).getCphone () );
        startActivity ( in );
    }
}