package com.shivam.whatsappclone.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.shivam.whatsappclone.Models.MessageModel;
import com.shivam.whatsappclone.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> list;
    Context context;
    String recId;

    int Sender_Viewtype = 1;
    int Reciever_Viewtype = 2;


    public ChatAdapter(ArrayList<MessageModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

//    for delete message
    public ChatAdapter(ArrayList<MessageModel> list, Context context, String recId) {
        this.list = list;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==Sender_Viewtype)
        {
            View sender = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(sender);
        }
        else{
            View reciever = LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
            return new RecieverViewHolder(reciever);
        }

    }

    @Override
    public int getItemViewType(int position) {

        if(list.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return Sender_Viewtype;
        }
        else {
            return Reciever_Viewtype;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final MessageModel messageModel = list.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are You Sure To Delete This Message")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid()+recId;
                                database.getReference().child("Chats").child(senderRoom).child(messageModel.getMessageId())
                                        .setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                }).show();
                return false;
            }
        });

        if(holder.getClass() == SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senderMessage.setText(messageModel.getMessage());

        }
        else{
            ((RecieverViewHolder)holder).recieverMessage.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class RecieverViewHolder extends RecyclerView.ViewHolder {

        TextView recieverMessage,recieverTime;
        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);

            recieverMessage = itemView.findViewById(R.id.recieverMessage);
            recieverTime = itemView.findViewById(R.id.reciverTime);
        }
    }

    public  class SenderViewHolder extends  RecyclerView.ViewHolder {

        TextView senderMessage,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessage = itemView.findViewById(R.id.senderMessage);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }


}
