package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.List;

public class ShopkeeperHome extends Global {

    RecyclerView skrv;
    String sPh;
    int c;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    ArrayList<Items> itemsArrayList;
    List<Integer> indices;
    int d;
    int start;
    int end;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_shopkeeper_home );
        sPh=getIntent ().getStringExtra ( "Phone" );
        OneSignal.startInit ( this ).init ();
        OneSignal.setSubscription ( true );
        OneSignal.idsAvailable ( new OneSignal.IdsAvailableHandler () {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                String id= ssList.get ( c ).getSKid ();
                FirebaseDatabase.getInstance ().getReference ().child ( "user" ).child ( FirebaseAuth.getInstance ().getUid () ).child ( "key" ).setValue ( userId );

            }
        } );
        OneSignal.setInFocusDisplaying ( OneSignal.OSInFocusDisplayOption.Notification );


        databaseReference= FirebaseDatabase.getInstance ().getReference ("Items"+sPh);
        progressBar=findViewById ( R.id.hProgress );
        itemsArrayList=new ArrayList<> (  );
        skrv=findViewById ( R.id.skrecycler );
        indices=new ArrayList<Integer> (  );
        for(int i=0;i<ssList.size ();i++){
            if(ssList.get ( i ).getSKphone ().equals ( sPh )){
                c=i;
                index=i;
            }
        }

        if(c==0){
            if(sizes.get ( 0 )!=0) {
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

        databaseReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    itemsArrayList.clear ();
                    for (DataSnapshot data:dataSnapshot.getChildren ()){
                        Items items =data.getValue ( Items.class );
                        itemsArrayList.add ( items );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        databaseReference.removeEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    itemsArrayList.clear ();
                    for (DataSnapshot data:snapshot.getChildren ()){
                        Items items =data.getValue ( Items.class );
                        itemsArrayList.add ( items );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

        progressBar.setVisibility ( View.VISIBLE );
        Runnable r = new Runnable() {
            @Override
            public void run(){
                progressBar.setVisibility ( View.INVISIBLE );

                if(itemsArrayList.size ()==0){
                    Toast.makeText ( ShopkeeperHome.this,"Sorry! No items added in your home page" ,Toast.LENGTH_SHORT ).show ();
                }
                else{
                    skrv.setLayoutManager ( new LinearLayoutManager ( ShopkeeperHome.this ) );
                    skrv.setAdapter ( new SKAdapter ( ShopkeeperHome.this,itemsArrayList,sPh) );

                }
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, 7000);
    }
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.
        return;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shopkeeper_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.skoption_orders:
                Intent it =new Intent ( ShopkeeperHome.this,SKOrders.class );
                it.putExtra ( "Index",index );
                it.putExtra ( "Ph",sPh );
                startActivity ( it );

                return true;
            case R.id.skoption_profile:
                Intent i =new Intent ( ShopkeeperHome.this,ShopkeeperProfile.class );
                i.putExtra ( "Index",index );
                startActivity ( i );

                return true;
            case R.id.skoption_add:
                Intent intent1 =new Intent ( ShopkeeperHome.this,AddItem.class );
                intent1.putExtra ( "Ph",sPh );
                startActivity ( intent1 );

                return true;
            case R.id.skoption_logout:
                OneSignal.setSubscription ( false );
                Intent intent2 =new Intent ( ShopkeeperHome.this,SignIn.class );
                FirebaseAuth.getInstance ().signOut ();
                startActivity ( intent2 );

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}