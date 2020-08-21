package com.example.shopnow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddToCart extends Global {

    TextView cNameTv,cPriceTv;
    EditText cQtyEt,cTotalEt;
    String cName,cPrice;
    int ind;
    LinearLayout linearLayout;
    int c;
    Double qty,pri;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    int CInd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        setContentView ( R.layout.activity_add_to_cart );
        cNameTv=findViewById ( R.id.cItemName );
        cPriceTv=findViewById ( R.id.cItemPrice );
        cQtyEt=findViewById ( R.id.cQty );
        linearLayout=findViewById ( R.id.cartLayout );
        cTotalEt=findViewById ( R.id.cTotal );
        cName=getIntent ().getStringExtra ( "iName" );
        cPrice=getIntent ().getStringExtra ( "iPrice" );
        ind=getIntent ().getIntExtra ( "index",0 );
        CInd=getIntent ().getIntExtra ( "Cindex",0 );
        cNameTv.setText ( cName );
        c=0;
        cPriceTv.setText ( cPrice );
        String[] p=cPrice.split ( "/" );
        pri=Double.parseDouble ( p[0] );
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();


    }
    @Override
    public void onBackPressed() {
        // Add the Back key handler here.
        return;
    }


    public void cancel(View view) {
        Intent i =new Intent ( AddToCart.this,CustomerItems.class );
        i.putExtra ( "CIndex",CInd );
        i.putExtra ( "Name",ssList.get ( ind ).getSKshop () );
        startActivity ( i );
    }

    public void calculate(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);

        if(cQtyEt.getText ().toString ().isEmpty ()){
            Toast.makeText ( AddToCart.this,"Please type the quantity you require",Toast.LENGTH_SHORT ).show ();
        }
        else {
            qty=Double.parseDouble (cQtyEt.getText ().toString ());

            String total=String.valueOf ( qty*pri );
            cTotalEt.setText ( total );
            c=c+1;
        }

    }

    public void addToCart(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService( Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);


        if(c==0 || cQtyEt.getText ().toString ().isEmpty ()){
            Toast.makeText ( AddToCart.this,"Please fill details properly",Toast.LENGTH_SHORT ).show ();
        }
        else {
            qty=Double.parseDouble (cQtyEt.getText ().toString ());
            cTotalEt.setText ( String.valueOf ( qty*pri ) );
            String loc = ssList.get ( ind ).SKstreet+","+ssList.get ( ind ).SKdist+","+ssList.get ( ind ).SKstate+", India -"+ssList.get ( ind ).SKpin;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date ());
            CCartItem cCartItem =new CCartItem ( cName,cPrice,cTotalEt.getText ().toString (),ssList.get ( ind ).SKshop,ssList.get ( ind ).SKphone,loc,String.valueOf ( qty ),
                    "0", scList.get ( CInd ).getCphone (),currentDateandTime);
            databaseReference.child ( "Cart" +scList.get ( CInd ).getCphone ()  ).child ( ssList.get ( ind ).getSKshop () ).child ( cName ).setValue ( cCartItem );
            databaseReference.child ( "Orders" +ssList.get ( ind ).getSKphone ()  ).child ( scList.get ( CInd ).getCphone () ).child ( cName ).setValue ( cCartItem );
            Toast.makeText (AddToCart.this,"Item added to the cart",Toast.LENGTH_SHORT ).show ();
            Intent i =new Intent ( AddToCart.this,CustomerCart.class );
            i.putExtra ( "CInd",CInd );
            startActivity ( i );

        }


    }
}