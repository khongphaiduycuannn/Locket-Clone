package com.example.locketclone.ui.login;

import android.view.LayoutInflater;

import androidx.navigation.Navigation;

import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentLoginUsernameBinding;

public class LoginUsernameFragment extends BaseFragment<FragmentLoginUsernameBinding> {

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });
    }

    @Override
    protected FragmentLoginUsernameBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginUsernameBinding.inflate(inflater);
    }
}