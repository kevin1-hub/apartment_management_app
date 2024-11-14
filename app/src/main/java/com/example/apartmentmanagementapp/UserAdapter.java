package com.example.apartmentmanagementapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.firstName.setText(user.getFirstName());
        holder.lastName.setText(user.getLastName());
        holder.email.setText(user.getEmail());
        holder.role.setText(user.getRole());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Change UserViewHolder to public or leave it private if only used here
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView firstName, lastName, email, role;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.userFirstName);
            lastName = itemView.findViewById(R.id.userLastName);
            email = itemView.findViewById(R.id.userEmail);
            role = itemView.findViewById(R.id.userRole);
        }
    }
}
