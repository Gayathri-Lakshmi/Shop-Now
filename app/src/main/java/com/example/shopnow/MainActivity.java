package com.example.shopnow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.shopnow.SIgnUp.CustomerUp;
import com.example.shopnow.SIgnUp.ShopkeeperUp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    RadioButton cust,sk;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        userLoggedIn();
        cust=findViewById ( R.id.cust );
        sk=findViewById ( R.id.sk );
        rg=findViewById ( R.id.radioGroup );
        cust.setChecked ( false );
        sk.setChecked ( false );
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
    }

    public void signIn(View view) {
        startActivity ( new Intent (this,SignIn.class  ) );
    }

    public void shopkeeper(View view) {
        startActivity ( new Intent (this, ShopkeeperUp.class  ) );


    }

    public void customer(View view) {
        startActivity ( new Intent (this, CustomerUp.class  ) );

    }
    public void userLoggedIn(){
        FirebaseUser user=FirebaseAuth.getInstance ().getCurrentUser ();
        if(user!=null){
            startActivity (new Intent ( getApplicationContext (),SignIn.class ) );
            finish ();
            return;
        }
    }
}