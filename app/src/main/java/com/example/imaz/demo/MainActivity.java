package com.example.imaz.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference ref;
    StorageReference sr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://dbdemo-d011c.firebaseio.com/");
        mAuth=FirebaseAuth.getInstance();
        sr= FirebaseStorage.getInstance().getReference();
    }

    public void addEntry(View view)
    {
        Intent i=new Intent(getApplicationContext(),AddEntryActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logoutOption)
        {
            mAuth.signOut();
            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        else
            return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}
