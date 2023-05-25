package com.cookandroid.project_mobile.community;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.project_mobile.R;

public class CommunityDetailAdapter extends ListAdapter<CommunityDetailData, CommunityDetailAdapter.ViewHolder> {

    public CommunityDetailAdapter() {
        super(diffUtil);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommunityDetailData chatItem = getItem(position);
        holder.bind(chatItem);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView chatTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatTextView = itemView.findViewById(R.id.chatTextView);
        }

        public void bind(CommunityDetailData chatItem) {
            chatTextView.setText(chatItem.getMessage());
        }
    }

    private static DiffUtil.ItemCallback<CommunityDetailData> diffUtil = new DiffUtil.ItemCallback<CommunityDetailData>() {
        @Override
        public boolean areItemsTheSame(@NonNull CommunityDetailData oldItem, @NonNull CommunityDetailData newItem) {
            return oldItem.equals(newItem);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull CommunityDetailData oldItem, @NonNull CommunityDetailData newItem) {
            return oldItem.equals(newItem);
        }
    };
}