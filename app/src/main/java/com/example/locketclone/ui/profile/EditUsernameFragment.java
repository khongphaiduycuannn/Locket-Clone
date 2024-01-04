package com.example.locketclone.ui.profile;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;

import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentEditUsernameBinding;
import com.example.locketclone.model.User;
import com.example.locketclone.repository.UserRepository;

public class EditUsernameFragment extends BaseFragment<FragmentEditUsernameBinding> {

    private UserRepository userRepository = new UserRepository();

    private User currentUser = MyApplication.getUser();

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        getBinding().btnSave.setEnabled(false);
        getBinding().btnSave.setOnClickListener(view -> {
            User tempUser = new User();
            tempUser.setUser(currentUser);
            tempUser.setFirstName(String.valueOf(getBinding().edtFirstName.getText()));
            tempUser.setLastName(String.valueOf(getBinding().edtLastName.getText()));
            userRepository.updateUser(tempUser, it -> {
                currentUser.setUser(tempUser);
                Toast.makeText(requireContext(), "Change username success", Toast.LENGTH_LONG).show();
            });
        });

        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });

        getBinding().edtFirstName.addTextChangedListener(textWatcher());

        getBinding().edtLastName.addTextChangedListener(textWatcher());
    }

    @Override
    protected FragmentEditUsernameBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentEditUsernameBinding.inflate(inflater);
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
                    getBinding().btnSave.setEnabled(false);
                    getBinding().txtSave.setTextColor(Color.parseColor("#FFFFFF"));
                    getBinding().txtSave.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_btn_continue_disable));
                } else {
                    getBinding().btnSave.setEnabled(true);
                    getBinding().txtSave.setTextColor(Color.parseColor("#101010"));
                    getBinding().txtSave.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_btn_continue_enable));
                }
            }
        };
    }
}