package com.example.imaz.demo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class AddEntryActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference ref;
    StorageReference sr;
    EditText shoeId,shoeCost,shoeName;
    String sId,cost,name,url;
    ImageView img;
    Uri pictureUri=null;
    boolean containsPic=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://dbdemo-d011c.firebaseio.com/");
        mAuth=FirebaseAuth.getInstance();
        sr= FirebaseStorage.getInstance().getReference();
        shoeId=findViewById(R.id.shoeIdInput);
        shoeCost=findViewById(R.id.shoeCostInput);
        shoeName=findViewById(R.id.shoeNameInput);
        img=findViewById(R.id.img);
    }

    public void save(View v)
    {
        sId=shoeId.getText().toString();
        cost=shoeCost.getText().toString();
        name=shoeName.getText().toString();
        if(!TextUtils.isEmpty(sId) && !TextUtils.isEmpty(cost) && !TextUtils.isEmpty(name) &&!containsPic)
        {
            DatabaseReference child=ref.child("Shoes");
            ShoeModel sm=new ShoeModel(name,sId,Integer.parseInt(cost));
            DatabaseReference child2=child.child(sm.getId());
            child2.setValue(sm);
        }
        else
        {
            saveWithPicture();
        }
    }
    public void saveWithPicture()
    {

        sId=shoeId.getText().toString();
        cost=shoeCost.getText().toString();
        name=shoeName.getText().toString();
        if(!TextUtils.isEmpty(sId) && !TextUtils.isEmpty(cost) && !TextUtils.isEmpty(name) &&containsPic) {
            if (pictureUri != null) {
                Log.i("step","1");
                DatabaseReference child=ref.child("Shoes");
                ShoeModel sm=new ShoeModel(name,sId,Integer.parseInt(cost));
                DatabaseReference child2=child.child(sm.getId());
                child2.setValue(sm).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //file storage
                        final StorageReference filePath = sr.child(sId).child(name);
                        filePath.putFile(pictureUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();


                                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {
                                        ref.child("Shoes").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    if (ds.getKey().equals(sId)) {
                                                        DatabaseReference updateRef = ref.child("Shoes").child(ds.getKey()).child("imageUrl");
                                                        updateRef.setValue(uri.toString());

                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }
                });

            }
        }
    }
    public void addPhoto(View v)
    {
        if(checkPermission()) {
            AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
            pictureDialog.setTitle("Select Action");
            String[] pictureDialogItems = {
                    "Select photo from gallery",
                    "Capture photo from camera"};
            pictureDialog.setItems(pictureDialogItems,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    choosePhotoFromGallary();
                                    break;
                                case 1:
                                    takePhotoFromCamera();
                                    break;
                            }
                        }
                    });
            pictureDialog.show();
        }
    }
    public void choosePhotoFromGallary() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 1);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }
    public boolean checkPermission()
    {
        if(Build.VERSION.SDK_INT<23)
        {
            return true;
        }
        else
        {
            if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
            else
                return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                addPhoto(null);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                containsPic=true;
                Uri uri=data.getData();
                pictureUri=uri;
                Picasso.with(getApplicationContext()).load(uri).into(img);
            }

        }
        else if(requestCode==2)
        {
            containsPic=true;
            Bitmap bm=(Bitmap)data.getExtras().get("data");
            //img.setImageBitmap(bm);
            Uri tempUri = getImageUri(getApplicationContext(), bm);
            Picasso.with(getApplicationContext()).load(tempUri).into(img);
            pictureUri=tempUri;


        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
