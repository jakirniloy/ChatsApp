package com.example.chatsapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.chatsapp.databinding.ActivitySetupProfileBinding;
import com.example.chatsapp.Models.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SetupProfile extends AppCompatActivity {
  ActivitySetupProfileBinding binding;
  FirebaseAuth auth;
  FirebaseStorage Storage;
  FirebaseDatabase Data;
  Uri selectedImage;
  ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth=FirebaseAuth.getInstance();
        Storage=FirebaseStorage.getInstance();
        Data = FirebaseDatabase.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploding profile.......");
        dialog.setCancelable(false);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        binding.profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,50);

            }
        });
        binding.setupbtn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                dialog.show();
                String name = binding.name.getText().toString();
                if(name.isEmpty()){
                    binding.name.setError("Please type a name");
                    return;
                }
                if(selectedImage !=null){
                    final StorageReference reference = Storage.getReference().child("Profiles").child(auth.getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                   String image = uri.toString();
                                   String uid = auth.getUid();
                                   String phone = auth.getCurrentUser().getPhoneNumber();
                                   String name = binding.name.getText().toString();
                                   user User= new user(uid,name,phone,image); //this class use for data collecet and pass data to firebase
                                   Data.getReference() //for data pass in firebase

                                           .child("users")
                                           .child(uid)
                                           .setValue(User)
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   dialog.dismiss();
                                              Intent inten = new Intent(SetupProfile.this,MainActivity.class) ;
                                              startActivity(inten);
                                              finish();
                                               }
                                           });

                                    }
                                });
                            }

                        }
                    });
                }

                else
                {
                    String uid = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();
                    user User= new user(uid,name,phone,"No Image"); //this class use for data collecet and pass data to firebase
                    Data.getReference() //for data pass in firebase

                            .child("users")
                            .child(uid)
                            .setValue(User)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Intent inten = new Intent(SetupProfile.this,MainActivity.class) ;
                                    startActivity(inten);
                                    finish();
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data !=null){
            if(data.getData() != null){
                binding.profileimage.setImageURI(data.getData());
                selectedImage=data.getData();
            }
        }
    }
}