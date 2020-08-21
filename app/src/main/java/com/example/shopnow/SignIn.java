package com.example.shopnow;

import androidx.annotation.NonNull;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignIn extends Global {

    EditText sPh;
    DatabaseReference sReference;
    private ProgressBar sProgress;
    RadioButton sCust,sSk;
    RadioGroup rg;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_sign_in );
        sPh=findViewById ( R.id.sign_phone );
        sProgress=findViewById ( R.id.sProgress );
        linearLayout=findViewById ( R.id.signInLayout );
        rg=findViewById ( R.id.radio );
        ssList=new ArrayList<> (  );
        scList=new ArrayList<> (  );
        itemsList=new ArrayList<> (  );
        sizes=new ArrayList<> (  );
        sCust=findViewById ( R.id.Scust );
        sSk=findViewById ( R.id.Ssk );
        sCust.setChecked ( false );
        sSk.setChecked ( false );
        final String phoneNumber=sPh.getText ().toString ();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener ()
        {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id)
            {
                RadioButton checkedRadioButton = (RadioButton)rg.findViewById(id);

                boolean isChecked = checkedRadioButton.isChecked();

                if (isChecked)
                {
                    checkedRadioButton.setChecked(false);
                }
                else
                    checkedRadioButton.setChecked(true);
            }});
        sReference= FirebaseDatabase.getInstance ().getReference ();
        sReference.child ( "ShopkeeperDetails" ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ssList.clear ();
                    for (DataSnapshot data:dataSnapshot.getChildren ()){
                        Shopkeeper shopkeeper=data.getValue (Shopkeeper.class);
                        ssList.add ( shopkeeper );
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        sReference.child ( "CustomerDetails" ).addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    scList.clear ();
                    for (DataSnapshot data:dataSnapshot.getChildren ()){
                        Customer customer=data.getValue (Customer.class);
                        scList.add ( customer );
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        Runnable r = new Runnable() {
            @Override
            public void run(){
                sizes.clear ();
                for (int i=0;i<ssList.size ();i++){
                    final int finalI = i;
                    sReference.child ( "Items"+ssList.get ( i ).getSKphone () ).addValueEventListener ( new ValueEventListener () {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot data:dataSnapshot.getChildren ()){
                                    Items items=data.getValue (Items.class);
                                    itemsList.add ( items );

                                }
                            }
                            if(finalI==0){
                                if(itemsList.size ()==0){
                                    sizes.add ( 0 );
                                }
                                else {
                                    sizes.add ( itemsList.size () );
                                }
                            }
                            else {
                                if(itemsList.size ()==sizes.get ( finalI-1 )){
                                    sizes.add ( 0 );
                                }else {
                                    sizes.add ( itemsList.size () );
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                }

            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 5000);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText ( SignIn.this,""+sizes.size ()+itemsList.size (),Toast.LENGTH_SHORT ).show ();
        return;
    }

    public void forgot(View view) {
        sProgress.setVisibility ( View.VISIBLE );
        Runnable r = new Runnable() {
            @Override
            public void run() {
                sProgress.setVisibility ( View.INVISIBLE );
                Intent intent =new Intent ( SignIn.this,ForgotPass.class );
                startActivity ( intent );

            }
        };


        Handler h = new Handler();
        h.postDelayed(r, 5000);
    }


    public void logIn(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);

        String Phone = sPh.getText ().toString ();
        Phone="+91 "+Phone;
        String Cat="";
        if(sCust.isChecked ()){
            Cat="c";
        }
        if(sSk.isChecked ()){
            Cat="sk";
        }
        if(Cat==""){
            Toast.makeText ( SignIn.this,"Please check one of the category.",Toast.LENGTH_SHORT ).show ();
        }
        if(Phone.isEmpty () ){
            Toast.makeText ( SignIn.this,"Please fill the details.",Toast.LENGTH_SHORT ).show ();
            sProgress.setVisibility ( View.INVISIBLE );
        }
        else {
            sProgress.setVisibility ( View.VISIBLE );

            final String finalCat = Cat;
            final String finalPhone = Phone;
            Runnable r = new Runnable () {
                @Override
                public void run() {

                    int count=0;
                    if(finalCat.equals ( "c" )){
                        for(int  j= 0; j<scList.size (); j++){
                            if(finalPhone.equals ( scList.get ( j ).getCphone () )){
                                count++;
                                sProgress.setVisibility ( View.INVISIBLE );
                                Intent i =new Intent ( SignIn.this,PhoneVerification.class );
                                i.putExtra ( "Name","null" );
                                i.putExtra ( "Shop","null" );
                                i.putExtra ( "Pass", "null");
                                i.putExtra ( "Add","null" );
                                i.putExtra ( "Dist","null" );
                                i.putExtra ( "State","null" );
                                i.putExtra ( "zip","null" );
                                i.putExtra ( "Cat","null" );
                                i.putExtra ( "type","c" );
                                i.putExtra ( "Phone", finalPhone );
                                startActivity ( i );
                            }
                        }
                    }
                    if(finalCat.equals ( "sk" )){
                        for(int j = 0; j<ssList.size (); j++){
                            if(finalPhone.equals ( ssList.get ( j ).getSKphone () )){
                                count++;
                                    sProgress.setVisibility ( View.INVISIBLE );
                                    Intent i =new Intent ( SignIn.this,PhoneVerification.class );
                                i.putExtra ( "Name","null" );
                                i.putExtra ( "Shop","null" );
                                i.putExtra ( "Add","null" );
                                i.putExtra ( "Dist","null" );
                                i.putExtra ( "State","null" );
                                i.putExtra ( "zip","null" );
                                i.putExtra ( "Cat","null" );
                                i.putExtra ( "type","sk" );
                                    i.putExtra ( "Phone", finalPhone );
                                    startActivity ( i );

                                }
                            }

                        }



                    if(count ==0){
                        Toast.makeText ( getApplicationContext (),"The User not Signed Up",Toast.LENGTH_SHORT ).show ();
                        sProgress.setVisibility ( View.INVISIBLE );
                    }
                }


            };
            Handler h = new Handler ();
            h.postDelayed ( r, 10000 );

        }


    }

    public void SsignUp(View view) {
        Intent intent =new Intent ( SignIn.this,MainActivity.class );
        startActivity ( intent );
    }
}