package com.example.l23_0824_assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ChatListFragment extends Fragment {

    private RecyclerView rvChatList;
    private TextView tvNoChats;
    private String currentSellerId;

    public ChatListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        rvChatList = view.findViewById(R.id.rvChatList);
        tvNoChats = view.findViewById(R.id.tvNoChats);
        rvChatList.setLayoutManager(new LinearLayoutManager(getContext()));

        currentSellerId = FirebaseAuth.getInstance().getUid();

        loadChats();

        return view;
    }

    private void loadChats() {
        // Find all chats where sellerId is part of the chatId
        FirebaseDatabase.getInstance().getReference("Chats")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<String> buyerIds = new ArrayList<>();

                        for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                            String chatId = chatSnapshot.getKey();
                            // chatId format: smallerId_largerId
                            // so check if sellerId is part of chatId
                            if (chatId != null && chatId.contains(currentSellerId)) {
                                // Extract buyerId from chatId
                                String buyerId = chatId.replace(currentSellerId, "")
                                        .replace("_", "");
                                if (!buyerId.isEmpty()) {
                                    buyerIds.add(buyerId);
                                }
                            }
                        }

                        if (buyerIds.isEmpty()) {
                            tvNoChats.setVisibility(View.VISIBLE);
                            rvChatList.setVisibility(View.GONE);
                        } else {
                            tvNoChats.setVisibility(View.GONE);
                            rvChatList.setVisibility(View.VISIBLE);
                            loadBuyerNames(buyerIds);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void loadBuyerNames(ArrayList<String> buyerIds) {
        ArrayList<String[]> buyerList = new ArrayList<>(); // [0]=id, [1]=name

        for (String buyerId : buyerIds) {
            FirebaseDatabase.getInstance().getReference("Users")
                    .child(buyerId)
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        String name = "Buyer";
                        if (snapshot.exists()) {
                            User user = snapshot.getValue(User.class);
                            if (user != null && user.fullName != null) {
                                name = user.fullName;
                            }
                        }
                        buyerList.add(new String[]{buyerId, name});

                        // When all buyers loaded, set adapter
                        if (buyerList.size() == buyerIds.size()) {
                            ChatListAdapter adapter = new ChatListAdapter(
                                    getContext(), buyerList, currentSellerId);
                            rvChatList.setAdapter(adapter);
                        }
                    });
        }
    }
}