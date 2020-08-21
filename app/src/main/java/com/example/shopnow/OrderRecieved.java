package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderRecieved extends Global {

    Integer cInd;
    String ph;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> cBill;
    RecyclerView rv;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_order_recieved );
        cInd=getIntent ().getIntExtra ( "CInd",0 );
        ph=scList.get ( cInd ).getCphone ();
        cBill=new ArrayList<> (  );
        rv=findViewById ( R.id.ORRecycler );
        progressBar=findViewById ( R.id.ORProgress );

        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();

        cBill.clear ();
        sReference.child ( "Recieved"+ph ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists ()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren ()){
                        cBill.add ( dataSnapshot.getValue (String.class) );
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
            public void run() {
                progressBar.setVisibility ( View.INVISIBLE );
                int count=0;
                if(cBill.size ()!=0){
                    rv.setLayoutManager ( new LinearLayoutManager ( OrderRecieved.this ) );
                    ORDAdapter ordAdapter = new ORDAdapter ( OrderRecieved.this, cBill,ph,cInd );
                    rv.setAdapter ( ordAdapter );
                }else {
                    Toast.makeText ( OrderRecieved.this,"No items in the cart",Toast.LENGTH_SHORT ).show ();
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
        Intent in =new Intent ( OrderRecieved.this,CustomerHome.class );
        in.putExtra ( "Phone",ph );
        startActivity ( in );
    }
}