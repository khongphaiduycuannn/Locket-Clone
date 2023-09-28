package com.example.locketclone.ui.login;

import android.view.LayoutInflater;

import androidx.navigation.Navigation;

import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentLoginBinding;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> {

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnSignUp.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginFragment_to_loginEmailFragment);
        });
    }

    @Override
    protected FragmentLoginBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginBinding.inflate(inflater);
    }
}