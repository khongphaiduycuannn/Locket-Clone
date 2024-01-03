package com.example.locketclone.ui.camera;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CameraViewModel extends ViewModel {

    public MutableLiveData<Boolean> status = new MutableLiveData<>(true);

    public byte[] image;

    public MutableLiveData<Boolean> flashMode = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> cameraMode = new MutableLiveData<>(true);

    public void flipStatus() {
        status.setValue(!status.getValue());
    }

    public void flipFlashMode() {
        flashMode.setValue(!flashMode.getValue());
    }

    public void flipCamera() {
        cameraMode.setValue(!cameraMode.getValue());
    }

    public void setImage(byte[] newImage) {
        image = newImage;
    }
}
