package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity {


    private EditText codeEt;
    private String verId;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    String phNo,pName,pShop,pAdd,pDist,pState,pPin,pCat;
    String code;
    String type;
    String id;
    int c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_phone_verification );
        progressBar=findViewById ( R.id.progressbar );
        firebaseAuth=FirebaseAuth.getInstance ();
        c=0;
        databaseReference=  FirebaseDatabase.getInstance ().getReference ();
        codeEt = findViewById ( R.id.editTextCode );
        phNo=getIntent ().getStringExtra ( "Phone" );
        pName=getIntent ().getStringExtra ( "Name" );
        pShop=getIntent ().getStringExtra ( "Shop" );
        pAdd=getIntent ().getStringExtra ( "Add" );
        pDist=getIntent ().getStringExtra ( "Dist" );
        pState=getIntent ().getStringExtra ( "State" );
        pPin=getIntent ().getStringExtra ( "zip" );
        pCat=getIntent ().getStringExtra ( "Cat" );
        type=getIntent ().getStringExtra ( "type" );
        Toast.makeText ( PhoneVerification.this,""+phNo,Toast.LENGTH_SHORT ).show ();
        sendVerificationCode ( phNo );


    }

    private  void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance ().verifyPhoneNumber ( number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD,mCallBack );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent ( s, forceResendingToken );
            verId=s;
        }
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            code= phoneAuthCredential.getSmsCode ();
            if(code!=null){
                progressBar.setVisibility ( View.VISIBLE );
                verify ( code );

            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText ( PhoneVerification.this,e.getMessage (),Toast.LENGTH_SHORT  ).show ();

        }


    };

    public void verified(View view) {
        String enCode=codeEt.getText ().toString ();
        if(enCode.isEmpty ()){
            Toast.makeText ( PhoneVerification.this,"Enter the code",Toast.LENGTH_SHORT  ).show ();
            return;

        }
        progressBar.setVisibility ( View.VISIBLE );
        verify ( enCode );

    }
    private void verify(String code){
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential ( verId,code );
        firebaseAuth.signInWithCredential ( phoneAuthCredential ).addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                id= firebaseAuth.getUid ();
                if(task.isSuccessful ()){
                    if(!pName.equals ( "null" )) {
                        if (type.equals ( "c" )) {
                            Customer customer = new Customer ( pName, phNo, pAdd, pDist, pState, pPin, id );
                            databaseReference.child ( "CustomerDetails" ).child ( phNo ).setValue ( customer );

                        } else {
                            Shopkeeper sk = new Shopkeeper ( pName, pShop, phNo, pAdd, pDist, pState, pPin, pCat, id
                            );
                            databaseReference.child ( "ShopkeeperDetails" ).child ( phNo ).setValue ( sk );

                        }
                    }
                    c=c+1;
                    Toast.makeText ( PhoneVerification.this,"Code checked successfully.Please Sign In.",Toast.LENGTH_SHORT  ).show ();


                }
                else {
                     Toast.makeText ( PhoneVerification.this,task.getException ().getMessage (),Toast.LENGTH_SHORT  ).show ();
                }



                }

        } );

    }

    public void signIn(View view) {
        if(c==0){
            Toast.makeText ( PhoneVerification.this,"Please check the code",Toast.LENGTH_SHORT  ).show ();
        }
        else {
            if(type.equals ( "c" )){
                Intent i =new Intent ( PhoneVerification.this,LogoPage.class );
                i.putExtra ( "Phone",phNo );
                i.putExtra ( "type","c" );
                startActivity ( i );

            }
            else {
                Intent i =new Intent ( PhoneVerification.this,LogoPage.class );
                i.putExtra ( "type","sk" );
                i.putExtra ( "Phone",phNo );
                startActivity ( i );

            }


        }
    }
}
