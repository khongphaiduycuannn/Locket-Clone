package com.example.locketclone.adpater;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locketclone.R;
import com.example.locketclone.databinding.ItemPostDetailBinding;
import com.example.locketclone.repository.PostRepository;
import com.example.locketclone.repository.UserRepository;
import com.example.locketclone.ui.history.HistoryViewModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.PostDetailViewHolder> {

    private Fragment fragment;
    private ArrayList<String> listPostsDetail = new ArrayList<>();

    private HistoryViewModel historyViewModel;

    private PostRepository postRepository = new PostRepository();

    private UserRepository userRepository = new UserRepository();

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
        int sz = getItemCount();
        if (sz == 0) return;
        postRepository.getPostById(listPostsDetail.get(sz - position - 1), it -> {
            if (it.getData() != null) {
                String content = (String) it.getData().get("content");
                String image = (String) it.getData().get("image");
                String userId = (String) it.getData().get("userId");
                String postId = (String) it.getData().get("postId");
                Date createdAt = ((Timestamp) it.getData().get("createdAt")).toDate();

                Glide.with(holder.binding.imgPostImage.getContext())
                        .load(image).into(holder.binding.imgPostImage);
                holder.binding.imgPostContent.setText(content);

                int timeDay = (int) (new Date().getDay() - createdAt.getDay());
                int timeHours = (int) (new Date().getHours() - createdAt.getHours());
                int timeMinutes = (int) (new Date().getMinutes() - createdAt.getMinutes());
                if (timeDay < 1 && timeHours < 1) {
                    holder.binding.txtPostTimeCreated.setText(Integer.toString(timeMinutes) + "m");
                } else if (timeDay < 1) {
                    holder.binding.txtPostTimeCreated.setText(Integer.toString(timeHours) + "h");
                } else holder.binding.txtPostTimeCreated.setText(Integer.toString(timeDay) + "d");

                userRepository.getUserById(userId, doc -> {
                    if (doc.getData() != null) {
                        String username = doc.get("firstName") + " " + doc.get("lastName");
                        holder.binding.txtPostOwner.setText(username);
                    }
                });

                postRepository.getPostReaction(postId, it1 -> {
                    int[] cnt = {0, 0, 0, 0, 0};
                    Map<String, Object> data = it1.getData();
                    if (data != null && data.size() > 0) {
                        holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_disable);
                        holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_disable);
                        holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_disable);
                        holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_disable);
                        holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_disable);

                        data.forEach((key, value) -> {
                            if (key.equals(userId)) {
                                if ((Long) value == 1)
                                    holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_enable);
                                else if ((Long) value == 2)
                                    holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_enable);
                                else if ((Long) value == 3)
                                    holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_enable);
                                else if ((Long) value == 4)
                                    holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_enable);
                                else if ((Long) value == 5)
                                    holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_enable);
                            }
                            cnt[(int) ((Long) value - 1)]++;
                        });
                    }
                    holder.binding.txtCountOne.setText(String.valueOf(cnt[0]));
                    holder.binding.txtCountTwo.setText(String.valueOf(cnt[1]));
                    holder.binding.txtCountThree.setText(String.valueOf(cnt[2]));
                    holder.binding.txtCountFour.setText(String.valueOf(cnt[3]));
                    holder.binding.txtCountFive.setText(String.valueOf(cnt[4]));
                });

                holder.binding.btnEmojiOne.setOnClickListener(view -> {
                    postRepository.updatePostEmoji(postId, userId, 1L, v -> {
                        postRepository.getPostReaction(postId, it1 -> {
                            int[] cnt = {0, 0, 0, 0, 0};
                            Map<String, Object> data = it1.getData();
                            if (data != null && data.size() > 0) {
                                holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_disable);

                                data.forEach((key, value) -> {
                                    if (key.equals(userId)) {
                                        if ((Long) value == 1)
                                            holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 2)
                                            holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 3)
                                            holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 4)
                                            holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 5)
                                            holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_enable);
                                    }
                                    cnt[(int) ((Long) value - 1)]++;
                                });
                            }
                            holder.binding.txtCountOne.setText(String.valueOf(cnt[0]));
                            holder.binding.txtCountTwo.setText(String.valueOf(cnt[1]));
                            holder.binding.txtCountThree.setText(String.valueOf(cnt[2]));
                            holder.binding.txtCountFour.setText(String.valueOf(cnt[3]));
                            holder.binding.txtCountFive.setText(String.valueOf(cnt[4]));
                        });
                    });
                });

                holder.binding.btnEmojiTwo.setOnClickListener(view -> {
                    postRepository.updatePostEmoji(postId, userId, 2L, v -> {
                        postRepository.getPostReaction(postId, it1 -> {
                            int[] cnt = {0, 0, 0, 0, 0};
                            Map<String, Object> data = it1.getData();
                            if (data != null && data.size() > 0) {
                                holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_disable);

                                data.forEach((key, value) -> {
                                    if (key.equals(userId)) {
                                        if ((Long) value == 1)
                                            holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 2)
                                            holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 3)
                                            holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 4)
                                            holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 5)
                                            holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_enable);
                                    }
                                    cnt[(int) ((Long) value - 1)]++;
                                });
                            }
                            holder.binding.txtCountOne.setText(String.valueOf(cnt[0]));
                            holder.binding.txtCountTwo.setText(String.valueOf(cnt[1]));
                            holder.binding.txtCountThree.setText(String.valueOf(cnt[2]));
                            holder.binding.txtCountFour.setText(String.valueOf(cnt[3]));
                            holder.binding.txtCountFive.setText(String.valueOf(cnt[4]));
                        });
                    });
                });

                holder.binding.btnEmojiThree.setOnClickListener(view -> {
                    postRepository.updatePostEmoji(postId, userId, 3L, v -> {
                        postRepository.getPostReaction(postId, it1 -> {
                            int[] cnt = {0, 0, 0, 0, 0};
                            Map<String, Object> data = it1.getData();
                            if (data != null && data.size() > 0) {
                                holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_disable);

                                data.forEach((key, value) -> {
                                    if (key.equals(userId)) {
                                        if ((Long) value == 1)
                                            holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 2)
                                            holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 3)
                                            holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 4)
                                            holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 5)
                                            holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_enable);
                                    }
                                    cnt[(int) ((Long) value - 1)]++;
                                });
                            }
                            holder.binding.txtCountOne.setText(String.valueOf(cnt[0]));
                            holder.binding.txtCountTwo.setText(String.valueOf(cnt[1]));
                            holder.binding.txtCountThree.setText(String.valueOf(cnt[2]));
                            holder.binding.txtCountFour.setText(String.valueOf(cnt[3]));
                            holder.binding.txtCountFive.setText(String.valueOf(cnt[4]));
                        });
                    });
                });

                holder.binding.btnEmojiFour.setOnClickListener(view -> {
                    postRepository.updatePostEmoji(postId, userId, 4L, v -> {
                        postRepository.getPostReaction(postId, it1 -> {
                            int[] cnt = {0, 0, 0, 0, 0};
                            Map<String, Object> data = it1.getData();
                            if (data != null && data.size() > 0) {
                                holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_disable);

                                data.forEach((key, value) -> {
                                    if (key.equals(userId)) {
                                        if ((Long) value == 1)
                                            holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 2)
                                            holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 3)
                                            holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 4)
                                            holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 5)
                                            holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_enable);
                                    }
                                    cnt[(int) ((Long) value - 1)]++;
                                });
                            }
                            holder.binding.txtCountOne.setText(String.valueOf(cnt[0]));
                            holder.binding.txtCountTwo.setText(String.valueOf(cnt[1]));
                            holder.binding.txtCountThree.setText(String.valueOf(cnt[2]));
                            holder.binding.txtCountFour.setText(String.valueOf(cnt[3]));
                            holder.binding.txtCountFive.setText(String.valueOf(cnt[4]));
                        });
                    });
                });

                holder.binding.btnEmojiFive.setOnClickListener(view -> {
                    postRepository.updatePostEmoji(postId, userId, 5L, v -> {
                        postRepository.getPostReaction(postId, it1 -> {
                            int[] cnt = {0, 0, 0, 0, 0};
                            Map<String, Object> data = it1.getData();
                            if (data != null && data.size() > 0) {
                                holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_disable);
                                holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_disable);

                                data.forEach((key, value) -> {
                                    if (key.equals(userId)) {
                                        if ((Long) value == 1)
                                            holder.binding.btnEmojiOne.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 2)
                                            holder.binding.btnEmojiTwo.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 3)
                                            holder.binding.btnEmojiThree.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 4)
                                            holder.binding.btnEmojiFour.setBackgroundResource(R.drawable.bg_emoji_enable);
                                        else if ((Long) value == 5)
                                            holder.binding.btnEmojiFive.setBackgroundResource(R.drawable.bg_emoji_enable);
                                    }
                                    cnt[(int) ((Long) value - 1)]++;
                                });
                            }
                            holder.binding.txtCountOne.setText(String.valueOf(cnt[0]));
                            holder.binding.txtCountTwo.setText(String.valueOf(cnt[1]));
                            holder.binding.txtCountThree.setText(String.valueOf(cnt[2]));
                            holder.binding.txtCountFour.setText(String.valueOf(cnt[3]));
                            holder.binding.txtCountFive.setText(String.valueOf(cnt[4]));
                        });
                    });
                });
            }
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
