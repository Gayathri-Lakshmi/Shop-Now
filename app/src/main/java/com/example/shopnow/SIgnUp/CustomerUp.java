package com.example.shopnow.SIgnUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shopnow.Customer;
import com.example.shopnow.PhoneVerification;
import com.example.shopnow.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerUp extends AppCompatActivity {

    TextInputEditText custName,custPhone,custAdd,custDist,custState,custPin;
    FirebaseAuth cAuth;
    DatabaseReference cReference;
    ArrayList<Customer> cList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_customer_up );
        custName=findViewById ( R.id.cust_name );
        custPhone=findViewById ( R.id.cust_phone );
        custAdd=findViewById ( R.id.cust_add );
        custDist=findViewById ( R.id.cust_dist );
        custState=findViewById ( R.id.cust_state );
        custPin = findViewById ( R.id.cust_pin );
        cAuth=FirebaseAuth.getInstance ();
        cList=new ArrayList<> (  );
        cReference= FirebaseDatabase.getInstance ().getReference ("CustomerDetails");
        cReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    cList.clear ();
                    for (DataSnapshot data:dataSnapshot.getChildren ()){
                        Customer customer=data.getValue (Customer.class);
                        cList.add ( customer );
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    public void join(View view) {
        String cName=custName.getText ().toString ();
        String cPhone=custPhone.getText ().toString ();
        String cStreet=custAdd.getText ().toString ();
        String cDist=custDist.getText ().toString ();
        String cState=custState.getText ().toString ();
        String cPin=custPin.getText ().toString ();
        cPhone="+91 "+cPhone;
        int count=0;
        for(int i = 0; i<cList.size (); i++){
            if(cPhone.equals ( cList.get ( i ).getCphone () )){
                count++;
            }
        }
        if(cName.isEmpty ()  || cStreet.isEmpty () || cDist.isEmpty () || cState.isEmpty () || cPin.isEmpty ()){
            Toast.makeText ( CustomerUp.this,"Please fill all the details.",Toast.LENGTH_SHORT ).show ();
        }
        else if(count>0){
            Toast.makeText ( CustomerUp.this,"The Phone Number already exists",Toast.LENGTH_SHORT  ).show ();
        }
        else if(cPhone.length ()!=14){
            Toast.makeText ( CustomerUp.this,"Enter valid phone number",Toast.LENGTH_SHORT  ).show ();
        }
        else{


            Intent i =new Intent ( CustomerUp.this, PhoneVerification.class );
            i.putExtra ( "Phone",cPhone );
            i.putExtra ( "Name",cName );
            i.putExtra ( "Shop","null" );
            i.putExtra ( "Add",cStreet );
            i.putExtra ( "Dist",cDist );
            i.putExtra ( "State",cState );
            i.putExtra ( "zip",cPin );
            i.putExtra ( "Cat","null" );
            i.putExtra ( "type","c" );
            startActivity ( i );
        }

    }
}