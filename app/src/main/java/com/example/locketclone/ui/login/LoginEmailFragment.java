package com.example.locketclone.ui.login;

import android.view.LayoutInflater;

import androidx.navigation.Navigation;

import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentLoginEmailBinding;

public class LoginEmailFragment extends BaseFragment<FragmentLoginEmailBinding> {

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnContinue.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginEmailFragment_to_loginPasswordFragment);
        });

        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });
    }

    @Override
    protected FragmentLoginEmailBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginEmailBinding.inflate(inflater);
    }
}