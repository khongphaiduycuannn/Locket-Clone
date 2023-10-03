package com.example.locketclone.ui.history;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HistoryViewModel extends ViewModel {

    public MutableLiveData<Integer> currentPos = new MutableLiveData<>(0);

    public MutableLiveData<Boolean> status = new MutableLiveData<>(false);

    public void flipStatus() {
        status.setValue(!status.getValue());
    }

    public void setCurrentPos(Integer newPos) {
        currentPos.setValue(newPos);
    }
}
