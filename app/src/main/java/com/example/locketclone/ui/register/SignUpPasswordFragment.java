package com.example.locketclone.ui.register;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentSignUpPasswordBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpPasswordFragment extends BaseFragment<FragmentSignUpPasswordBinding> {

    private SignUpViewModel signUpViewModel;

    private FirebaseAuth firebaseAuth;

    @Override
    public void initData() {
        signUpViewModel = new ViewModelProvider(getActivity()).get(SignUpViewModel.class);
    }

    @Override
    public void initView() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void initEvent() {
        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });

        getBinding().btnContinue.setOnClickListener(view -> {
            String password = getBinding().edtPassword.getText().toString();
            if (!password.isEmpty() && !password.isBlank()) {
                signUpViewModel.setPassword(password);
                login(signUpViewModel.email.getValue(), password);
            } else {
                Toast.makeText(requireContext(), "Password invalidate", Toast.LENGTH_LONG).show();
            }
        });

        getBinding().edtPassword.addTextChangedListener(textWatcher());
    }

    @Override
    protected FragmentSignUpPasswordBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentSignUpPasswordBinding.inflate(inflater);
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

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it -> {
                    if (it.isSuccessful()) {
                        MyApplication.setUserId(it.getResult().getUser().getUid());
                        Toast.makeText(requireContext(), "Login success!", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_signUpPasswordFragment_to_cameraFragment);
                    } else {
                        Toast.makeText(requireContext(), "Email or password is incorrect!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}