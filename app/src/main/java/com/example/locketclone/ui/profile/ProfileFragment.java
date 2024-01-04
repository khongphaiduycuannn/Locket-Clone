package com.example.locketclone.ui.profile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.DialogEditAvatarBinding;
import com.example.locketclone.databinding.FragmentProfileBinding;
import com.example.locketclone.model.User;
import com.example.locketclone.repository.UserRepository;

import java.util.Map;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> {

    private User currentUser = MyApplication.getUser();

    private UserRepository userRepository = new UserRepository();

    private Dialog settingDialog;

    private DialogEditAvatarBinding dialogEditAvatarBinding;

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public void initData() {
        settingDialog = new Dialog(requireContext());
        dialogEditAvatarBinding = DialogEditAvatarBinding.inflate(getLayoutInflater());
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            Uri imageUri = result.getData().getData();
            if (result.getResultCode() == Activity.RESULT_OK) {
                MediaManager.get().upload(imageUri).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String avatarUrl = MediaManager.get().url().generate(resultData.get("public_id").toString());
                        User tempUser = new User();
                        tempUser.setUser(currentUser);
                        tempUser.setAvatar(avatarUrl);
                        userRepository.updateUser(tempUser, it -> {
                            currentUser.setUser(tempUser);
                            try {
                                Glide.with(getBinding().imgAvatar.getContext())
                                        .load(currentUser.getAvatar())
                                        .into(getBinding().imgAvatar);
                                Toast.makeText(getActivity(), "Change success!", Toast.LENGTH_LONG).show();
                            } catch (Exception ignored) {

                            }
                        });
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();
            }
        });

    }

    @Override
    public void initView() {
        String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        getBinding().txtUsername.setText(fullName);
        Glide.with(getBinding().imgAvatar.getContext())
                .load(currentUser.getAvatar())
                .into(getBinding().imgAvatar);
    }

    @Override
    public void initEvent() {
        getBinding().btnEditAvatar.setOnClickListener(view -> {
            showEditAvatarDialog();
        });

        getBinding().btnEditUsername.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_editUsernameFragment);
        });

        getBinding().btnSignOut.setOnClickListener(view -> {
            MyApplication.clearUserId();
            Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_loginFragment);
        });

        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });

        dialogEditAvatarBinding.btnLibrary.setOnClickListener(view -> {
            chooseImage();
            settingDialog.dismiss();
        });

        dialogEditAvatarBinding.btnCancel.setOnClickListener(view -> {
            settingDialog.dismiss();
        });
    }

    @Override
    protected FragmentProfileBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentProfileBinding.inflate(inflater);
    }

    private void showEditAvatarDialog() {
        settingDialog.setContentView(dialogEditAvatarBinding.getRoot());

        Window window = settingDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
        settingDialog.show();
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        resultLauncher.launch(intent);
    }
}