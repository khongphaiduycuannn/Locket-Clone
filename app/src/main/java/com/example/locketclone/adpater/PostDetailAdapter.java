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
import com.example.locketclone.databinding.ItemPostDetailBinding;
import com.example.locketclone.ui.history.HistoryViewModel;

import java.util.ArrayList;

public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.PostDetailViewHolder> {

    private Fragment fragment;
    private ArrayList<String> listPostsDetail = new ArrayList<>();

    private HistoryViewModel historyViewModel;

    public PostDetailAdapter(Fragment fragment) {
        this.fragment = fragment;
        historyViewModel = new ViewModelProvider(fragment).get(HistoryViewModel.class);
    }

    @NonNull
    @Override
    public PostDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_post_detail, parent, false);
        return new PostDetailViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listPostsDetail.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PostDetailViewHolder holder, int position) {
        holder.binding.imgPostImage.setImageDrawable(
                AppCompatResources.getDrawable(
                        holder.binding.imgPostImage.getContext(),
                        R.drawable.image
                )
        );
        holder.binding.imgPostContent.setText("One day");
        holder.binding.txtPostOwner.setText("Duy Quan");
        holder.binding.txtPostTimeCreated.setText(Integer.toString(position + 1) + "d");

        holder.binding.btnGalery.setOnClickListener(view -> {
            historyViewModel.flipStatus();
            historyViewModel.setCurrentPos(position);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListPosts(ArrayList<String> listPosts) {
        this.listPostsDetail.clear();
        this.listPostsDetail.addAll(listPosts);
        notifyDataSetChanged();
    }

    static class PostDetailViewHolder extends RecyclerView.ViewHolder {
        private ItemPostDetailBinding binding;

        public PostDetailViewHolder(@NonNull View view) {
            super(view);
            binding = ItemPostDetailBinding.bind(view);
        }
    }
}
