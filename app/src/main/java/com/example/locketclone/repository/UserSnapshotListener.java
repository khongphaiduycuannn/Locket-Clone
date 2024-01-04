package com.example.locketclone.repository;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;

@FunctionalInterface
public interface UserSnapshotListener {
    void onSnapshot(DocumentSnapshot doc, FirebaseFirestoreException exception);
}
