package com.example.locketclone.ui.history;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locketclone.adpater.PostAdapter;
import com.example.locketclone.adpater.PostDetailAdapter;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentHistoryBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import carbon.view.View;

public class HistoryFragment extends BaseFragment<FragmentHistoryBinding> {

    private PostAdapter postAdapter;
    private PostDetailAdapter postDetailAdapter;

    private HistoryViewModel historyViewModel;

    private int currentPosition = 0;
    private float itemHeight = 0F;
    private float padding = 0F;
    private float firstItemHeight = 0F;
    private float allPixels = 0F;
    private int finalHeight = 0;

    private ArrayList<String> data = new ArrayList<>(
            Arrays.asList("A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"));

    @Override
    public void initData() {
        postAdapter = new PostAdapter(this);
        postDetailAdapter = new PostDetailAdapter(this);

        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    }

    @Override
    public void initView() {
        getBinding().recyclerListPost.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        getBinding().recyclerListPost.setAdapter(postAdapter);
        postAdapter.setListPosts(data);

        getRecyclerPostDetail();
    }

    @Override
    public void initEvent() {
        historyViewModel.currentPos.observe(getViewLifecycleOwner(), position -> {
            getBinding().recyclerListPost.scrollToPosition(position);
            getBinding().recyclerDetailPost.scrollToPosition(position);
            allPixels = position * itemHeight;
        });

        historyViewModel.status.observe(getViewLifecycleOwner(), status -> {
            if (status) {
                getBinding().recyclerListPost.setVisibility(View.VISIBLE);
                getBinding().recyclerDetailPost.setVisibility(View.GONE);
            } else {
                getBinding().recyclerListPost.setVisibility(View.GONE);
                getBinding().recyclerDetailPost.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected FragmentHistoryBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentHistoryBinding.inflate(inflater);
    }

    private void getRecyclerPostDetail() {
        RecyclerView recyclerDetail = getBinding().recyclerDetailPost;
        ViewTreeObserver vbo = recyclerDetail.getViewTreeObserver();
        vbo.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerDetail.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = recyclerDetail.getMeasuredHeight();
                itemHeight = recyclerDetail.getMeasuredHeight();
                padding = 0F;
                firstItemHeight = padding;
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
                                currentPosition = Math.round(((allPixels + padding - firstItemHeight) / itemHeight));
                                calculatePositionAndScroll(recyclerView);
                            }
                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        allPixels += (float) dy;
                        int expectedPosition = Math.round(((allPixels + padding - firstItemHeight) / itemHeight));
                        if (currentPosition != expectedPosition)
                            calculatePositionAndScroll(recyclerView);
                    }
                });

                recyclerDetail.setAdapter(postDetailAdapter);
                postDetailAdapter.setListPosts(data);
                return true;
            }
        });
    }

    private void calculatePositionAndScroll(RecyclerView recyclerView) {
        int expectedPosition = Math.round((allPixels + padding - firstItemHeight) / itemHeight);
        if (expectedPosition == -1) {
            expectedPosition = 0;
        } else if (expectedPosition >= recyclerView.getAdapter().getItemCount()) {
            expectedPosition--;
        }
        scrollListToPosition(recyclerView, expectedPosition);
    }

    private void scrollListToPosition(RecyclerView recyclerView, int expectedPosition) {
        float targetScrollPos = expectedPosition * itemHeight + firstItemHeight - padding;
        float missingPy = targetScrollPos - allPixels;
        if (missingPy != 0) {
            recyclerView.smoothScrollBy(0, (int) missingPy);
        }
    }
}