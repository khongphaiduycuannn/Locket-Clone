package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemFriendChooseBinding;

import java.util.ArrayList;

public class FriendChooseAdapter extends RecyclerView.Adapter<FriendChooseAdapter.FriendChooseViewHolder> {

    private ArrayList<String> listFriendChoose = new ArrayList<>();

    @NonNull
    @Override
    public FriendChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_choose, parent, false);
        return new FriendChooseViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listFriendChoose.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendChooseViewHolder holder, int position) {

    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFriendChooses(ArrayList<String> listFriendChoose) {
        this.listFriendChoose.clear();
        this.listFriendChoose.addAll(listFriendChoose);
        notifyDataSetChanged();
    }

    static class FriendChooseViewHolder extends RecyclerView.ViewHolder {
        private ItemFriendChooseBinding binding;

        public FriendChooseViewHolder(@NonNull View view) {
            super(view);
            binding = ItemFriendChooseBinding.bind(view);
        }
    }
}