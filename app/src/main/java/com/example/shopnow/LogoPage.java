package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogoPage extends Global {

    String type,phn,ph,p;
    DatabaseReference sReference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_logo_page );
        type="c";


        type=getIntent ().getStringExtra ( "type" );
        phn=getIntent ().getStringExtra ( "Phone" );
        progressBar=findViewById ( R.id.logoProgress );
        FirebaseUser user= FirebaseAuth.getInstance ().getCurrentUser ();
        if(user!=null){
            if(phn == null){
                phn=user.getPhoneNumber ();
                assert phn != null;
                ph=phn.substring ( 0,3 )+" "+phn.substring ( 3,13);
                ssList=new ArrayList<> (  );
                scList=new ArrayList<> (  );
                itemsList=new ArrayList<> (  );
                sizes=new ArrayList<> (  );
                progressBar.setVisibility ( View.VISIBLE );
                sReference= FirebaseDatabase.getInstance ().getReference ();
                sReference.child ( "ShopkeeperDetails" ).addValueEventListener ( new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            ssList.clear ();
                            for (DataSnapshot data:dataSnapshot.getChildren ()){
                                Shopkeeper shopkeeper=data.getValue (Shopkeeper.class);
                                ssList.add ( shopkeeper );
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
                sReference.child ( "CustomerDetails" ).addValueEventListener ( new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            scList.clear ();
                            for (DataSnapshot data:dataSnapshot.getChildren ()){
                                Customer customer=data.getValue (Customer.class);
                                scList.add ( customer );
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
                Runnable r = new Runnable() {
                    @Override
                    public void run(){
                        sizes.clear ();
                        for (int i=0;i<ssList.size ();i++){
                            final int finalI = i;
                            sReference.child ( "Items"+ssList.get ( i ).getSKphone () ).addValueEventListener ( new ValueEventListener () {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot data:dataSnapshot.getChildren ()){
                                            Items items=data.getValue (Items.class);
                                            itemsList.add ( items );

                                        }
                                    }
                                    if(finalI==0){
                                        if(itemsList.size ()==0){
                                            sizes.add ( 0 );
                                        }
                                        else {
                                            sizes.add ( itemsList.size () );
                                        }
                                    }
                                    else {
                                        if(itemsList.size ()==sizes.get ( finalI-1 )){
                                            sizes.add ( 0 );
                                        }else {
                                            sizes.add ( itemsList.size () );
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            } );

                        }


                    }
                };
                Handler h = new Handler();
                h.postDelayed(r, 5000);
                Runnable r1 = new Runnable() {
                    @Override
                    public void run(){

                        for(int i=0;i<scList.size ();i++){
                            if(scList.get ( i ).getCphone ().equals ( ph )){
                                type="c";
                            }
                        }
                        for(int i=0;i<ssList.size ();i++){
                            if(ssList.get ( i ).getSKphone ().equals ( ph )){
                                type="sk";
                            }
                        }
                        progressBar.setVisibility ( View.INVISIBLE );
                        if (type.equals ( "c" )) {
                            Intent i = new Intent ( LogoPage.this, CustomerHome.class );
                            i.putExtra ( "Phone", ph );
                            startActivity ( i );
                        } else {
                            Intent i = new Intent ( LogoPage.this, ShopkeeperHome.class );
                            i.putExtra ( "Phone", ph );
                            startActivity ( i );
                        }


                    }
                };
                Handler h1 = new Handler();
                h1.postDelayed(r1, 15000);


            }
            else {
                if (type.equals ( "c" )) {
                    Intent i = new Intent ( LogoPage.this, CustomerHome.class );
                    i.putExtra ( "Phone", phn );
                    startActivity ( i );
                } else {
                    Intent i = new Intent ( LogoPage.this, ShopkeeperHome.class );
                    i.putExtra ( "Phone", phn );
                    startActivity ( i );
                }
            }
        }
        else {
            Intent i=new Intent ( LogoPage.this,SignIn.class );
            startActivity ( i );
        }
    }
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.
        return;
    }
}