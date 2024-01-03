package com.example.locketclone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.locketclone.base.BaseActivity;
import com.example.locketclone.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    public void initData() {

    }

    @Override
    public void handleEvent() {
        cameraPermission();
        storagePermission();
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
}