package com.example.instagramapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramapp.LoginActivity;
import com.example.instagramapp.Post;
import com.example.instagramapp.PostsAdapter;
import com.example.instagramapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private Button btnLogout;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        btnLogout = view.findViewById(R.id.btnLogout);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();

            }
        });

        rvPosts.setAdapter(adapter);

        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();

    }





    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
