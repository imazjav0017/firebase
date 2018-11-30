package com.example.imaz.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference ref;
    StorageReference sr;
    RecyclerView shoesRv;
    List<ShoeModel>shoeList;
    ShoeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref= FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://dbdemo-d011c.firebaseio.com/");
        mAuth=FirebaseAuth.getInstance();
        sr= FirebaseStorage.getInstance().getReference();
        shoesRv=findViewById(R.id.shoesList);
        shoeList=new ArrayList<>();
        adapter=new ShoeAdapter(shoeList);
        LinearLayoutManager lm=new LinearLayoutManager(getApplicationContext());
        shoesRv.setAdapter(adapter);
        shoesRv.setLayoutManager(lm);
        shoesRv.setHasFixedSize(true);
        getShoes();
    }

    public void getShoes()
    {
        ref.child("Shoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shoeList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    ShoeModel sm=ds.getValue(ShoeModel.class);
                    shoeList.add(sm);
                }
                adapter.notifyDataSetChanged();
                Log.i("shoe",String.valueOf(shoeList.size()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
