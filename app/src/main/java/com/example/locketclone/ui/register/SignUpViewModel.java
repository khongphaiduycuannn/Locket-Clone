package com.example.locketclone.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignUpViewModel extends ViewModel {

    public MutableLiveData<String> email = new MutableLiveData<>();

    public MutableLiveData<String> password = new MutableLiveData<>();

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }
}
