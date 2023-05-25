package com.cookandroid.project_mobile.community;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.project_mobile.R;

import java.util.Objects;

public class CommunityAdapter extends ListAdapter<CommunityData, CommunityAdapter.ViewHolder> {

    private final OnItemClickedListener onItemClickedListener;

    public CommunityAdapter(OnItemClickedListener onItemClickedListener) {
        super(new DiffUtil.ItemCallback<CommunityData>() {
            @Override
            public boolean areItemsTheSame(@NonNull CommunityData oldItem, @NonNull CommunityData newItem) {
                return Objects.equals(oldItem, newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull CommunityData oldItem, @NonNull CommunityData newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public interface OnItemClickedListener{
        void onItemClicked(CommunityData item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final View itemView;

        public ViewHolder(View itemView){
            super(itemView);
            this.itemView = itemView;
        }

        public void bind(CommunityData item){
            TextView titleTextView = itemView.findViewById(R.id.communityTitle);
            ImageView imageView = itemView.findViewById(R.id.communityImg);

            titleTextView.setText(item.getText());

            if (!item.getUri().isEmpty()){
                Glide.with(imageView)
                        .load(item.getUri())
                        .into(imageView);
            }

            itemView.setOnClickListener(view -> onItemClickedListener.onItemClicked(item));
        }


    }
}
