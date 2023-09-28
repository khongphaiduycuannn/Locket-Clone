package com.example.locketclone.ui.login;

import android.view.LayoutInflater;

import androidx.navigation.Navigation;

import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentLoginPasswordBinding;

public class LoginPasswordFragment extends BaseFragment<FragmentLoginPasswordBinding> {

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnContinue.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginPasswordFragment_to_loginUsernameFragment);
        });

        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });
    }

    @Override
    protected FragmentLoginPasswordBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginPasswordBinding.inflate(inflater);
    }
}