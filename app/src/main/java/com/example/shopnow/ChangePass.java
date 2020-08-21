package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChangePass extends AppCompatActivity {

    String type;
    String ph;
    EditText newPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_change_pass );
        type=getIntent ().getStringExtra ( "Cat" );
        ph=getIntent ().getStringExtra ( "Ph" );
        newPass=findViewById ( R.id.newPass );

    }
    public void onBackPressed() {
        // Add the Back key handler here.

        return;
    }


    public void change(View view) {
        final String nPass=newPass.getText ().toString ();
        if(nPass.isEmpty ()){
            Toast.makeText ( ChangePass.this,"Please enter the new password!",Toast.LENGTH_SHORT ).show ();
        }
        else {
            if(type.equals ( "c" )){
                DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
                Query query=reference.child ( "CustomerDetails" ).orderByChild ( "cphone" ).equalTo ( ph );
                final HashMap<String,Object> map=new HashMap<> (  );
                query.addListenerForSingleValueEvent ( new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren ()){
                            map.put ( "cpass",nPass );
                            data.getRef ().updateChildren ( map );
                        }
                        Toast.makeText ( ChangePass.this,"Password Changed Successfully",Toast.LENGTH_SHORT ).show ();
                        Intent intent=new Intent ( ChangePass.this,SignIn.class );
                        startActivity ( intent );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText ( ChangePass.this,"Password Change Cancelled",Toast.LENGTH_SHORT ).show ();

                    }
                } );
            }
            else {
                DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
                Query query=reference.child ( "ShopkeeperDetails" ).orderByChild ( "skphone" ).equalTo ( ph );
                final HashMap<String,Object> map=new HashMap<> (  );
                query.addListenerForSingleValueEvent ( new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren ()){
                            map.put ( "skpass",nPass );
                            data.getRef ().updateChildren ( map );
                        }
                        Toast.makeText ( ChangePass.this,"Password Changed Successfully",Toast.LENGTH_SHORT ).show ();
                        Intent intent=new Intent ( ChangePass.this,SignIn.class );
                        startActivity ( intent );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText ( ChangePass.this,"Password Change Cancelled",Toast.LENGTH_SHORT ).show ();

                    }
                } );

            }

        }


    }
}