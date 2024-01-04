package com.example.locketclone.repository;

import com.google.android.gms.tasks.Task;

@FunctionalInterface
public interface PostOnComplete {
    void onComplete(Task it);
}
