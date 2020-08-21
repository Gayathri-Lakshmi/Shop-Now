package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PassVerif extends Global {

    private EditText PcodeEt;
    private String PVerId;
    private FirebaseAuth PfirebaseAuth;
    private ProgressBar PprogressBar;
    DatabaseReference PdatabaseReference;
    String PphNo;
    String Ptype;
    String Pcode;
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_pass_verif );
        PprogressBar=findViewById ( R.id.progressbar1);
        PfirebaseAuth=FirebaseAuth.getInstance ();
        PdatabaseReference=  FirebaseDatabase.getInstance ().getReference ();
        PcodeEt = findViewById ( R.id.editTextCode1 );
        PphNo=getIntent ().getStringExtra ( "Phone" );
        Ptype=getIntent ().getStringExtra ( "Cat" );
        c=0;

        sendVerifCode ( PphNo );
    }
    public void onBackPressed() {
        // Add the Back key handler here.

        return;
    }


    public void verified1(View view) {
        String code= PcodeEt.getText ().toString ();
        if(code.isEmpty ()){
            Toast.makeText ( PassVerif.this,"Please Enter the code.The code will expire in 2minutes from the time sent.",Toast.LENGTH_SHORT ).show ();
        }
        PprogressBar.setVisibility ( View.VISIBLE );
        verifiedCode ( code );
    }


    private void sendVerifCode(String phone){
        PhoneAuthProvider.getInstance ().verifyPhoneNumber (
                phone,120,TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                pCallBack
        );
    }

    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks pCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks () {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent ( s, forceResendingToken );
            PVerId=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
             Pcode=phoneAuthCredential.getSmsCode ();
             if(Pcode!= null){
                 PprogressBar.setVisibility ( View.VISIBLE );
                 verifiedCode ( Pcode );
             }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText ( PassVerif.this,e.getMessage ().toString (),Toast.LENGTH_SHORT ).show ();

        }
    };

    private void verifiedCode(String code){
        PhoneAuthCredential phoneAuthCredential =PhoneAuthProvider.getCredential ( PVerId,Pcode );
        signIn(phoneAuthCredential);
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential) {
        PfirebaseAuth.signInWithCredential ( phoneAuthCredential )
                .addOnCompleteListener ( new OnCompleteListener<AuthResult> () {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful ()){
                            c=c+1;
                            Toast.makeText ( PassVerif.this,"Code checked successfully.Please Chanage Password.",Toast.LENGTH_SHORT  ).show ();


                        }else {
                            Toast.makeText ( PassVerif.this,task.getException ().getMessage (),Toast.LENGTH_SHORT ).show ();
                        }
                    }
                } );
    }

    public void changePass(View view) {
        if(c==0){
            Toast.makeText ( PassVerif.this,"Please Check Code.",Toast.LENGTH_SHORT  ).show ();

        }else {
            Intent intent = new Intent ( PassVerif.this, ChangePass.class );
            intent.putExtra ( "Ph", PphNo );
            intent.putExtra ( "Cat", Ptype );
            startActivity ( intent );
        }

    }
}