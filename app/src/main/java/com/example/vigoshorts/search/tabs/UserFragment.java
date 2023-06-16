package com.example.vigoshorts.search.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vigoshorts.R;

import java.util.ArrayList;

public class UserFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userAdapter = new UserAdapter();
        recyclerView.setAdapter(userAdapter);

        // Initialize the user list
        userList = new ArrayList<>();

        return view;
    }

    public void updateUserList(List<User> users) {
        // Update the user list with the new data
        userList.clear();
        userList.addAll(users);
        userAdapter.notifyDataSetChanged();
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_user, parent, false);
            return new UserViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = userList.get(position);

            // Set the user data to the views in the list item layout
            holder.textViewUserName.setText(user.getName());

            // Set any other user data to respective views

            // Handle item click if needed
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the click event for the user item
                    // You can perform any action you want here
                }
            });
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    private static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName;

        UserViewHolder(View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
        }
    }
}

