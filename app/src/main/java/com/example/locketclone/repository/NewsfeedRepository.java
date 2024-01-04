package com.example.locketclone.repository;

import com.example.locketclone.model.Newsfeed;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsfeedRepository {

    private final FirebaseFirestore firebaseStore = FirebaseFirestore.getInstance();

    public void createNewsfeed(Newsfeed newsfeed) {
        DocumentReference doc = firebaseStore.collection("newsfeed").document();
        newsfeed.setNewsfeedId(doc.getId());
        doc.set(newsfeed);
    }

    public void getNewsfeedByUserId(String userId, PostOnSuccess task) {
        firebaseStore.collection("newsfeed")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(task::onSuccess);
    }

    public void updateNewsfeedPost(Newsfeed newsfeed) {
        firebaseStore.collection("newsfeed")
                .document(newsfeed.getNewsfeedId())
                .set(newsfeed, SetOptions.merge());
    }

    public void updateNewsfeed(String newsfeedId, ArrayList<String> posts) {
        Map<String, Object> mp = new HashMap<>();
        mp.put("posts", posts);
        firebaseStore.collection("newsfeed")
                .document(newsfeedId)
                .set(mp, SetOptions.merge());
    }

    public void newsfeedSnapshotListener(String newsfeedId, NewsfeedSnapshotListener task) {
        firebaseStore.collection("newsfeed")
                .document(newsfeedId)
                .addSnapshotListener(task::onSnapshot);
    }
}
