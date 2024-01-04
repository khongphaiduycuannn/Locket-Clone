package com.example.locketclone.repository;

import com.google.firebase.firestore.DocumentSnapshot;

@FunctionalInterface
public interface UserOnSuccess {
    void onSuccess(DocumentSnapshot doc);
}
