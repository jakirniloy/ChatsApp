package com.example.chatsapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsapp.Models.Massage;
import com.example.chatsapp.R;
import com.example.chatsapp.databinding.ItemReciveBinding;
import com.example.chatsapp.databinding.ItemSendBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter{
    Context context;
    ArrayList<Massage>massages;
    final int ITEAM_SENT = 1;
    final int ITEAM_Receive = 2;
    String senderRoom;
    String recevierRoom;


    public MessagesAdapter(Context context, ArrayList<Massage>massages,  String senderRoom, String recevierRoom){
      this.context = context;
      this.massages = massages;
      this.senderRoom = senderRoom;
      this.recevierRoom=recevierRoom;
   }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder    (ViewGroup parent, int viewType) {

       if (viewType == ITEAM_SENT){
      View view = LayoutInflater.from(context).inflate(R.layout.item_send,parent,false);
      return new sentViewHolder(view);

       }
        else {
           View view = LayoutInflater.from(context).inflate(R.layout.item_recive,parent,false);
           return new reciveViewHolder(view);
       }
    }

    @Override
    public int getItemViewType(int position) {
        Massage massage = massages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(massage.getSenderId()))
        {
            return ITEAM_SENT;
        }else {
            return ITEAM_Receive;
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
 Massage massage = massages.get(position);
 int reactions []=new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();
        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if(holder.getClass() == sentViewHolder.class){
                sentViewHolder viewHolder = (sentViewHolder)holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }else {
                reciveViewHolder viewHolder = (reciveViewHolder) holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            massage.setFeelings(pos);
            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child("senderRoom")
                    .child("messages")
                    .child(massage.getMessageId())
                    .setValue(massage);
            FirebaseDatabase.getInstance().getReference().child("chats")
                    .child("receiverRoom")
                    .child("messages")
                    .child(massage.getMessageId())
                    .setValue(massage);
            return true;
        });

        if(holder.getClass() == sentViewHolder.class){
    sentViewHolder viewHolder = (sentViewHolder)holder;
    viewHolder.binding.message.setText(massage.getMessage());
    if (massage.getFeelings()>=0){
        //massage.setFeelings(reactions[(int)massage.getFeelings()]);
        viewHolder.binding.feeling.setImageResource(reactions[massage.getFeelings()]);
        viewHolder.binding.feeling.setVisibility(View.VISIBLE);
    }
    else {
        viewHolder.binding.feeling.setVisibility(View.GONE);
    }

viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
    @Override

    public boolean onTouch(View v, MotionEvent event) {
       popup.onTouch(v,event);
        return false;
    }
});




}else
        {
            reciveViewHolder viewHolder = (reciveViewHolder) holder;
            viewHolder.binding.message.setText(massage.getMessage());


            if (massage.getFeelings()>=0){
                viewHolder.binding.feeling.setImageResource(reactions[massage.getFeelings()]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }


            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v,event);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return massages.size();
    }

    public class sentViewHolder extends RecyclerView.ViewHolder{

ItemSendBinding binding;
        public sentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding =ItemSendBinding.bind(itemView);
        }
    }

    public class reciveViewHolder extends RecyclerView.ViewHolder{
   ItemReciveBinding binding;

        public reciveViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReciveBinding.bind(itemView);
        }
    }

}
