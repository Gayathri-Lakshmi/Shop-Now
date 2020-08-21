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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ORDBill extends AppCompatActivity {

    String type,ph;
    int index,position;
    TextView phnEt;
    TextInputEditText totalEt;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> Bill;
    RecyclerView rv;
    ProgressBar progressBar;
    String[] strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_o_r_d_bill );
        type=getIntent ().getStringExtra ( "type" );
        ph=getIntent ().getStringExtra ( "Ph" );
        index=getIntent ().getIntExtra ( "Ind",0 );
        position=getIntent ().getIntExtra ( "pos",0 );
        Bill= new ArrayList<> (  );
        progressBar=findViewById ( R.id.billProgress );
        phnEt=findViewById ( R.id.bPhone );
        totalEt=findViewById ( R.id.bTotal );
        rv=findViewById ( R.id.bill_recycler );
        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();

        if(type.equals ( "c" )){
            Bill.clear ();
            sReference.child ( "Recieved"+ph ).addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists ()){
                        for (DataSnapshot dataSnapshot:snapshot.getChildren ()){
                            Bill.add ( dataSnapshot.getValue (String.class) );
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            } );
        }
        else {
            Bill.clear ();
            sReference.child ( "Done" + ph ).addValueEventListener ( new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists ()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren ()) {
                            Bill.add ( dataSnapshot.getValue ( String.class ) );
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            } );
        }
            progressBar.setVisibility ( View.VISIBLE );
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    strings=Bill.get ( position ).split ( ";" );
                    int c;
                    c=(strings.length-5)/4;
                    if(type.equals ( "c" )){
                        phnEt.setText ( strings[2] );
                    }
                    else {
                        phnEt.setText ( strings[1] );
                    }
                    totalEt.setText ( strings[4] );
                    progressBar.setVisibility ( View.INVISIBLE );
                    if(c!=0){
                        rv.setLayoutManager ( new LinearLayoutManager ( ORDBill.this ) );
                        ORDBillAdapter ordBillAdapter = new ORDBillAdapter ( ORDBill.this, strings,c );
                        rv.setAdapter ( ordBillAdapter );
                    }else {
                        Toast.makeText ( ORDBill.this,"No items in the cart",Toast.LENGTH_SHORT ).show ();
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
        String phCall;
        if(type.equals ( "c" )){
            phCall= strings[2] ;
        }
        else {
            phCall=strings[1];
        }
        Toast.makeText ( this, "Calling", Toast.LENGTH_SHORT ).show ();
        Uri u = Uri.parse ( "tel:"+phCall );
        Intent intent = new Intent ( Intent.ACTION_CALL, u );
        final int REQUEST_PHONE_CALL = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission ( ORDBill.this, Manifest.permission.CALL_PHONE ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions ( ORDBill.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL );
            } else {
                startActivity ( intent );
            }
        } else {
            startActivity ( intent );
        }
    }

    public void back(View view) {
        if(type.equals ( "c" )){
            Intent i=new Intent ( ORDBill.this,OrderRecieved.class );
            i.putExtra ( "CInd",index );
            startActivity ( i );
        }
        else {
            Intent intent = new Intent ( ORDBill.this, OrderDone.class );
            intent.putExtra ( "Ph", ph );
            intent.putExtra ( "Index", index );
            startActivity ( intent );
        }
    }
}