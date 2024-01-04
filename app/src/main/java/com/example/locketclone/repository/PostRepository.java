package com.example.locketclone.repository;

import com.example.locketclone.model.Post;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostRepository {

    private final FirebaseFirestore firebaseStore = FirebaseFirestore.getInstance();

    private final NewsfeedRepository newsfeedRepository = new NewsfeedRepository();

    public void createPost(Post post, ArrayList<String> userIds, PostOnComplete task) {
        DocumentReference doc = firebaseStore.collection("post").document();
        post.setPostId(doc.getId());
        doc.set(post).addOnCompleteListener(it -> {
            if (it.isSuccessful()) {
                task.onComplete(it);
                addPostToNewsfeed(userIds, post.getPostId());
            }
        });
    }

    public void getPostById(String postId, UserOnSuccess task) {
        firebaseStore.collection("post")
                .document(postId)
                .get()
                .addOnSuccessListener(task::onSuccess);
    }

    public void getPostReaction(String postId, UserOnSuccess task) {
        firebaseStore.collection("react")
                .document(postId)
                .get()
                .addOnSuccessListener(task::onSuccess);
    }

    public void updatePostEmoji(String postId, String userId, Long react, UserOnComplete task) {
        Map<String, Long> map = new HashMap<>();
        map.put(userId, react);
        firebaseStore.collection("react")
                .document(postId)
                .set(map, SetOptions.merge())
                .addOnCompleteListener(task::onComplete);
    }

    public void addPostToNewsfeed(ArrayList<String> userIds, String postId) {
        for (String userId :
                userIds) {
            firebaseStore.collection("newsfeed")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener(it -> {
                        if (!it.isEmpty()) {
                            ArrayList<String> posts = (ArrayList<String>) it.getDocuments().get(0).getData().get("posts");
                            posts.add(postId);
                            newsfeedRepository.updateNewsfeed(it.getDocuments().get(0).getId(), posts);
                        }
                    });
        }
    }
}
