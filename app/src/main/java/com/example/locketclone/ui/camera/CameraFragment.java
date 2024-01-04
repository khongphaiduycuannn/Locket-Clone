package com.example.locketclone.ui.camera;

import android.annotation.SuppressLint;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.adpater.FriendChooseAdapter;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentCameraBinding;
import com.example.locketclone.model.Newsfeed;
import com.example.locketclone.model.Post;
import com.example.locketclone.repository.NewsfeedRepository;
import com.example.locketclone.repository.PostRepository;
import com.example.locketclone.repository.UserRepository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CameraFragment extends BaseFragment<FragmentCameraBinding> {

    private final int MEDIA_TYPE_IMAGE = 1;

    private CameraViewModel cameraViewModel;

    private Camera camera;
    private CameraPreview backCameraPreview;
    private Camera.PictureCallback picture;
    private Camera.Parameters parameters;

    private FriendChooseAdapter friendChooseAdapter;

    private ArrayList<String> friends = new ArrayList<>();

    private final UserRepository userRepository = new UserRepository();

    private final PostRepository postRepository = new PostRepository();

    private final NewsfeedRepository newsfeedRepository = new NewsfeedRepository();

    private ArrayList<String> data = new ArrayList<>(Arrays.asList("A", "A", "A", "A", "A", "A"));

    @Override
    public void initData() {
        cameraViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        camera = getBackCameraInstance();
        if (camera != null) {
            camera.setDisplayOrientation(90);
            backCameraPreview = new CameraPreview(requireContext(), camera);
            parameters = camera.getParameters();
        }
        picture = (data, camera) -> cameraViewModel.setImage(data);
        friendChooseAdapter = new FriendChooseAdapter();
    }

    @Override
    public void initView() {
        camera.startPreview();
        if (backCameraPreview != null) {
            FrameLayout preview = getBinding().cameraPreview;
            ViewGroup parentView = (ViewGroup) backCameraPreview.getParent();
            if (parentView != null) {
                parentView.removeView(backCameraPreview);
            }
            new Handler().postDelayed(() -> {
                preview.addView(backCameraPreview);
            }, 500);
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

        getBinding().btnDownload.setOnClickListener(view -> {
            savePhoto();
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
            byte[] image = cameraViewModel.image;
            if (image != null) {
                MediaManager.get().upload(image).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        getBinding().icSend.setVisibility(View.GONE);
                        getBinding().icLoading.setVisibility(View.VISIBLE);
                        getBinding().btnDownload.setEnabled(false);
                        getBinding().btnCancel.setEnabled(false);
                        getBinding().btnSend.setEnabled(false);
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        getBinding().icSend.setVisibility(View.VISIBLE);
                        getBinding().icLoading.setVisibility(View.GONE);
                        getBinding().btnDownload.setEnabled(true);
                        getBinding().btnCancel.setEnabled(true);
                        getBinding().btnSend.setEnabled(true);
                        String imageUrl = MediaManager.get().url().generate(resultData.get("public_id").toString());
                        String content = getBinding().edtPhotoContent.getText().toString();
                        Post post = new Post(content, new Date(), imageUrl, MyApplication.getUserId());
                        postRepository.createPost(post, friends, it -> {
                            if (it.isSuccessful()) {
                                Toast.makeText(requireContext(), "Upload success!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(requireContext(), "Upload fail!", Toast.LENGTH_LONG).show();
                            }
                            cameraViewModel.flipStatus();
                        });
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        getBinding().icSend.setVisibility(View.VISIBLE);
                        getBinding().icLoading.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "Upload fail!", Toast.LENGTH_LONG).show();
                        cameraViewModel.flipStatus();
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
        userRepository.userSnapshotListener(MyApplication.getUserId(), (doc, exception) -> {
            if (doc.getData() == null)
                return;

            ArrayList<String> temp = (ArrayList<String>) doc.getData().get("friends");
            friends.clear();
            friends.add(MyApplication.getUserId());
            if (temp != null)
                for (String item :
                        temp) {
                    if (item.split("_")[1].equals("friend"))
                        friends.add(item.split("_")[0]);
                }
        });
        newsfeedRepository.newsfeedSnapshotListener(MyApplication.getNewsfeed().getNewsfeedId(), (doc, exception) -> {
            Map<String, Object> mp = doc.getData();
            if (mp != null) {
                String newsfeedId = (String) mp.get("newsfeedId");
                String userId = (String) mp.get("userId");
                ArrayList<String> posts = (ArrayList<String>) mp.get("posts");
                Newsfeed newsfeed = new Newsfeed(newsfeedId, userId, posts);
                MyApplication.setNewsfeed(newsfeed);
            }
        });
    }

    @Override
    protected FragmentCameraBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentCameraBinding.inflate(inflater);
    }

    @Override
    public void onPause() {
        camera.stopPreview();
        super.onPause();
    }

    @Override
    public void onStop() {
        camera.stopPreview();
        super.onStop();
    }

    public void onStatusChange(boolean newStatus) {
        if (newStatus) {
            camera.startPreview();
            getBinding().clTakePhoto.setVisibility(View.VISIBLE);
            getBinding().lnHistoryNav.setVisibility(View.VISIBLE);
            getBinding().edtPhotoContent.setVisibility(View.GONE);
            getBinding().edtPhotoContent.setText("");
            getBinding().lnFriendChoose.setVisibility(View.GONE);
            getBinding().clSendPhoto.setVisibility(View.GONE);
        } else {
            getBinding().clTakePhoto.setVisibility(View.GONE);
            getBinding().lnHistoryNav.setVisibility(View.GONE);
            getBinding().edtPhotoContent.setVisibility(View.VISIBLE);
            getBinding().edtPhotoContent.requestFocus();
//            getBinding().lnFriendChoose.setVisibility(View.VISIBLE);
            getBinding().lnFriendChoose.setVisibility(View.GONE);
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
    }

    private void setFriendsChoose() {
        getBinding().rclFriendShare.setAdapter(friendChooseAdapter);
        friendChooseAdapter.setListFriendChooses(data);
    }

    public void setPreviewSize() {
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
        camera.setParameters(parameters);
    }

    public Camera getBackCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e("Camera Fragment", "Error opening camera: " + e.getMessage());
        }
        return camera;
    }

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
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraPD");

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

    private void savePhoto() {
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
    }
}