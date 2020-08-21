package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AppComponentFactory;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerHome extends Global {

    RecyclerView rv;
    String cPh;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;
    String name;
    ProgressBar chProgressBar;
    int c;
    ArrayList<Shopkeeper> shopList;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_customer_home );
        cPh=getIntent ().getStringExtra ( "Phone" );
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
        rv=findViewById ( R.id.recycler );
        chProgressBar=findViewById ( R.id.chProgress );

        shopList=new ArrayList<> (  );

        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();

        for(int i=0;i<scList.size ();i++){
            if(scList.get ( i ).getCphone ().equals ( cPh )){
                c=i;
            }
        }
        for(int i=0;i<ssList.size ();i++){
            if(scList.get ( c ).getCpin ().equals ( ssList.get ( i ).getSKpin () )){
                shopList.add ( ssList.get ( i ) );
            }
        }

        chProgressBar.setVisibility ( View.VISIBLE );
        Runnable r = new Runnable() {
            @Override
            public void run() {

                chProgressBar.setVisibility ( View.INVISIBLE );
                if (ssList.size () == 0) {
                    Toast.makeText ( CustomerHome.this, "Sorry! No shops in your locality", Toast.LENGTH_LONG ).show ();
                } else {
                    rv.setLayoutManager ( new LinearLayoutManager ( CustomerHome.this ) );
                    rv.setAdapter ( new Adapter ( CustomerHome.this, shopList, c ) );

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_profile:
                Intent i =new Intent ( CustomerHome.this,CustomerProfile.class );
                i.putExtra ( "Index",c );
                startActivity ( i );

                return true;
            case R.id.option_cart:
                Intent in =new Intent ( CustomerHome.this,CustomerCart.class );
                in.putExtra ( "CInd",c );
                startActivity ( in );
                return true;
            case R.id.option_recieved:
                Intent intent1 =new Intent ( CustomerHome.this,OrderRecieved.class );
                intent1.putExtra ( "CInd",c );
                startActivity ( intent1 );
                return true;
            case R.id.option_logout:
                OneSignal.setSubscription ( false );
                FirebaseAuth.getInstance ().signOut ();
                Intent intent =new Intent ( CustomerHome.this,SignIn.class );
                startActivity ( intent );

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}