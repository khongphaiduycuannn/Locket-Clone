package com.example.locketclone.ui.login;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentLoginEmailBinding;

public class LoginEmailFragment extends BaseFragment<FragmentLoginEmailBinding> {

    private LoginViewModel loginViewModel;

    @Override
    public void initData() {
        loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });

        getBinding().btnContinue.setOnClickListener(view -> {
            String email = getBinding().edtEmail.getText().toString();
            if (!email.isEmpty() && !email.isBlank()) {
                loginViewModel.email = email;
                Navigation.findNavController(getView()).navigate(R.id.action_loginEmailFragment_to_loginPasswordFragment);
            } else {
                Toast.makeText(requireContext(), "Email invalidate", Toast.LENGTH_LONG).show();
            }
        });

        getBinding().edtEmail.addTextChangedListener(textWatcher());
    }

    @Override
    protected FragmentLoginEmailBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginEmailBinding.inflate(inflater);
    }

    private TextWatcher textWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.isEmpty()) {
                    getBinding().btnContinue.setEnabled(false);
                    getBinding().btnContinue.setTextColor(Color.parseColor("#505052"));
                    getBinding().btnContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_btn_continue_disable));
                } else {
                    getBinding().btnContinue.setEnabled(true);
                    getBinding().btnContinue.setTextColor(Color.parseColor("#101010"));
                    getBinding().btnContinue.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_btn_continue_enable));
                }
            }
        };
    }
}