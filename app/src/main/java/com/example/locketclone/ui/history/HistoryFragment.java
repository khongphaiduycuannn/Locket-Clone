package com.example.locketclone.ui.history;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locketclone.MyApplication;
import com.example.locketclone.R;
import com.example.locketclone.adpater.PostAdapter;
import com.example.locketclone.adpater.PostDetailAdapter;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentHistoryBinding;
import com.example.locketclone.model.Newsfeed;

import java.util.ArrayList;
import java.util.Arrays;

import carbon.view.View;

public class HistoryFragment extends BaseFragment<FragmentHistoryBinding> {

    private PostAdapter postAdapter;
    private PostDetailAdapter postDetailAdapter;

    private HistoryViewModel historyViewModel;

    private Newsfeed newsfeed;

    private int currentPosition = 0;
    private float itemHeight = 0F;
    private float allPixels = 0F;

    private ArrayList<String> data = new ArrayList<>(
            Arrays.asList("A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"));

    @Override
    public void initData() {
        postAdapter = new PostAdapter(this);
        postDetailAdapter = new PostDetailAdapter(this);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    }

    @Override
    public void initView() {
        newsfeed = MyApplication.getNewsfeed();
        getBinding().recyclerListPost.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        getBinding().recyclerListPost.setAdapter(postAdapter);
        postAdapter.setListPosts(newsfeed.getPosts());

        getRecyclerPostDetail();
    }

    @Override
    public void initEvent() {
        getBinding().btnGalery.setOnClickListener(view -> {
            historyViewModel.flipStatus();
            historyViewModel.setCurrentPos(Math.round(allPixels / itemHeight));
        });

//        getBinding().toolbar.btnPostSetting.setOnClickListener(view -> {
//            showSettingDialog();
//        });

        getBinding().toolbar.btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });

        historyViewModel.currentPos.observe(getViewLifecycleOwner(), position -> {
            getBinding().recyclerListPost.scrollToPosition(position);
            getBinding().recyclerDetailPost.scrollToPosition(position);
            allPixels = position * itemHeight;
        });

        historyViewModel.status.observe(getViewLifecycleOwner(), this::onStatusChange);
    }

    @Override
    protected FragmentHistoryBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentHistoryBinding.inflate(inflater);
    }

    private void showSettingDialog() {
        Dialog settingDialog = new Dialog(requireContext());
        settingDialog.setContentView(R.layout.dialog_post_setting);

        Window window = settingDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }
        settingDialog.show();
    }

    private void onStatusChange(boolean newStatus) {
        AnimatorSet fadeIn = (AnimatorSet) AnimatorInflater.loadAnimator(
                requireContext(), R.animator.transition_fade_in
        );
        AnimatorSet fadeOut = (AnimatorSet) AnimatorInflater.loadAnimator(
                requireContext(), R.animator.transition_fade_out
        );

        RecyclerView recyclerListPost = getBinding().recyclerListPost;
        RecyclerView recyclerDetailPost = getBinding().recyclerDetailPost;

        if (newStatus) {
            fadeIn.setTarget(recyclerListPost);
            fadeOut.setTarget(recyclerDetailPost);
            recyclerListPost.setVisibility(View.VISIBLE);
            recyclerDetailPost.setVisibility(View.INVISIBLE);
            getBinding().bottomBar.setVisibility(View.INVISIBLE);
//            getBinding().toolbar.btnPostSetting.setVisibility(View.INVISIBLE);
        } else {
            fadeIn.setTarget(recyclerDetailPost);
            fadeOut.setTarget(recyclerListPost);
            recyclerListPost.setVisibility(View.INVISIBLE);
            recyclerDetailPost.setVisibility(View.VISIBLE);
            getBinding().bottomBar.setVisibility(View.VISIBLE);
//            getBinding().toolbar.btnPostSetting.setVisibility(View.VISIBLE);
        }
        fadeIn.start();
        fadeOut.start();
    }

    private void getRecyclerPostDetail() {
        RecyclerView recyclerDetail = getBinding().recyclerDetailPost;
        ViewTreeObserver vbo = recyclerDetail.getViewTreeObserver();
        vbo.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerDetail.getViewTreeObserver().removeOnPreDrawListener(this);
                itemHeight = recyclerDetail.getMeasuredHeight();
                allPixels = 0;

                LinearLayoutManager itemsLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext());
                itemsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerDetail.setLayoutManager(itemsLayoutManager);

                recyclerDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        synchronized (this) {
                            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                                currentPosition = Math.round(allPixels / itemHeight);
                                calculatePositionAndScroll(recyclerView);
                            }
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        allPixels += (float) dy;
                        int expectedPosition = Math.round(allPixels / itemHeight);
                        if (currentPosition != expectedPosition)
                            calculatePositionAndScroll(recyclerView);
                    }
                });

                recyclerDetail.setAdapter(postDetailAdapter);
                postDetailAdapter.setListPosts(newsfeed.getPosts());
                return true;
            }
        });
    }

    private void calculatePositionAndScroll(RecyclerView recyclerView) {
        int expectedPosition = Math.round(allPixels / itemHeight);
        if (expectedPosition == -1) {
            expectedPosition = 0;
        } else if (expectedPosition >= recyclerView.getAdapter().getItemCount()) {
            expectedPosition--;
        }
        scrollListToPosition(recyclerView, expectedPosition);
    }

    private void scrollListToPosition(RecyclerView recyclerView, int expectedPosition) {
        float targetScrollPos = expectedPosition * itemHeight;
        float missingPy = targetScrollPos - allPixels;
        if (missingPy != 0) {
            recyclerView.smoothScrollBy(0, (int) missingPy);
        }
    }
}