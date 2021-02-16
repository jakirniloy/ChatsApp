package com.example.chatsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatsapp.Activity.ChatActivity;
import com.example.chatsapp.Models.user;
import com.example.chatsapp.R;
import com.example.chatsapp.databinding.RowConversationBinding;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder>{
       Context context;
       ArrayList<user>Users;

    public UsersAdapter(Context context, ArrayList<user> users) {
        this.context = context;
        this.Users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(context).inflate(R.layout.row_conversation,parent,false);


        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
       final user users= Users.get(position);
      holder.binding.chatname.setText(users.getName());
        Glide.with(context).load(users.getProfileImage())
                .placeholder(R.drawable.avatar)
                .into(holder.binding.chatprofile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name",users.getName());
                intent.putExtra("uid",users.getUid());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{
RowConversationBinding binding;
        public UserViewHolder(@NonNull View itemView) {

            super(itemView);
            binding = RowConversationBinding.bind(itemView);
        }
    }
}
