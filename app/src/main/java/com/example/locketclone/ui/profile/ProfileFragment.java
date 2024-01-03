package com.example.locketclone.ui.profile;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.navigation.Navigation;

import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentProfileBinding;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {


    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnEditUsername.setOnClickListener(view -> {
            showEditAvatarDialog();
        });

        getBinding().btnEditAvatar.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_editUsernameFragment);
        });

        getBinding().btnSignOut.setOnClickListener(view -> {
            MyApplication.clearUserId();
            Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_loginFragment);
        });

        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });
    }

    @Override
    protected FragmentProfileBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentProfileBinding.inflate(inflater);
    }

    private void showEditAvatarDialog() {
        Dialog settingDialog = new Dialog(requireContext());
        settingDialog.setContentView(R.layout.dialog_edit_avatar);

        Window window = settingDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
        settingDialog.show();
    }
}