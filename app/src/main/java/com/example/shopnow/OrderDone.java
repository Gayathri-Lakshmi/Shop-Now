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

public class OrderDone extends AppCompatActivity {

    String ph;
    int sInd;
    DatabaseReference sReference;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String > skBill;
    RecyclerView rv;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_order_done );
        ph=getIntent ().getStringExtra ( "Ph" );
        sInd=getIntent ().getIntExtra ( "Index",0 );
        skBill=new ArrayList<> (  );
        rv=findViewById ( R.id.ORRecycler );
        progressBar=findViewById ( R.id.ORProgress );


        firebaseDatabase = FirebaseDatabase.getInstance();
        sReference = firebaseDatabase.getReference();

        skBill.clear ();
        sReference.child ( "Done"+ph ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists ()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren ()){
                        skBill.add ( dataSnapshot.getValue (String.class) );
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
                if(skBill.size ()!=0){
                    rv.setLayoutManager ( new LinearLayoutManager ( OrderDone.this ) );
                    ODAdapter odAdapter = new ODAdapter ( OrderDone.this, skBill,ph,sInd );
                    rv.setAdapter ( odAdapter );
                }else {
                    Toast.makeText ( OrderDone.this,"No items in the cart",Toast.LENGTH_SHORT ).show ();
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
        Intent i = new Intent ( OrderDone.this, ShopkeeperHome.class );
        i.putExtra ( "Ph", ph );
        i.putExtra ( "Stat", 1);
        i.putExtra ( "Index", sInd );
        startActivity ( i );
    }
}