package com.example.imaz.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText emailID,paswd;
    RadioGroup rg;
    String userType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        emailID=findViewById(R.id.emailIdRegister);
        paswd=findViewById(R.id.passwordRegister);
        rg=findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.adminOption)
                    userType="admin";
                else if(i==R.id.userOption)
                    userType="user";
            }
        });
    }
    public void register(View v)
    {
        final String email=emailID.getText().toString();
        String password=paswd.getText().toString();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userType))
        {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(RegisterActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                        FirebaseUser user=mAuth.getCurrentUser();
                        if(user!=null)
                        {
                            String uid=user.getUid();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference ref= FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl("https://dbdemo-d011c.firebaseio.com/");
                            DatabaseReference usrRef=ref.child("Users");
                            UserModel um= new UserModel(email,userType);
                            DatabaseReference child=usrRef.child(uid);
                            child.setValue(um);
                            switchActivity();
                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
            Toast.makeText(this, "Missing Fields!", Toast.LENGTH_SHORT).show();
    }
    public void switchActivity()
    {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
