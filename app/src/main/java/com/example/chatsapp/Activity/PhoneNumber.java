package com.example.chatsapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatsapp.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PhoneNumber extends AppCompatActivity {
 ActivityPhoneNumberBinding binding;
 FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // content view image upload er jonn use kora hoche
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        auth = FirebaseAuth.getInstance();
           if(auth.getCurrentUser()!= null){
               Intent intent = new Intent(PhoneNumber.this, MainActivity.class);
               startActivity(intent);
           }
        binding.setupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.name.getText().toString();
                if (name.isEmpty()){
                    binding.name.setError("Please type a number");
                return;
                }
            else {
                    Intent intent = new Intent(PhoneNumber.this, OTPActivity.class);
                    intent.putExtra("phoneNumber", binding.name.getText().toString());
                    startActivity(intent);
                }
            }

        });

    }
}