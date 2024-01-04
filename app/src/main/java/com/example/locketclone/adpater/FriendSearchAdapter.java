package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemFriendRequestBinding;
import com.example.locketclone.databinding.ItemFriendSearchBinding;
import com.example.locketclone.repository.UserRepository;

import java.util.ArrayList;

public abstract class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.FriendSearchViewHolder> {

    private ArrayList<String> listFriendSearch = new ArrayList<>();

    private UserRepository userRepository = new UserRepository();

    @NonNull
    @Override
    public FriendSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_search, parent, false);
        return new FriendSearchViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listFriendSearch.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FriendSearchViewHolder holder, int position) {
        userRepository.getUserById(listFriendSearch.get(position), doc -> {
            if (doc.getData() == null)
                return;

            String firstName = (String) doc.getData().get("firstName");
            String lastName = (String) doc.getData().get("lastName");

            String username;
            if (firstName == null)
                username = lastName;
            else if (lastName == null)
                username = firstName;
            else username = firstName + " " + lastName;

            String avatar = (String) doc.getData().get("avatar");
            if (avatar == null)
                avatar = "https://res.cloudinary.com/dh9ougddd/image/upload/v1693305148/m1nhqsruh3ydexbr0wqq.webp";
            holder.binding.txtUsername.setText(username);
            Glide.with(holder.binding.imgAvatar)
                    .load(avatar)
                    .into(holder.binding.imgAvatar);
        });

        holder.binding.btnAdd.setOnClickListener(view -> {
            onClickItem(listFriendSearch.get(position));
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFriendSearch(ArrayList<String> listFriendSearch) {
        this.listFriendSearch.clear();
        this.listFriendSearch.addAll(listFriendSearch);
        notifyDataSetChanged();
    }

    static class FriendSearchViewHolder extends RecyclerView.ViewHolder {
        private ItemFriendSearchBinding binding;

        public FriendSearchViewHolder(@NonNull View view) {
            super(view);
            binding = ItemFriendSearchBinding.bind(view);
        }
    }

    public abstract void onClickItem(String userSearchId);
}
