package com.example.locketclone.ui.login;

import android.view.LayoutInflater;

import androidx.navigation.Navigation;

import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentLoginBinding;
import com.example.locketclone.model.User;
import com.example.locketclone.repository.UserRepository;

import java.util.ArrayList;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    private UserRepository userRepository = new UserRepository();

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        checkUserId();
    }

    @Override
    public void initEvent() {
        getBinding().btnSignUp.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_loginEmailFragment);
        });

        getBinding().btnSignIn.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_signUpEmailFragment);
        });
    }

    @Override
    protected FragmentLoginBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginBinding.inflate(inflater);
    }

    private void checkUserId() {
        String userId = MyApplication.getUserId();
        if (!(userId == null || userId.isEmpty() || userId.isBlank())) {
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
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_cameraFragment2);
            });
        }
    }
}