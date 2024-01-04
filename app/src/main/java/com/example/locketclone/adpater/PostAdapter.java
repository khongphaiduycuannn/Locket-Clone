package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemPostBinding;
import com.example.locketclone.repository.PostRepository;
import com.example.locketclone.ui.history.HistoryViewModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

@SuppressLint("ClickableViewAccessibility")
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Fragment fragment;
    private ArrayList<String> listPosts = new ArrayList<>();

    private HistoryViewModel historyViewModel;

    private PostRepository postRepository = new PostRepository();

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
        int sz = getItemCount();
        if (sz == 0) return;
        postRepository.getPostById(listPosts.get(sz - position - 1), it -> {
            if (it.getData() != null) {
                String image = (String) it.getData().get("image");
                Glide.with(holder.binding.image.getContext())
                        .load(image).into(holder.binding.image);
            }
        });
        holder.binding.imageContainer.setOnTouchListener((view, motionEvent) ->
                onImageTouch(view, motionEvent, position)
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListPosts(ArrayList<String> listPosts) {
        this.listPosts.clear();
        this.listPosts.addAll(listPosts);
        notifyDataSetChanged();
    }

    private boolean onImageTouch(View view, MotionEvent motionEvent, int position) {
        ScaleAnimation zoomInAnimation = new ScaleAnimation(
                1f, 0.9f,
                1f, 0.9f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        zoomInAnimation.setDuration(150);
        zoomInAnimation.setFillAfter(true);

        ScaleAnimation zoomOutAnimation = new ScaleAnimation(
                0.9f, 1f,
                0.9f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        zoomOutAnimation.setDuration(150);
        zoomOutAnimation.setFillAfter(true);

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                view.startAnimation(zoomInAnimation);
                break;
            }
            case MotionEvent.ACTION_UP: {
                view.startAnimation(zoomOutAnimation);
                historyViewModel.flipStatus();
                historyViewModel.setCurrentPos(position);
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                view.startAnimation(zoomOutAnimation);
                break;
            }
        }
        return true;
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        private ItemPostBinding binding;

        public PostViewHolder(@NonNull View view) {
            super(view);
            binding = ItemPostBinding.bind(view);
        }
    }
}
