package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemPostBinding;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<String> listPosts = new ArrayList<>();

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.binding.image.setImageDrawable(
            AppCompatResources.getDrawable(
                    holder.binding.image.getContext(),
                    R.drawable.image
            )
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListPosts(ArrayList<String> listPosts) {
        this.listPosts.clear();
        this.listPosts.addAll(listPosts);
        notifyDataSetChanged();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;

        public PostViewHolder(@NonNull View view) {
            super(view);
            binding = ItemPostBinding.bind(view);
        }
    }
}
