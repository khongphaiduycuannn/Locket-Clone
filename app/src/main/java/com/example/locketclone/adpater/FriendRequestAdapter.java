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
import com.example.locketclone.repository.UserRepository;

import java.util.ArrayList;

public abstract class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder> {

    private ArrayList<String> listFriendRequest = new ArrayList<>();

    private UserRepository userRepository = new UserRepository();

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
        userRepository.getUserById(listFriendRequest.get(position), doc -> {
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

        holder.binding.btnAccept.setOnClickListener(view -> {
            onClickItem(listFriendRequest.get(position));
        });
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

    public abstract void onClickItem(String userRequestId);
}
