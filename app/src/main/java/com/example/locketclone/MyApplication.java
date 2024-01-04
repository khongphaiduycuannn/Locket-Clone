package com.example.locketclone;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.locketclone.model.Newsfeed;
import com.example.locketclone.model.User;

public class MyApplication extends Application {

    private static Context context;

    private static User currentUser;

    private static Newsfeed currentNewsfeed;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }

    public static String getUserId() {
        SharedPreferences setting = getAppContext().getSharedPreferences("app_status", 0);
        return setting.getString("user_id", null);
    }

    public static void setUserId(String userId) {
        SharedPreferences setting = getAppContext().getSharedPreferences("app_status", 0);
        setting.edit().putString("user_id", userId).apply();
    }

    public static void clearUserId() {
        SharedPreferences setting = getAppContext().getSharedPreferences("app_status", 0);
        setting.edit().remove("user_id").apply();
    }

    public static User getUser() {
        return currentUser;
    }

    public static void setUser(User user) {
        currentUser = user;
    }

    public static Newsfeed getNewsfeed() {
        return currentNewsfeed;
    }

    public static void setNewsfeed(Newsfeed newsfeed) {
        currentNewsfeed = newsfeed;
    }
}
