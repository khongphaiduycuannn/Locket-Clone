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
import com.example.locketclone.databinding.FragmentLoginUsernameBinding;
import com.example.locketclone.model.Newsfeed;
import com.example.locketclone.model.User;
import com.example.locketclone.repository.NewsfeedRepository;
import com.example.locketclone.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUsernameFragment extends BaseFragment<FragmentLoginUsernameBinding> {

    private LoginViewModel loginViewModel;

    private FirebaseAuth firebaseAuth;

    private UserRepository userRepository = new UserRepository();

    private NewsfeedRepository newsfeedRepository = new NewsfeedRepository();


    @Override
    public void initData() {
        loginViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        firebaseAuth = FirebaseAuth.getInstance();
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
            String firstName = getBinding().edtFirstName.getText().toString();
            String lastName = getBinding().edtLastName.getText().toString();
            if (!firstName.isEmpty() && !firstName.isBlank() && !lastName.isEmpty() && !lastName.isBlank()) {
                register(loginViewModel.email, loginViewModel.password, firstName, lastName);
            } else {
                Toast.makeText(requireContext(), "Email invalidate", Toast.LENGTH_LONG).show();
            }
        });

        getBinding().edtFirstName.addTextChangedListener(textWatcher());

        getBinding().edtLastName.addTextChangedListener(textWatcher());
    }

    @Override
    protected FragmentLoginUsernameBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentLoginUsernameBinding.inflate(inflater);
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

    private void register(String email, String password, String firstName, String lastName) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(it -> {
                    if (it.isSuccessful()) {
                        String userId = it.getResult().getUser().getUid();
                        userRepository.createUser(new User(userId, email, firstName, lastName, password));
                        newsfeedRepository.createNewsfeed(new Newsfeed(userId));
                        Toast.makeText(requireContext(), "Register success!", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(getView()).navigate(R.id.action_loginUsernameFragment_to_loginFragment);
                    } else {
                        Toast.makeText(requireContext(), "Account is Invalid!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}