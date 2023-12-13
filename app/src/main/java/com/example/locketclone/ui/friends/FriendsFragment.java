package com.example.locketclone.ui.friends;

import android.view.LayoutInflater;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.locketclone.adpater.FriendAdapter;
import com.example.locketclone.adpater.FriendRequestAdapter;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentFriendsBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendsFragment extends BaseFragment<FragmentFriendsBinding> {

    private FriendAdapter friendAdapter;

    private FriendRequestAdapter friendRequestAdapter;

    private ArrayList<String> data = new ArrayList<>(
            Arrays.asList("A", "A", "A"));

    @Override
    public void initData() {
        friendAdapter = new FriendAdapter();
        friendRequestAdapter = new FriendRequestAdapter();
    }

    @Override
    public void initView() {
        getBinding().rclFriends.setLayoutManager(new LinearLayoutManager(requireContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        getBinding().rclFriends.setAdapter(friendAdapter);
        friendAdapter.setListFriends(data);

        getBinding().rclFriendRequests.setLayoutManager(new LinearLayoutManager(requireContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        getBinding().rclFriendRequests.setAdapter(friendRequestAdapter);
        friendRequestAdapter.setListFriendRequests(data);
    }

    @Override
    public void initEvent() {

    }

    @Override
    protected FragmentFriendsBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentFriendsBinding.inflate(inflater);
    }
}