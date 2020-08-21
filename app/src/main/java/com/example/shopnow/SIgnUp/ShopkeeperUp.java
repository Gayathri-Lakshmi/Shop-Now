package com.example.shopnow.SIgnUp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.shopnow.PhoneVerification;
import com.example.shopnow.R;
import com.example.shopnow.Shopkeeper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShopkeeperUp extends AppCompatActivity {

    TextInputEditText skName,shopName,skPhone,skAdd,skDist,skState,skZip;
    CheckBox med,market,store,dairy;
    DatabaseReference skReference;
    ArrayList<Shopkeeper> skList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_shopkeeper_up );
        skList=new ArrayList<> (  );
        skName=findViewById ( R.id.sk_name );
        shopName=findViewById ( R.id.shop_name );
        skPhone=findViewById ( R.id.sk_phone );
        skAdd=findViewById ( R.id.sk_add );
        skDist=findViewById ( R.id.sk_dist );
        skState=findViewById ( R.id.sk_state );
        skZip=findViewById ( R.id.sk_zip );
        med=findViewById ( R.id.sk_medicals );
        market=findViewById ( R.id.sk_market );
        store=findViewById ( R.id.sk_store );
        dairy=findViewById ( R.id.sk_dairy );
        skReference= FirebaseDatabase.getInstance ().getReference ("ShopkeeperDetails");
        skReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    skList.clear ();
                    for (DataSnapshot data:dataSnapshot.getChildren ()){
                        Shopkeeper shopkeeper=data.getValue (Shopkeeper.class);
                        skList.add ( shopkeeper );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    public void join(View view) {
        String name=skName.getText ().toString ();
        String shop=shopName.getText ().toString ();
        String phone=skPhone.getText ().toString ();
        String add=skAdd.getText ().toString ();
        String dist=skDist.getText ().toString ();
        String state=skState.getText ().toString ();
        String zip=skZip.getText ().toString ();
        String category="" ;
        phone="+91 "+phone;
        if(med.isChecked ()){
            category=category.concat ( "Medicals\t" );
        }
        if(market.isChecked ()){
            category=category.concat ( "SuperMarket\t" );
        }
        if(store.isChecked ()){
            category=category.concat ( "Fruits and Vegetables Store\t" );
        }
        if(dairy.isChecked ()){
            category=category.concat ( "Dairy" );
        }
        int count=0;
        for(int i = 0; i<skList.size (); i++){
            if(phone.equals ( skList.get ( i ).getSKphone () )){
                count++;
            }
        }

        if(name.isEmpty () || shop.isEmpty () || phone.isEmpty ()  || add.isEmpty () || dist.isEmpty () || state.isEmpty () || zip.isEmpty ()
                || category.isEmpty ()){
            Toast.makeText (ShopkeeperUp.this,"Please fill all the details",Toast.LENGTH_SHORT).show ();
        }
        else if(phone.length ()!=14){
            Toast.makeText ( ShopkeeperUp.this,"Enter valid phone number",Toast.LENGTH_SHORT  ).show ();
        }
        else if(count>0){
            Toast.makeText ( ShopkeeperUp.this,"The Phone Number already exists",Toast.LENGTH_SHORT  ).show ();
        }
        else{


            Intent i =new Intent ( ShopkeeperUp.this, PhoneVerification.class );
            i.putExtra ( "Phone",phone );
            i.putExtra ( "Name",name );
            i.putExtra ( "Shop",shop );
            i.putExtra ( "Add",add );
            i.putExtra ( "Dist",dist );
            i.putExtra ( "State",state );
            i.putExtra ( "zip",zip );
            i.putExtra ( "Cat",category );

            i.putExtra ( "type","sk" );
            startActivity ( i );
        }
    }
}