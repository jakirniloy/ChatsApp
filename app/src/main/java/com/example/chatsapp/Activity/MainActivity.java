package com.example.chatsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chatsapp.R;
import com.example.chatsapp.Adapter.UsersAdapter;
import com.example.chatsapp.databinding.ActivityMainBinding;
import com.example.chatsapp.Models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  ActivityMainBinding binding;
  FirebaseDatabase database;
  ArrayList<user>Users;
  UsersAdapter usersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        Users = new ArrayList<>();
        usersAdapter = new UsersAdapter(this,Users);
        binding.recylerview.setAdapter(usersAdapter);
      database.getReference().child("users").addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
             Users.clear();
             for(DataSnapshot snapshot1 : snapshot.getChildren()){
                user User = snapshot1.getValue(user.class);
                Users.add(User);
             }
             usersAdapter.notifyDataSetChanged();
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.serch:
                Toast.makeText(this,"Search Clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"Settings Clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.group:
                Toast.makeText(this,"Group Clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.calls:
                Toast.makeText(this,"Calls Clicked",Toast.LENGTH_LONG).show();
                break;
            case R.id.invite:
                Toast.makeText(this,"Invite Clicked",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);

    }
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}