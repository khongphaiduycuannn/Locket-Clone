package com.example.locketclone.ui.login;

import android.view.LayoutInflater;

import androidx.navigation.Navigation;

import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentLoginBinding;
import com.example.locketclone.repository.UserRepository;

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
            userRepository.getUserById(userId, () -> {
                Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_cameraFragment2);
            });
        }
    }
}