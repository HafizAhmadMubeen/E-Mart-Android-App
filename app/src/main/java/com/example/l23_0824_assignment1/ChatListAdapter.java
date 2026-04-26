package com.example.l23_0824_assignment1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context context;
    private ArrayList<String[]> buyerList; // [0]=buyerId, [1]=buyerName
    private String currentSellerId;

    public ChatListAdapter(Context context, ArrayList<String[]> buyerList, String currentSellerId) {
        this.context = context;
        this.buyerList = buyerList;
        this.currentSellerId = currentSellerId;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_chat_list, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        String[] buyer = buyerList.get(position);
        String buyerId = buyer[0];
        String buyerName = buyer[1];

        holder.tvBuyerName.setText(buyerName);
        holder.tvTapToChat.setText("Tap to open chat");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("receiverId", buyerId);
            intent.putExtra("receiverName", buyerName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return buyerList.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder {
        TextView tvBuyerName, tvTapToChat;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBuyerName = itemView.findViewById(R.id.tvBuyerName);
            tvTapToChat = itemView.findViewById(R.id.tvTapToChat);
        }
    }
}