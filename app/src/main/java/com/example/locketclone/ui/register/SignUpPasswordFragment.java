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
import com.example.locketclone.databinding.FragmentSignUpPasswordBinding;
import com.example.locketclone.model.Newsfeed;
import com.example.locketclone.model.User;
import com.example.locketclone.repository.NewsfeedRepository;
import com.example.locketclone.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SignUpPasswordFragment extends BaseFragment<FragmentSignUpPasswordBinding> {

    private SignUpViewModel signUpViewModel;

    private FirebaseAuth firebaseAuth;

    private UserRepository userRepository = new UserRepository();

    private NewsfeedRepository newsfeedRepository = new NewsfeedRepository();

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
                        String userId = it.getResult().getUser().getUid();
                        MyApplication.setUserId(userId);
                        Toast.makeText(requireContext(), "Login success!", Toast.LENGTH_LONG).show();
                        userRepository.getUserById(userId, doc -> {
                            String userID = (String) doc.getData().get("userId");
                            String firstName = (String) doc.getData().get("firstName");
                            String lastName = (String) doc.getData().get("lastName");
                            String avatar = (String) doc.getData().get("avatar");
                            String curEmail = (String) doc.getData().get("email");
                            String phone = (String) doc.getData().get("phone");
                            String curPassword = (String) doc.getData().get("password");
                            ArrayList<String> friends = (ArrayList<String>) doc.getData().get("friends");

                            User user = new User(userID, email, password, firstName, lastName, avatar, phone, friends);
                            MyApplication.setUser(user);
                            newsfeedRepository.getNewsfeedByUserId(userID, it2 -> {
                                if (!it2.getDocuments().isEmpty()) {
                                    String newsfeedId = (String) it2.getDocuments().get(0).getData().get("newsfeedId");
                                    ArrayList<String> posts = (ArrayList<String>) it2.getDocuments().get(0).getData().get("posts");

                                    Newsfeed newsfeed = new Newsfeed(newsfeedId, userID, posts);
                                    MyApplication.setNewsfeed(newsfeed);
                                    newsfeedRepository.updateNewsfeedPost(newsfeed);
                                }
                                Navigation.findNavController(getView()).navigate(R.id.action_signUpPasswordFragment_to_cameraFragment);
                            });
                        });
                    } else {
                        Toast.makeText(requireContext(), "Email or password is incorrect!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}