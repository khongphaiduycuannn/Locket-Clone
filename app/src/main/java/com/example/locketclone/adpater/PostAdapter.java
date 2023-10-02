package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemPostBinding;
import com.example.locketclone.ui.history.HistoryViewModel;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Fragment fragment;
    private ArrayList<String> listPosts = new ArrayList<>();

    private HistoryViewModel historyViewModel;

    public PostAdapter(Fragment fragment) {
        this.fragment = fragment;
        historyViewModel = new ViewModelProvider(fragment).get(HistoryViewModel.class);
    }

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
        holder.binding.image.setOnClickListener(view -> {
            historyViewModel.flipStatus();
            historyViewModel.setCurrentPos(position);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListPosts(ArrayList<String> listPosts) {
        this.listPosts.clear();
        this.listPosts.addAll(listPosts);
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;

        public PostViewHolder(@NonNull View view) {
            super(view);
            binding = ItemPostBinding.bind(view);
        }
    }
}
