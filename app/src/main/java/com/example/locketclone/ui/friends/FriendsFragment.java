package com.example.locketclone.ui.friends;

import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.locketclone.MyApplication;
import com.example.locketclone.adpater.FriendAdapter;
import com.example.locketclone.adpater.FriendRequestAdapter;
import com.example.locketclone.base.BaseFragment;
import com.example.locketclone.databinding.FragmentFriendsBinding;
import com.example.locketclone.model.User;
import com.example.locketclone.repository.UserRepository;

import java.util.ArrayList;
import java.util.Map;

public class FriendsFragment extends BaseFragment<FragmentFriendsBinding> {

    private FriendAdapter friendAdapter;

    private FriendRequestAdapter friendRequestAdapter;

    private UserRepository userRepository = new UserRepository();

    private ArrayList<String> data = MyApplication.getUser().getFriends();

    private ArrayList<String> friend = new ArrayList<>();

    private ArrayList<String> friendRequest = new ArrayList<>();

    @Override
    public void initData() {
        friendAdapter = new FriendAdapter() {
            @Override
            public void onClickItem(String userRemoveId) {
                onFriendRemoveClick(userRemoveId);
            }
        };
        friendRequestAdapter = new FriendRequestAdapter() {
            @Override
            public void onClickItem(String userRequestId) {
                onFriendAcceptClick(userRequestId);
            }
        };
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

        getBinding().rclFriendRequests.setLayoutManager(new LinearLayoutManager(requireContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        getBinding().rclFriendRequests.setAdapter(friendRequestAdapter);

        setData();

        String count = friend.size() + " friends added";
        getBinding().txtFriendCount.setText(count);
    }

    @Override
    public void initEvent() {
        getBinding().btnBack.setOnClickListener(view -> {
            Navigation.findNavController(getView()).popBackStack();
        });

        userRepository.userSnapshotListener(MyApplication.getUserId(), (doc, exception) -> {
            if (doc.getData() == null)
                return;

            data = (ArrayList<String>) doc.getData().get("friends");
            setData();
        });
    }

    @Override
    protected FragmentFriendsBinding inflateViewBinding(LayoutInflater inflater) {
        return FragmentFriendsBinding.inflate(inflater);
    }

    private void setData() {
        for (String item :
                data) {
            if (item.split("_")[1].equals("friend") && !friend.contains(item.split("_")[0]))
                friend.add(item.split("_")[0]);
            else if (item.split("_")[1].equals("pending") && !friendRequest.contains(item.split("_")[0]))
                friendRequest.add(item.split("_")[0]);
        }
        friendAdapter.setListFriends(friend);
        friendRequestAdapter.setListFriendRequests(friendRequest);
    }

    private void onFriendRemoveClick(String userRemoveId) {
        int index = friend.indexOf(userRemoveId);
        int index1 = data.indexOf(userRemoveId + "_friend");
        if (index != -1) {
            friend.remove(index);
            if (index1 != -1)
                data.remove(index1);

            friendAdapter.setListFriends(friend);

            User currentUser = MyApplication.getUser();
            currentUser.setFriends(data);
            MyApplication.setUser(currentUser);

            userRepository.updateUser(currentUser, it -> {
                if (it.isSuccessful()) {
                    String count = friend.size() + " friends added";
                    getBinding().txtFriendCount.setText(count);
                    Toast.makeText(requireContext(), "Remove success!", Toast.LENGTH_LONG).show();
                }
            });

            userRepository.getUserById(userRemoveId, it -> {
                Map<String, Object> data = it.getData();
                if (data != null) {
                    ArrayList<String> listFriend = (ArrayList<String>) it.getData().get("friends");
                    if (listFriend != null) {
                        int index2 = listFriend.indexOf(currentUser.getUserId() + "_friend");
                        if (index2 != -1)
                            listFriend.remove(index2);
                        data.put("friends", listFriend);
                        userRepository.updateUser(userRemoveId, data, it1 -> {
                        });
                    }
                }
            });
        }
    }

    private void onFriendAcceptClick(String userRequestId) {
        int index = friendRequest.indexOf(userRequestId);
        if (index != -1) {
            friendRequest.remove(index);
            friend.add(userRequestId);
            for (int i = 0; i < data.size(); i++)
                if (data.get(i).split("_")[0].equals(userRequestId))
                    data.set(i, userRequestId + "_friend");
            friendAdapter.setListFriends(friend);
            friendRequestAdapter.setListFriendRequests(friendRequest);

            User currentUser = MyApplication.getUser();
            currentUser.setFriends(data);
            MyApplication.setUser(currentUser);

            userRepository.updateUser(currentUser, it -> {
                if (it.isSuccessful()) {
                    String count = friend.size() + " friends added";
                    getBinding().txtFriendCount.setText(count);
                    Toast.makeText(requireContext(), "Accept success!", Toast.LENGTH_LONG).show();
                }
            });

            userRepository.getUserById(userRequestId, it -> {
                Map<String, Object> data = it.getData();
                if (data != null) {
                    ArrayList<String> listFriend = (ArrayList<String>) it.getData().get("friends");
                    if (listFriend != null) {
                        for (int i = 0; i < listFriend.size(); i++)
                            if (listFriend.get(i).split("_")[0].equals(currentUser.getUserId()))
                                listFriend.set(i, currentUser.getUserId() + "_friend");
                        data.put("friends", listFriend);
                        userRepository.updateUser(userRequestId, data, it1 -> {
                        });
                    }
                }
            });
        }
    }
}