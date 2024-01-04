package com.example.locketclone.repository;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

@FunctionalInterface
public interface PostOnSuccess {
    void onSuccess(QuerySnapshot doc);
}
