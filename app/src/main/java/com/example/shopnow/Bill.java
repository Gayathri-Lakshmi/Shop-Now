package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bill extends Global {

    int iInd,cInd;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;

    ArrayList<ArrayList<CCartItem>> cBill;
    ArrayList<Shopkeeper> shopList;
    ArrayList<CCartItem> sBill;

    ProgressBar progressBar;
    TextView phn;
    TextInputEditText total;
    String shopPhn,shopLoc,shop;
    RecyclerView rv;
    Double t;
    String status,id1,id2;
    Button placeOrder,shipped;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_bill );
        iInd=getIntent ().getIntExtra ( "iInd",0 );
        cInd=getIntent ().getIntExtra ( "cInd",0 );
        shopPhn=getIntent ().getStringExtra ( "Phone" );
        shopLoc=getIntent ().getStringExtra ( "Loc" );
        shop=getIntent ().getStringExtra ( "Shop" );
        status=getIntent ().getStringExtra ( "Status" );
        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);


        sBill = new ArrayList<> (  );
        cBill=new ArrayList<> (  );
        shopList=new ArrayList<> (  );
        id1=scList.get ( cInd).getCid ();
        if(ssList.size ()!=0){
            for (int i=0;i<ssList.size ();i++){
                if(ssList.get ( i ).getSKshop ().equals ( shop )){
                    id2=ssList.get ( i ).getSKid ();
                }
            }
        }
        t=0.0;


        progressBar=findViewById ( R.id.billProgress );
        phn=findViewById ( R.id.bPhone );
        total=findViewById ( R.id.bTotal );
        rv=findViewById ( R.id.bill_recycler );
        placeOrder=findViewById ( R.id.buttonOrder );
        shipped=findViewById ( R.id.buttonShipped );

        if(! status.equals ("0")){
            placeOrder.setEnabled ( false );
        }
        if(status.equals ( "2" )){
            shipped.setEnabled ( true );
        }

        phn.setText ( shopPhn );

        if(ssList.size ()!=0) {
            for (int i = 0; i < ssList.size (); i++) {
                if (scList.get ( cInd ).getCpin ().equals ( ssList.get ( i ).getSKpin () )) {
                    shopList.add ( ssList.get ( i ) );
                }
            }
        }

        cBill.clear ();

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
                            cBill.add ( cCartItems );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                } );
                final int finalI1 = i;
                sReference.child ( "Cart" + scList.get ( cInd ).getCphone () ).child ( shopList.get ( i ).getSKshop () ).removeEventListener ( new ValueEventListener () {
                    @Override

                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            if(finalI1 ==0){
                                cBill.clear ();
                            }
                            for (DataSnapshot data:snapshot.getChildren ()){
                                cCartItems.add ( data.getValue ( CCartItem.class ) );
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
                progressBar.setVisibility ( View.INVISIBLE );

                sBill = cBill.get ( iInd );

                if(sBill.size ()==0){
                    Toast.makeText ( Bill.this,"No items in the bill",Toast.LENGTH_SHORT ).show ();
                    total.setText ( "0.0" );
                }
                else {
                    for(int i=0;i<sBill.size ();i++){
                        t=t+Double.parseDouble ( sBill.get ( i ).getcItTotal () );
                    }
                    total.setText ( String.valueOf ( t ) );
                    rv.setLayoutManager ( new LinearLayoutManager (Bill.this) );
                    BillAdapter billAdapter=new BillAdapter (Bill.this,sBill,cInd,shop,iInd,shopPhn,shopLoc,status);
                    rv.setAdapter ( billAdapter );

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
        Uri u = Uri.parse ( "tel:"+phn.getText() );
        Intent intent = new Intent ( Intent.ACTION_CALL, u );
        final int REQUEST_PHONE_CALL = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission ( Bill.this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions ( Bill.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL );
            } else {
                startActivity ( intent );
            }
        } else {
            startActivity ( intent );
        }
    }

    public void location(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+shopLoc);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void placeOrder(View view) {
        progressBar.setVisibility ( View.VISIBLE );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        final String currentDT = sdf.format(new Date ());
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
        Query query=reference.child ( "Cart" +scList.get ( cInd ).getCphone ()  ).child ( shop ).orderByChild ( "cItName" ).equalTo ( sBill.get ( 0 ).getcItName () );
        final HashMap<String,Object> map=new HashMap<> (  );
        query.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren ()){
                    map.put ( "cStatus","1" );
                    map.put ( "cTime",currentDT );

                    data.getRef ().updateChildren ( map );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        Query query1=reference.child ( "Orders" +shopPhn  ).child ( scList.get ( cInd ).getCphone () ).orderByChild ( "cItName" ).equalTo ( sBill.get ( 0 ).getcItName ());
        final HashMap<String,Object> map1=new HashMap<> (  );
        query1.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren ()){
                    map1.put ( "cStatus","1" );
                    map1.put ( "cTime",currentDT );

                    data.getRef ().updateChildren ( map1 );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        getKey( id2);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                new SendNotification ("New Order received from the customer"+sBill.get ( 0 ).getcCustPhone (),"New Order!",key);
                progressBar.setVisibility ( View.INVISIBLE );
                Toast.makeText ( Bill.this,"Order Placed Successfully",Toast.LENGTH_SHORT ).show ();
                Intent i = new Intent(Bill.this,CustomerCart.class);
                i.putExtra ( "CInd",cInd );
                startActivity ( i );

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 5000);

    }


    public void back(View view) {
        Intent i = new Intent(Bill.this,CustomerCart.class);
        i.putExtra ( "CInd",cInd );
        startActivity ( i );
    }

    public void orderShipped(View view) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        final String cDT = sdf.format(new Date ());
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();

        reference.child ( "Orders" +shopPhn  ).child ( scList.get ( cInd ).getCphone () ).removeValue ();
        reference.child ( "Cart" +scList.get ( cInd ).getCphone ()  ).child ( shop ).removeValue ();

        StringBuilder bill = new StringBuilder ( cDT + ";" + sBill.get ( 0 ).getcCustPhone () + ";" + sBill.get ( 0 ).getCShopPhone () + ";" + sBill.get ( 0 ).getCShopName ()+";"+String.valueOf ( t )+";");
        if(sBill.size ()!=0){
            for(int i=0;i<sBill.size ();i++){
                bill.append ( sBill.get ( i ).getcItName () ).append ( ";" ).append ( sBill.get ( i ).getcItPrice () ).append ( ";" ).append ( sBill.get ( i ).getcItemQty () ).append ( ";" ).append ( sBill.get ( i ).getcItTotal () ).append ( ";" );
            }
        }
        reference.child ( "Recieved" +scList.get ( cInd ).getCphone ()  ).child ( cDT).setValue ( bill.toString () );
        reference.child ( "Done" +shopPhn  ).child ( cDT ).setValue ( bill.toString () );
        getKey( id2);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                new SendNotification ("Order received by the customer "+sBill.get ( 0 ).getcCustPhone (),"Order Successfully shipped!",key);
                progressBar.setVisibility ( View.INVISIBLE );
                Toast.makeText ( Bill.this,"Hope you had great shopping!",Toast.LENGTH_SHORT ).show ();
                Intent i = new Intent(Bill.this,CustomerCart.class);
                i.putExtra ( "CInd",cInd );
                startActivity ( i );

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 5000);


    }

    public void getKey(String id){
        DatabaseReference bReference=FirebaseDatabase.getInstance ().getReference ().child ( "user" ).child ( id );
        bReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child ( "key" ).getValue ()!=null){
                    key=snapshot.child ( "key" ).getValue ().toString ();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

    }
}