package com.example.locketclone.ui.camera;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.locketclone.R;
import com.example.locketclone.adpater.FriendChooseAdapter;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentCameraBinding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraFragment extends BaseFragment<FragmentCameraBinding> {

    private final int MEDIA_TYPE_IMAGE = 1;

    private CameraViewModel cameraViewModel;

    private Camera camera;
    private Camera frontCamera;
    private CameraPreview frontCameraPreview;
    private CameraPreview backCameraPreview;
    private Camera.PictureCallback picture;
    private Camera.Parameters parameters;

    private FriendChooseAdapter friendChooseAdapter;

    private ArrayList<String> data = new ArrayList<>(
            Arrays.asList("A", "A", "A", "A", "A", "A"));

    @Override
    public void initData() {
        initConfig();
        cameraViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        camera = getBackCameraInstance();
//        frontCamera = getFrontCameraInstance();
        if (camera != null) {
            camera.setDisplayOrientation(90);
//            frontCamera.setDisplayOrientation(90);
            backCameraPreview = new CameraPreview(requireContext(), camera);
            frontCameraPreview = new CameraPreview(requireContext(), frontCamera);
            parameters = camera.getParameters();
        }
        picture = (data, camera) -> cameraViewModel.setImage(data);
        friendChooseAdapter = new FriendChooseAdapter();
    }

    @Override
    public void initView() {
        if (backCameraPreview != null) {
            FrameLayout preview = getBinding().cameraPreview;
            preview.addView(backCameraPreview);
        }
        setFriendsChoose();
        setPreviewSize();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initEvent() {
        getBinding().btnProfileNav.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_profileFragment);
        });

        getBinding().btnFriendNav.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_friendsFragment);
        });

        getBinding().btnHistoryNav.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_historyFragment);
        });

        getBinding().btnCapture.setOnClickListener(view -> {
            Navigation.findNavController(getView()).navigate(R.id.action_cameraFragment_to_loginFragment);
        });

        getBinding().btnDownload.setOnClickListener(view -> {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d("", "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(cameraViewModel.image);
                fos.close();
                Toast.makeText(requireContext(), "Download success!", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Log.d("", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("", "Error accessing file: " + e.getMessage());
            }
        });

        Camera.AutoFocusCallback autoFocusCallback = (success, camera) -> {
        };

        if (backCameraPreview != null) {
            backCameraPreview.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Camera camera = this.camera;
                    try {
                        camera.cancelAutoFocus();
                        camera.autoFocus(autoFocusCallback);
                    } catch (Exception e) {
                        Log.e("", "Auto focus error");
                    }
                }
                return true;
            });
        }

        getBinding().btnCapture.setOnClickListener(view -> {
            if (camera != null) {
                camera.takePicture(null, null, picture);
                cameraViewModel.flipStatus();
            }
        });

        getBinding().btnCancel.setOnClickListener(view -> {
            if (camera != null) {
                cameraViewModel.flipStatus();
            }
        });

        getBinding().btnFlash.setOnClickListener(view -> {
            cameraViewModel.flipFlashMode();
        });

        getBinding().btnSend.setOnClickListener(view -> {
//            if (camera != null) {
//                cameraViewModel.flipStatus();
//            }

            byte[] image = cameraViewModel.image;
            if (image != null) {
                MediaManager.get()
                        .upload(image)
                        .callback(new UploadCallback() {
                            @Override
                            public void onStart(String requestId) {

                            }

                            @Override
                            public void onProgress(String requestId, long bytes, long totalBytes) {

                            }

                            @Override
                            public void onSuccess(String requestId, Map resultData) {
                                Toast.makeText(requireContext(), "Upload success!", Toast.LENGTH_LONG).show();
                                cameraViewModel.flipStatus();
                            }

                            @Override
                            public void onError(String requestId, ErrorInfo error) {

                            }

                            @Override
                            public void onReschedule(String requestId, ErrorInfo error) {

                            }
                        }).dispatch();
            }
        });

        getBinding().btnFlipCamera.setOnClickListener(view -> {
            cameraViewModel.flipCamera();
        });

        cameraViewModel.status.observe(getViewLifecycleOwner(), this::onStatusChange);
        cameraViewModel.flashMode.observe(getViewLifecycleOwner(), this::onFlashModeChange);
//        cameraViewModel.cameraMode.observe(getViewLifecycleOwner(), this::onCameraModeChange);
    }

    @Override
    protected FragmentCameraBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentCameraBinding.inflate(inflater);
    }

    public void initConfig() {
        HashMap<String, String> config = new HashMap<>();
        config.put("cloud_name", "dh9ougddd");
        config.put("api_key", "731328598728427");
        config.put("api_secret", "9cayqFpAlaCGOnudfeaW-hGFsQ0");
        MediaManager.init(requireContext(), config);
    }

    public void onStatusChange(boolean newStatus) {
        if (newStatus) {
            camera.startPreview();
            getBinding().clTakePhoto.setVisibility(View.VISIBLE);
            getBinding().lnHistoryNav.setVisibility(View.VISIBLE);
            getBinding().lnFriendChoose.setVisibility(View.GONE);
            getBinding().clSendPhoto.setVisibility(View.GONE);
        } else {
            getBinding().clTakePhoto.setVisibility(View.GONE);
            getBinding().lnHistoryNav.setVisibility(View.GONE);
            getBinding().lnFriendChoose.setVisibility(View.VISIBLE);
            getBinding().clSendPhoto.setVisibility(View.VISIBLE);
        }
    }

    public void onFlashModeChange(boolean newFlashMode) {
        if (newFlashMode) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            getBinding().btnFlash.setImageResource(R.drawable.ic_flash_on);
        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            getBinding().btnFlash.setImageResource(R.drawable.ic_flash_off);
        }
        camera.setParameters(parameters);
//        frontCamera.setParameters(parameters);
    }

//    public void onCameraModeChange(boolean newCameraMode) {
//        FrameLayout preview = getBinding().cameraPreview;
//        preview.removeAllViews();
//        if (newCameraMode) {
//            preview.addView(backCameraPreview);
//        }
//        else {
//            preview.addView(frontCameraPreview);
//        }
//        setPreviewSize();
//    }

    private void setFriendsChoose() {
        getBinding().rclFriendShare.setAdapter(friendChooseAdapter);
        friendChooseAdapter.setListFriendChooses(data);
    }

    public void setPreviewSize() {
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
        camera.setParameters(parameters);
//        frontCamera.setParameters(parameters);
    }

//    public Camera getFrontCameraInstance() {
//        Camera camera = null;
//        try {
//            camera = openFrontFacingCamera();
//        } catch (Exception e) {
//            Log.e("Camera Fragment", "Error opening camera: " + e.getMessage());
//        }
//        return camera;
//    }

    public Camera getBackCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e("Camera Fragment", "Error opening camera: " + e.getMessage());
        }
        return camera;
    }

//    private Camera openFrontFacingCamera() {
//        int cameraCount = 0;
//        Camera cam = null;
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        cameraCount = Camera.getNumberOfCameras();
//        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
//            Camera.getCameraInfo(camIdx, cameraInfo);
//            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                try {
//                    cam = Camera.open(camIdx);
//                } catch (RuntimeException e) {
//                    Log.e("Camera Fragment", "Camera failed to open: " + e.getLocalizedMessage());
//                }
//            }
//        }
//
//        return cam;
//    }

    public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w / h;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - h);
            }
        }

        if (optimalSize == null) {
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - h);
                }
            }
        }
        return optimalSize;
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "CameraPD"
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        switch (type) {
            case MEDIA_TYPE_IMAGE:
                return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

            default:
                return null;
        }
    }
}