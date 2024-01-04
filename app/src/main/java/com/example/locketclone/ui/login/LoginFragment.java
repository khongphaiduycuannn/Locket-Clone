package com.example.locketclone.ui.login;

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
import com.example.locketclone.databinding.DialogDisableViewBinding;
import com.example.locketclone.databinding.FragmentLoginBinding;
import com.example.locketclone.model.Newsfeed;
import com.example.locketclone.model.User;
import com.example.locketclone.repository.NewsfeedRepository;
import com.example.locketclone.repository.UserRepository;

import java.util.ArrayList;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    private final UserRepository userRepository = new UserRepository();

    private NewsfeedRepository newsfeedRepository = new NewsfeedRepository();

    private Dialog dialog;

    @Override

    public void initData() {
        dialog = new Dialog(requireContext());
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        initDialog();
        checkUserId();
        getBinding().btnSignUp.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_loginEmailFragment);
        });

        getBinding().btnSignIn.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_signUpEmailFragment);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected FragmentLoginBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginBinding.inflate(inflater);
    }

    private void checkUserId() {
        String userId = MyApplication.getUserId();
        if (!(userId == null || userId.isEmpty() || userId.isBlank())) {
            dialog.show();
            userRepository.getUserById(userId, doc -> {
                String userID = (String) doc.getData().get("userId");
                String firstName = (String) doc.getData().get("firstName");
                String lastName = (String) doc.getData().get("lastName");
                String avatar = (String) doc.getData().get("avatar");
                String email = (String) doc.getData().get("email");
                String phone = (String) doc.getData().get("phone");
                String password = (String) doc.getData().get("password");
                ArrayList<String> friends = (ArrayList<String>) doc.getData().get("friends");

                User user = new User(userID, email, password, firstName, lastName, avatar, phone, friends);
                MyApplication.setUser(user);
                newsfeedRepository.getNewsfeedByUserId(userID, it -> {
                    if (!it.getDocuments().isEmpty()) {
                        String newsfeedId = (String) it.getDocuments().get(0).getData().get("newsfeedId");
                        ArrayList<String> posts = (ArrayList<String>) it.getDocuments().get(0).getData().get("posts");

                        Newsfeed newsfeed = new Newsfeed(newsfeedId, userID, posts);
                        MyApplication.setNewsfeed(newsfeed);
                        newsfeedRepository.updateNewsfeedPost(newsfeed);
                    }
                    dialog.dismiss();
                    Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_cameraFragment2);
                });
            });
        }
    }

    private void initDialog() {
        DialogDisableViewBinding binding = DialogDisableViewBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.5F);
        }
        dialog.setCancelable(false);
    }
}