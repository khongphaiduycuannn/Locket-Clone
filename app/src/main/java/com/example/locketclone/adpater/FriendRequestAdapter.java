package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemFriendRequestBinding;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private ArrayList<String> listFriendRequest = new ArrayList<>();

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_request, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listFriendRequest.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFriendRequests(ArrayList<String> listFriendRequest) {
        this.listFriendRequest.clear();
        this.listFriendRequest.addAll(listFriendRequest);
        notifyDataSetChanged();
    }

    static class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        private ItemFriendRequestBinding binding;

        public FriendRequestViewHolder(@NonNull View view) {
            super(view);
            binding = ItemFriendRequestBinding.bind(view);
        }
    }
}
