package com.example.locketclone.repository;

import com.example.locketclone.model.Newsfeed;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewsfeedRepository {

    private final FirebaseFirestore firebaseStore = FirebaseFirestore.getInstance();

    public void createNewsfeed(Newsfeed newsfeed) {
        DocumentReference doc = firebaseStore.collection("newsfeed").document();
        newsfeed.setNewsfeedId(doc.getId());
        doc.set(newsfeed);
    }
}
