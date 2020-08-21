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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sBill extends Global {

    int iInd,sInd;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;

    ArrayList<ArrayList<CCartItem>> skBill;
    ArrayList<Customer> cList;
    ArrayList<CCartItem> sBill;

    ProgressBar progressBar;
    TextView phn;
    TextInputEditText total;
    String cPhn,shop,ph;
    RecyclerView rv;
    Double t;
    Button packed;
    String status,id2;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_s_bill );
        iInd=getIntent ().getIntExtra ( "iInd",0 );
        sInd=getIntent ().getIntExtra ( "sInd",0 );
        cPhn=getIntent ().getStringExtra ("Phone" );
        shop=getIntent ().getStringExtra ("Shop" );
        ph=getIntent ().getStringExtra ( "ph" );
        status=getIntent ().getStringExtra ( "Status" );
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        if(scList.size ()!=0){
            for (int i=0;i<scList.size ();i++){
                if(scList.get ( i ).getCphone ().equals ( cPhn )){
                    id2=scList.get ( i ).getCid ();
                }
            }
        }


        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();
        sBill = new ArrayList<> (  );
        skBill=new ArrayList<> (  );
        cList=new ArrayList<> (  );
        t=0.0;

        progressBar=findViewById ( R.id.sbillProgress );
        phn=findViewById ( R.id.sbPhone );
        total=findViewById ( R.id.sbTotal );
        rv=findViewById ( R.id.sbill_recycler );
        packed=findViewById ( R.id.orderPacked );
        if(status.equals ( "1" )){
            packed.setEnabled ( true );
        }

        phn.setText ( cPhn );

        for(int i=0;i<scList.size ();i++){
            if(ssList.get ( sInd ).getSKpin ().equals ( scList.get ( i ).getCpin () )){
                cList.add ( scList.get ( i ) );
            }
        }
        skBill.clear ();

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
                            if(cCartItems.get ( 0 ).getcStatus ().equals (status )){
                                skBill.add ( cCartItems );
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

                sBill = skBill.get ( iInd );
                if(!sBill.get ( 0 ).getcStatus ().equals ( "1" ))
                {
                    packed.setEnabled ( false );
                }

                if(sBill.size ()==0){
                    Toast.makeText ( sBill.this,"No items in the bill",Toast.LENGTH_SHORT ).show ();
                    total.setText ( "0.0" );
                }
                else {
                    for(int i=0;i<sBill.size ();i++){
                        t=t+Double.parseDouble ( sBill.get ( i ).getcItTotal () );
                    }
                    total.setText ( String.valueOf ( t ) );
                    rv.setLayoutManager ( new LinearLayoutManager (sBill.this) );
                    SBillAdapter SbillAdapter=new SBillAdapter (sBill.this,sBill);
                    rv.setAdapter ( SbillAdapter );

                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 5000);



    }
    public void onBackPressed() {
        // Add the Back key handler here.
        return;
    }

    public void completed(View view) {

            DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
            Query query=reference.child ( "Cart" +sBill.get ( 0 ).getcCustPhone () ).child ( sBill.get ( 0 ).getCShopName () ).orderByChild ( "cItName" ).equalTo ( sBill.get ( 0 ).getcItName () );
            final HashMap<String,Object> map=new HashMap<> (  );
            query.addListenerForSingleValueEvent ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data:dataSnapshot.getChildren ()){
                        map.put ( "cStatus","2" );

                        data.getRef ().updateChildren ( map );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
            Query query1=reference.child ( "Orders" +ph  ).child (sBill.get ( 0 ).getcCustPhone ()  ).orderByChild ( "cItName" ).equalTo ( sBill.get ( 0 ).getcItName ());
            final HashMap<String,Object> map1=new HashMap<> (  );
            query1.addListenerForSingleValueEvent ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot data:dataSnapshot.getChildren ()){
                        map1.put ( "cStatus","2" );

                        data.getRef ().updateChildren ( map1 );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
            getKey( id2);
            progressBar.setVisibility ( View.VISIBLE );

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                new SendNotification ("Order packed from the shop "+sBill.get ( 0 ).getCShopName (),"Order Packed",key);
                progressBar.setVisibility ( View.INVISIBLE );
                Toast.makeText ( sBill.this, "Order Packed Successfully and Customer is notified", Toast.LENGTH_SHORT ).show ();
                packed.setEnabled ( false );
                Intent in = new Intent ( sBill.this, SKOrders.class );
                in.putExtra ( "Ph", ph );
                in.putExtra ( "Index", sInd );
                startActivity ( in );


            }
        };
        Handler h1 = new Handler();
        h1.postDelayed(r1, 5000);




    }

    public void call(View view) {
        Toast.makeText ( this, "Calling", Toast.LENGTH_SHORT ).show ();
        Uri u = Uri.parse ( "tel:"+phn.getText() );
        Intent intent = new Intent ( Intent.ACTION_CALL, u );
        final int REQUEST_PHONE_CALL = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission ( sBill.this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions ( sBill.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL );
            } else {
                startActivity ( intent );
            }
        } else {
            startActivity ( intent );
        }
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




    public void backOrders(View view) {
        Intent in = new Intent ( sBill.this, SKOrders.class );
        in.putExtra ( "Ph", ph );
        in.putExtra ( "Index", sInd );
        startActivity ( in );
    }
}