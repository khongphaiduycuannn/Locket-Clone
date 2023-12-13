package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemFriendBinding;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private ArrayList<String> listFriend = new ArrayList<>();

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listFriend.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFriends(ArrayList<String> listFriend) {
        this.listFriend.clear();
        this.listFriend.addAll(listFriend);
        notifyDataSetChanged();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        private ItemFriendBinding binding;

        public FriendViewHolder(@NonNull View view) {
            super(view);
            binding = ItemFriendBinding.bind(view);
        }
    }
}
