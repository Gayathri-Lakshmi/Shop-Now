package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.Index;

import java.util.HashMap;

public class ShopkeeperProfile extends Global {

    EditText skNameP,skPhoneP,skShopP,skCityP,skStateP,skPinP,skAddP;
    ImageButton ibSKEdit;
    CheckBox medP,marketP,storeP,dairyP;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_shopkeeper_profile );
        i=getIntent ().getIntExtra ( "Index",0 );
        skNameP=findViewById ( R.id.cNameProfile);
        skShopP=findViewById ( R.id.skShopNameProfile );
        skPhoneP=findViewById ( R.id.skPhoneProfile);
        skAddP=findViewById ( R.id.skAddProfile );
        skCityP=findViewById ( R.id.skDistProfile );
        skStateP=findViewById ( R.id.skStateProfile );
        skPinP=findViewById ( R.id.skPinProfile );

        ibSKEdit=findViewById ( R.id.img_skEdit );
        medP=findViewById ( R.id.sk_medicalsP );
        marketP=findViewById ( R.id.sk_marketP );
        storeP=findViewById ( R.id.sk_storeP );
        dairyP=findViewById ( R.id.sk_dairyP );

        skNameP.setText ( ssList.get (i).getSKname () );
        skShopP.setText ( ssList.get ( i ).getSKshop () );
        skPhoneP.setText ( ssList.get ( i ).getSKphone () );
        skAddP.setText ( ssList.get ( i ).getSKstreet () );
        skCityP.setText ( ssList.get ( i ).getSKdist () );
        skStateP.setText ( ssList.get ( i ).getSKstate () );
        skPinP.setText ( ssList.get ( i ).getSKpin () );

        String cat=ssList.get ( i ).getSKcat ();
        String[] parts = cat.split("\t");
        for (int j=0;j<parts.length;j++){
            if(parts[j].equals ( "SuperMarket" )){
                marketP.setChecked ( true );

            }
            else if(parts[j].equals ( "Medicals" )){
                medP.setChecked ( true );

            }
            else if(parts[j].equals ( "Dairy" )){
                dairyP.setChecked ( true );
            }
            else{
                storeP.setChecked ( true );
            }
        }

    }

    public void skEdit(View view) {
        skNameP.setEnabled ( true );
        skShopP.setEnabled ( true );
        skStateP.setEnabled ( true );
        skCityP.setEnabled ( true );
        skAddP.setEnabled ( true );
        skPinP.setEnabled ( true );
        medP.setEnabled ( true );
        marketP.setEnabled ( true );
        storeP.setEnabled ( true );
        dairyP.setEnabled ( true );


        ibSKEdit.setVisibility ( View.INVISIBLE );
    }

    public void skProfileSave(View view) {
        final String name=skNameP.getText ().toString ();
        final String shop=skShopP.getText ().toString ();
        final String phone=skPhoneP.getText ().toString ();
        final String add=skAddP.getText ().toString ();
        final String city=skCityP.getText ().toString ();
        final String state=skStateP.getText ().toString ();
        final String pin=skPinP.getText ().toString ();
        String category="";
        if(medP.isChecked ()){
            category=category.concat ( "Medicals\t" );
        }
        if(marketP.isChecked ()){
            category=category.concat ( "SuperMarket\t" );
        }
        if(storeP.isChecked ()){
            category=category.concat ( "Fruits and Vegetables Store\t" );
        }
        if(dairyP.isChecked ()){
            category=category.concat ( "Dairy" );
        }
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
        Query query=reference.child ( "ShopkeeperDetails" ).orderByChild ( "skphone" ).equalTo ( phone );
        final HashMap<String,Object> map=new HashMap<> (  );
        final String finalCategory = category;
        query.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren ()){
                    map.put ( "skname",name );
                    map.put ( "skshop",shop );
                    map.put ( "skphone",phone );
                    map.put ( "skstate",state );
                    map.put ( "skdist",city );
                    map.put ( "skstreet",add );
                    map.put ( "skpin",pin );
                    map.put ( "skcat", finalCategory );


                    data.getRef ().updateChildren ( map );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        Intent i =new Intent ( ShopkeeperProfile.this,ShopkeeperHome.class );
        i.putExtra ( "Phone",phone );
        startActivity ( i );

    }

    public void backSk(View view) {
        Intent intent =new Intent ( ShopkeeperProfile.this,ShopkeeperHome.class );
        intent.putExtra ( "Phone",ssList.get ( i ).getSKphone () );
        startActivity ( intent );
    }
}