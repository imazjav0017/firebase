package com.example.imaz.demo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput,passwordInput;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput=findViewById(R.id.emailIdLogin);
        passwordInput=findViewById(R.id.passwordLogin);
        mAuth=FirebaseAuth.getInstance();
    }
    public void login(View v)
    {
        Log.i("LoginButton","clicked");
        String email=emailInput.getText().toString();
        String password=passwordInput.getText().toString();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                      switchActivity();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
            Toast.makeText(this, "Missing Fields", Toast.LENGTH_SHORT).show();

    }
    public void register(View v)
    {
        Log.i("RegisterButton","clicked");
        Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null)
            switchActivity();
    }
    void switchActivity()
    {
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
