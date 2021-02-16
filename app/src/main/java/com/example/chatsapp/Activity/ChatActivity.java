package com.example.chatsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.chatsapp.Adapter.MessagesAdapter;
import com.example.chatsapp.Models.Massage;
import com.example.chatsapp.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
ActivityChatBinding binding;
MessagesAdapter adapter;
ArrayList<Massage>massages;
String senderRoom,receiverRoom;
FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        massages = new ArrayList<>();
        adapter = new MessagesAdapter(this,massages,senderRoom,receiverRoom);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        String name = getIntent().getStringExtra("name");
        String receiveruid = getIntent().getStringExtra("uid");
        final String sendertuid = FirebaseAuth.getInstance().getUid();
        senderRoom = sendertuid+receiveruid;
        receiverRoom = receiveruid+sendertuid;
    database=FirebaseDatabase.getInstance();
    database.getReference().child("chats")
            .child(senderRoom)
            .child("messages")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   massages.clear();
                  for (DataSnapshot snapshot1: snapshot.getChildren())
                  {
                      Massage massage = snapshot1.getValue(Massage.class);
                      massage.setMessageId(snapshot1.getKey());
                      massages.add(massage);
                  }
                  adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

       binding.sendBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (binding.messageBox == null) {
                   binding.messageBox.setText("");
               }else
               {
                    String randomKey = database.getReference().push().getKey();
                   final String messageText = binding.messageBox.getText().toString();
                   final Date date = new Date();
                   final Massage massages = new Massage(messageText,sendertuid,date.getTime());
                   binding.messageBox.setText("");
                   database.getReference().child("chats")
                           .child(senderRoom)
                           .child("messages")
                           .child(randomKey)
                           .setValue(massages).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {

                           database.getReference().child("chats")
                                   .child(receiverRoom)
                                   .child("messages")
                                   .child(randomKey)
                                   .setValue(massages).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {

                               }
                           });
                       }
                   });



               }

           }
       });


        getSupportActionBar().setTitle(name);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}