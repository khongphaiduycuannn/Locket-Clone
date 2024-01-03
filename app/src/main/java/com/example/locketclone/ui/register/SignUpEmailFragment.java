package com.example.locketclone.ui.register;

import android.graphics.Color;
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
import com.example.locketclone.databinding.FragmentSignUpEmailBinding;

public class SignUpEmailFragment extends BaseFragment<FragmentSignUpEmailBinding> {

    private SignUpViewModel signUpViewModel;

    @Override
    public void initData() {
        signUpViewModel = new ViewModelProvider(getActivity()).get(SignUpViewModel.class);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnContinue.setOnClickListener(view -> {
            String email = getBinding().edtEmail.getText().toString();
            if (!email.isEmpty() && !email.isBlank()) {
                signUpViewModel.setEmail(email);
                Navigation.findNavController(getView()).navigate(R.id.action_signUpEmailFragment_to_signUpPasswordFragment);
            }
            else {
                Toast.makeText(requireContext(), "Email invalidate", Toast.LENGTH_LONG).show();
            }
        });

        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });

        getBinding().edtEmail.addTextChangedListener(textWatcher());
    }

    @Override
    protected FragmentSignUpEmailBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentSignUpEmailBinding.inflate(inflater);
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