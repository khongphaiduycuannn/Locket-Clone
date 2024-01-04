package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemFriendBinding;
import com.example.locketclone.repository.UserRepository;

import java.util.ArrayList;

public abstract class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder> {

    private ArrayList<String> listFriend = new ArrayList<>();

    private UserRepository userRepository = new UserRepository();

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
        userRepository.getUserById(listFriend.get(position), doc -> {
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

        holder.binding.btnDeleteFriend.setOnClickListener(view -> {
            onClickItem(listFriend.get(position));
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListFriends(ArrayList<String> listFriend) {
        this.listFriend.clear();
        this.listFriend.addAll(listFriend);
        notifyDataSetChanged();
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        public ItemFriendBinding binding;

        public FriendViewHolder(@NonNull View view) {
            super(view);
            binding = ItemFriendBinding.bind(view);
        }
    }

    public abstract void onClickItem(String userRemoveId);
}
