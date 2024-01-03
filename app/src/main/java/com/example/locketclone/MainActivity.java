package com.example.locketclone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cloudinary.android.MediaManager;
import com.example.locketclone.base.BaseActivity;
import com.example.locketclone.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public void initData() {

    }

    @Override
    public void handleEvent() {
        cameraPermission();
        storagePermission();
        cloudinaryConfig();
    }

    @Override
    public void bindData() {

    }

    @Override
    protected ActivityMainBinding inflateViewBinding(LayoutInflater inflater) {
        return ActivityMainBinding.inflate(inflater);
    }

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private void storagePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void cloudinaryConfig() {
        HashMap<String, String> config = new HashMap<>();
        config.put("cloud_name", "dh9ougddd");
        config.put("api_key", "731328598728427");
        config.put("api_secret", "9cayqFpAlaCGOnudfeaW-hGFsQ0");
        MediaManager.init(this, config);
    }
}