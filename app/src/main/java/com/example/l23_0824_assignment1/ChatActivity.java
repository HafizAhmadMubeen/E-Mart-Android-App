package com.example.l23_0824_assignment1;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private ImageButton btnSend;
    private TextView tvChatTitle;

    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> messageList;

    private DatabaseReference chatRef;
    private String currentUserId;
    private String receiverId;
    private String chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get data passed via Intent
        receiverId = getIntent().getStringExtra("receiverId");
        String receiverName = getIntent().getStringExtra("receiverName");

        currentUserId = FirebaseAuth.getInstance().getUid();

        // chatId = always smaller ID first so both sides generate same chatId
        if (currentUserId.compareTo(receiverId) < 0) {
            chatId = currentUserId + "_" + receiverId;
        } else {
            chatId = receiverId + "_" + currentUserId;
        }

        // Init views
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        tvChatTitle = findViewById(R.id.tvChatTitle);

        tvChatTitle.setText("Chat with " + receiverName);

        // Setup RecyclerView
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messageList, currentUserId);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(chatAdapter);

        // Firebase reference
        chatRef = FirebaseDatabase.getInstance()
                .getReference("Chats")
                .child(chatId)
                .child("messages");

        loadMessages();

        btnSend.setOnClickListener(v -> sendMessage());

        // Back button
        findViewById(R.id.btnChatBack).setOnClickListener(v -> finish());
    }

    private void loadMessages() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ChatMessage msg = ds.getValue(ChatMessage.class);
                    if (msg != null) messageList.add(msg);
                }
                chatAdapter.notifyDataSetChanged();
                // Auto scroll to latest message
                if (messageList.size() > 0) {
                    rvMessages.scrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String text = etMessage.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Type a message first", Toast.LENGTH_SHORT).show();
            return;
        }

        String messageId = chatRef.push().getKey();
        ChatMessage message = new ChatMessage(
                currentUserId,
                receiverId,
                text,
                System.currentTimeMillis()
        );

        chatRef.child(messageId).setValue(message).addOnSuccessListener(aVoid -> {
            etMessage.setText(""); // clear input after send
        });
    }
}