package com.example.l23_0824_assignment1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private ArrayList<ChatMessage> messageList;
    private String currentUserId;

    public ChatAdapter(Context context, ArrayList<ChatMessage> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messageList.get(position);
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date(message.getTimestamp()));

        if (holder instanceof SentViewHolder) {
            SentViewHolder sentHolder = (SentViewHolder) holder;
            sentHolder.tvMessage.setText(message.getMessage());
            sentHolder.tvTime.setText(time);
        } else if (holder instanceof ReceivedViewHolder) {
            ReceivedViewHolder receivedHolder = (ReceivedViewHolder) holder;
            receivedHolder.tvMessage.setText(message.getMessage());
            receivedHolder.tvTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // Sent message ViewHolder
    public static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvSentMessage);
            tvTime = itemView.findViewById(R.id.tvSentTime);
        }
    }

    // Received message ViewHolder
    public static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage, tvTime;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvReceivedMessage);
            tvTime = itemView.findViewById(R.id.tvReceivedTime);
        }
    }
}