package com.example.locketclone.repository;

import com.example.locketclone.model.Post;
import com.example.locketclone.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;

public class UserRepository {

    private final FirebaseFirestore firebaseStore = FirebaseFirestore.getInstance();


    public void createUser(User user) {
        firebaseStore.collection("user")
                .document(user.getUserId())
                .set(user);
    }

    public void updateUser(User user, UserOnComplete task) {
        firebaseStore.collection("user")
                .document(user.getUserId())
                .set(user, SetOptions.merge())
                .addOnCompleteListener(task::onComplete);
    }

    public void updateUser(String userId, Map<String, Object> user, UserOnComplete task) {
        firebaseStore.collection("user")
                .document(userId)
                .set(user, SetOptions.merge())
                .addOnCompleteListener(task::onComplete);
    }

    public void getUserById(String userId, UserOnSuccess task) {
        firebaseStore.collection("user")
                .document(userId)
                .get()
                .addOnSuccessListener(task::onSuccess);
    }

    public void getAllUser(PostOnSuccess task) {
        firebaseStore.collection("user")
                .get()
                .addOnSuccessListener(task::onSuccess);
    }

    public void userSnapshotListener(String userId, UserSnapshotListener task) {
        firebaseStore.collection("user")
                .document(userId)
                .addSnapshotListener(task::onSnapshot);
    }
}
