package com.example.locketclone.repository;

import com.example.locketclone.MyApplication;
import com.example.locketclone.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void getUserById(String userId, UserOnSuccess task) {
        firebaseStore.collection("user")
                .document(userId)
                .get()
                .addOnSuccessListener(doc -> {
                    String userID = (String) doc.getData().get("userId");
                    String firstName = (String) doc.getData().get("firstName");
                    String lastName = (String) doc.getData().get("lastName");
                    String avatar = (String) doc.getData().get("avatar");
                    String email = (String) doc.getData().get("email");
                    String phone = (String) doc.getData().get("phone");
                    String password = (String) doc.getData().get("password");
                    ArrayList<String> friends = (ArrayList<String>) doc.getData().get("friends");
                    User user = new User(userID, email, password, firstName, lastName, avatar, phone, friends);
                    MyApplication.setUser(user);
                    task.onSuccess();
                });
    }
}
