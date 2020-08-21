package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ForgotPass extends Global{

    TextInputEditText et;
    String ph;
    RadioButton fCust,fSk;
    String Cat="";
    Integer c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_forgot_pass );
        et=findViewById ( R.id.Pphone );
        fCust=findViewById ( R.id.Fcust );
        fSk=findViewById ( R.id.Fsk );
        fCust.setChecked ( false );
        fSk.setChecked ( false );
    }

    public void sendcode(View view) {
        ph=  Objects.requireNonNull ( et.getText () ).toString ();
        c=-1;

        if(fCust.isChecked ()){
            Cat="c";
        }
        if(fSk.isChecked ()){
            Cat="sk";
        }

        if(ph.isEmpty ()){
            Toast.makeText ( ForgotPass.this,"Enter the Phone Number",Toast.LENGTH_SHORT ).show ();
        }
        else if(ph.length () !=10){
            Toast.makeText ( ForgotPass.this,"Enter Valid Phone Number",Toast.LENGTH_SHORT ).show ();
        }else if(Cat.equals ( "" )){
            Toast.makeText ( ForgotPass.this,"Please check one of the category.",Toast.LENGTH_SHORT ).show ();
        }
        else{
            ph="+91 "+ph;
            Toast.makeText ( ForgotPass.this,""+ssList.size (),Toast.LENGTH_SHORT ).show ();

            if(Cat.equals ( "c" )){
                for(int i=0;i<scList.size ();i++){
                    if(scList.get ( i ).getCphone ().equals ( ph )){
                        c=i;
                    }
                }
            }
            else {
                for(int i=0;i<ssList.size ();i++){
                    if(ssList.get ( i ).getSKphone ().equals ( ph )){
                        c=i;
                    }
                }
            }
            if(c==-1){
                Toast.makeText ( ForgotPass.this,"User not signed Up",Toast.LENGTH_SHORT ).show ();
                Intent intent= new Intent ( ForgotPass.this,MainActivity.class );
                startActivity ( intent );
            }
            else{
                Toast.makeText ( ForgotPass.this,""+c,Toast.LENGTH_SHORT ).show ();
                Intent intent= new Intent ( ForgotPass.this,PassVerif.class );
                intent.putExtra ( "Phone",ph );
                intent.putExtra ( "Cat",Cat );
                startActivity ( intent );
            }





        }
    }
}