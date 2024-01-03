package com.example.locketclone.repository;

import com.example.locketclone.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private final FirebaseFirestore firebaseStore = FirebaseFirestore.getInstance();


    public void createUser(User user) {
        firebaseStore.collection("user")
                .document(user.getUserId())
                .set(user);
    }
}
