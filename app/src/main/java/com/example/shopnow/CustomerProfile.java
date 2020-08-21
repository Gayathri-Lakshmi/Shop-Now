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

import java.util.HashMap;

public class CustomerProfile extends Global {

    EditText cNameP,cPhoneP,cCityP,cStateP,cPinP,cAddP;
    ImageButton ibCEdit;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        i=getIntent ().getIntExtra ( "Index",0 );
        setContentView ( R.layout.activity_customer_profile );
        cNameP=findViewById ( R.id.cNameProfile);
        cPhoneP=findViewById ( R.id.cPhoneProfile);
        cAddP=findViewById ( R.id.cAddProfile );
        cCityP=findViewById ( R.id.cCityProfile );
        cStateP=findViewById ( R.id.cStateProfile );
        cPinP=findViewById ( R.id.cPinProfile );

        ibCEdit=findViewById ( R.id.img_cEdit );
        cNameP.setText ( scList.get ( i).getCname ()  );
        cPhoneP.setText ( scList.get ( i ).getCphone () );
        cAddP.setText ( scList.get ( i ).getCstreet () );
        cCityP.setText ( scList.get ( i ).getCdist () );
        cStateP.setText ( scList.get ( i ).getCstate () );
        cPinP.setText ( scList.get ( i ).getCpin () );

    }

    public void cEdit(View view) {
        cNameP.setEnabled ( true );
        cStateP.setEnabled ( true );
        cCityP.setEnabled ( true );
        cAddP.setEnabled ( true );
        cPinP.setEnabled ( true );
        ibCEdit.setVisibility ( View.INVISIBLE );
    }

    public void cProfileSave(View view) {
        final String name=cNameP.getText ().toString ();
        final String phone=cPhoneP.getText ().toString ();
        final String add=cAddP.getText ().toString ();
        final String city=cCityP.getText ().toString ();
        final String state=cStateP.getText ().toString ();
        final String pin=cPinP.getText ().toString ();
        DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
        Query query=reference.child ( "CustomerDetails" ).orderByChild ( "cphone" ).equalTo ( phone );
        final HashMap<String,Object> map=new HashMap<> (  );
        query.addListenerForSingleValueEvent ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren ()){
                    map.put ( "cname",name );
                    map.put ( "cphone",phone );
                    map.put ( "cstate",state );
                    map.put ( "cdist",city );
                    map.put ( "cstreet",add );
                    map.put ( "cpin",pin );


                    data.getRef ().updateChildren ( map );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        Intent i =new Intent ( CustomerProfile.this,CustomerHome.class );
        startActivity ( i );

    }
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.
        return;
    }



    public void back(View view) {
        Intent in =new Intent ( CustomerProfile.this,CustomerHome.class );
        in.putExtra ( "Phone",scList.get ( i ).getCphone () );
        startActivity ( in );
    }
}