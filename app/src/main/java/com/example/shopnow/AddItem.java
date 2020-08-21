package com.example.shopnow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddItem extends Global {

    EditText iNameEt,iPriceEt,iQtyEt;
    ImageView sIv;
    Uri uri;
    double db=0;
    String ph;
    int success;
    ArrayList<Items> itemsArrayList;
    List<Integer> indices;
    String filepath;
    Integer c;
    FirebaseStorage storage;
    StorageReference reference;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    public static final int CAMERA_REQUEST_CODE = 22;
    public static final int GALLERY_REQUEST_CODE = 33;
    int d;
    int start;
    int end;
    int index,there=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        c=0;
        success=0;
        itemsArrayList=new ArrayList<> (  );
        indices=new ArrayList<Integer> (  );
        setContentView ( R.layout.activity_add_item );
        iNameEt=findViewById ( R.id.iname );
        iPriceEt=findViewById ( R.id.iprice );
        iQtyEt=findViewById ( R.id.iqty );
        sIv=findViewById ( R.id.image );

        storage=FirebaseStorage.getInstance ();
        reference=storage.getReference ();

        ph=getIntent ().getStringExtra ( "Ph" );
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        for(int i=0;i<ssList.size ();i++){
            if(ssList.get ( i ).getSKphone ().equals ( ph )){
                c=i;
                index=i;
            }
        }
        if(c==0){
            if(sizes.get ( c )!=0) {
                end = sizes.get ( 0 );
                start = 0;
            }

        }else{
            if(sizes.get ( c )!=0) {
                end= sizes.get ( c );
                d=c-1;
                while(sizes.get ( d )==0 & d>0 ){
                    d=d-1;
                }

                start= sizes.get ( d );
            }
        }
        for(int i=start;i<end;i++){
            indices.add(i);
        }
        for(int i:indices){
            itemsArrayList.add ( itemsList.get ( i ) );
        }
    }

    public void saveItem(View view) {
        final String iName=iNameEt.getText ().toString ();
        final String iPrice=iPriceEt.getText ().toString ()+"/"+iQtyEt.getText ().toString ();

        there=0;
        if(c==0){
          Toast.makeText ( AddItem.this,"Please save the image first",Toast.LENGTH_SHORT ).show ();
        }
        else {
            if(itemsArrayList.size ()!=0){
                for (int j=0;j<itemsArrayList.size ();j++){
                    if(iName.equals ( itemsArrayList.get ( j ).getiName () )){
                        there=1;
                    }
                }
            }
            if(there==0) {

                Items items = new Items ( iName, iPrice, filepath );
                databaseReference.child ( "Items" + ph ).child ( iName ).setValue ( items );
                Toast.makeText ( AddItem.this, "Data saved", Toast.LENGTH_SHORT ).show ();
                Intent intent = new Intent ( AddItem.this, ShopkeeperHome.class );
                intent.putExtra ( "Phone", ph );
                startActivity ( intent );
            }
            else {
                Toast.makeText ( AddItem.this,"Item already exists in the list",Toast.LENGTH_SHORT ).show ();
            }

        }
    }

    public void ok(View view) {
        final String iName=iNameEt.getText ().toString ();
        final String iPrice=iPriceEt.getText ().toString ()+"/"+iQtyEt.getText ().toString ();

        if(iName.isEmpty () || iPriceEt.getText ().toString ().isEmpty () || iQtyEt.getText ().toString ().isEmpty () ){
            Toast.makeText ( AddItem.this,"Please fill the details",Toast.LENGTH_SHORT ).show ();
            return;
        }
        else {
            c++;
        uploadImage (iName,iPrice );}
    }

    private String getExtension(Uri u){
        ContentResolver cr=getContentResolver ();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton ();
        return mimeTypeMap.getExtensionFromMimeType ( cr.getType ( u ) );
    }

    private void uploadImage(final String nm, final String ph) {
        success=0;
        if(uri!=null){
            final ProgressDialog pd=new ProgressDialog (this);
            pd.setMessage ( "Uploading Image.." );
            pd.setProgressStyle ( ProgressDialog.STYLE_HORIZONTAL );
            pd.setMax ( 100 );
            pd.setCancelable ( false );
            pd.setProgress ( (int)db );
            pd.show ();
            reference = reference.child("Images/"+UUID.randomUUID().toString());

            reference.putFile ( uri ).addOnSuccessListener ( new OnSuccessListener<UploadTask.TaskSnapshot> () {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText ( AddItem.this,"Successfully uploaded",Toast.LENGTH_SHORT ).show ();
                    reference.getDownloadUrl ().addOnSuccessListener ( new OnSuccessListener<Uri> () {
                        @Override
                        public void onSuccess(Uri uri) {
                            filepath=uri.toString ();
                        }
                    } );
                    pd.dismiss ();

                }
            } ).addOnFailureListener ( new OnFailureListener () {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText ( AddItem.this,e.getMessage (),Toast.LENGTH_SHORT ).show ();
                    pd.dismiss ();

                }
            } ).addOnProgressListener ( new OnProgressListener<UploadTask.TaskSnapshot> () {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    db=(100*taskSnapshot.getBytesTransferred ()/taskSnapshot.getTotalByteCount ());
                    pd.setProgress ( (int)db );

                }
            } );




        }


    }

    public void camera(View view) {
        Intent i= new Intent ( MediaStore.ACTION_IMAGE_CAPTURE );

        if (i.resolveActivity(getPackageManager()) != null) {
            startActivityForResult ( i,CAMERA_REQUEST_CODE );
        }
    }

    public void gallery(View view) {
        Intent i =new Intent (  );
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,GALLERY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult ( requestCode, resultCode, data );
        if(requestCode==CAMERA_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Bitmap bitmap =(Bitmap)data.getExtras().get("data" );

                uri=getImageUri(AddItem.this,bitmap);
                sIv.setImageURI ( uri );

            }
        }
        else if(requestCode==GALLERY_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                uri=data.getData ();
                sIv.setImageURI ( uri );

            }
        }
    }

    private Uri getImageUri(AddItem addItem, Bitmap bitmap) {
        ByteArrayOutputStream bytes=new ByteArrayOutputStream (  );
        bitmap.compress ( Bitmap.CompressFormat.JPEG,100,bytes );
        String path = MediaStore.Images.Media.insertImage ( addItem.getContentResolver ()
                ,bitmap,"Title",null);
        return Uri.parse ( path );


    }


    public void cancelAdd(View view) {
        Intent intent= new Intent ( AddItem.this,ShopkeeperHome.class );
        intent.putExtra ( "Phone",ph );
        startActivity ( intent );
    }
}