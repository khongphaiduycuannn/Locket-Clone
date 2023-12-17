package com.example.locketclone.ui.camera;

import android.view.LayoutInflater;

import androidx.navigation.Navigation;

import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentCameraBinding;

public class CameraFragment extends BaseFragment<FragmentCameraBinding> {

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnProfileNav.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_profileFragment);
        });

        getBinding().btnFriendNav.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_friendsFragment);
        });

        getBinding().btnHistoryNav.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_historyFragment);
        });

        getBinding().btnShot.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_loginFragment);
        });
    }

    @Override
    protected FragmentCameraBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentCameraBinding.inflate(inflater);
    }
}