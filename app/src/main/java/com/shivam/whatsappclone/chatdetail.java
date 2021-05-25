package com.shivam.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shivam.whatsappclone.Adapters.ChatAdapter;
import com.shivam.whatsappclone.Models.MessageModel;
import com.shivam.whatsappclone.databinding.ActivityChatdetailBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatdetail extends AppCompatActivity {

    ActivityChatdetailBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatdetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

//        Recieve data
         final String senderId = auth.getUid();
        String reciverId = getIntent().getStringExtra("UserId");
        String profilePic = getIntent().getStringExtra("Profilepic");
        String userName = getIntent().getStringExtra("Username");

//        Set data
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.profileImage);

//        BackArrow used for move dashboard activity
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(chatdetail.this,dashboard.class));
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,this,reciverId);
        binding.chatRecycleView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatRecycleView.setLayoutManager(linearLayoutManager);


        final String senderRoom = senderId + reciverId;
        final String recieverRoom = reciverId + senderId;

//show message on list
        database.getReference().child("Chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                      use loop beacuse more message are send  & getchildren() use tabtak gabtak child are come
                        messageModels.clear(); // use for one message show at one time (message do not repeat)
                        for (DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            MessageModel model = snapshot1.getValue(MessageModel.class);
//                            get receiver Id :use for delete message
                            model.setMessageId(snapshot1.getKey());
                            messageModels.add(model);
                        }
                        chatAdapter.notifyDataSetChanged(); // recyclerview update at real time
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = binding.etmessage.getText().toString();
                Date date = new Date();
                final  MessageModel model = new MessageModel(senderId,message,date.getTime());
                model.setTimestamp(new Date().getTime()); // use for timestamping
                binding.etmessage.setText(""); // empty edit text message

//               On sender Message
                database.getReference().child("Chats")  // in fb create chat node
                        .child(senderRoom)   // in chat node create sender+reciever id
                        .push()   //in senderroom create new node id for each message
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

//                        On reciever message
                        database.getReference().child("Chats")
                        .child(recieverRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        });
    }
}