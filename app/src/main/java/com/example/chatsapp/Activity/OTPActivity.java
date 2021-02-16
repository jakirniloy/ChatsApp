package com.example.chatsapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.chatsapp.databinding.ActivityOTPBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mukesh.OnOtpCompletionListener;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
ActivityOTPBinding binding;
FirebaseAuth auth;
String verification;
ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOTPBinding.inflate(getLayoutInflater());
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
       dialog= new ProgressDialog(this);
       dialog.setMessage("Sending OTP....");
       dialog.setCancelable(false);
       dialog.show();
        auth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        binding.otpView.requestFocus();
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        binding.phone.setText("Verify "+phoneNumber);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OTPActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                dialog.dismiss();
                                verification=s;
                            }
                        })        // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
       binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
           @Override
           public void onOtpCompleted(String otp) {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification,otp);
            auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      Intent intent = new Intent(OTPActivity.this, SetupProfile.class);
                      startActivity(intent);
                     finishAffinity(); //ager sob activity finished kore dibe
                  }
                  else{
                      Toast.makeText(OTPActivity.this,"Failed",Toast.LENGTH_LONG).show();
                  }
                }
            });
           }
       });
    }
}