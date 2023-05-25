package com.cookandroid.project_mobile.community;

import static com.cookandroid.project_mobile.DBKey.DB_CHAT;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.project_mobile.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommunityDetailActivity extends AppCompatActivity {

    private List<CommunityDetailData> chatList = new ArrayList<>();
    private CommunityDetailAdapter adapter = new CommunityDetailAdapter();
    private DatabaseReference chatDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);

        FirebaseApp.initializeApp(this);

        long chatKey = getIntent().getLongExtra("chatKey", -1);
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView titleTextView = findViewById(R.id.communityDetailTitle);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView contentTextView = findViewById(R.id.communityDetailContent);
        RecyclerView recyclerView = findViewById(R.id.chatRecyclerview);
        EditText chatEditText = findViewById(R.id.chatEditText);
        Button chatButton = findViewById(R.id.sendButton);

        titleTextView.setText(title);
        contentTextView.setText(content);

        chatDB = FirebaseDatabase.getInstance().getReference().child(DB_CHAT).child(String.valueOf(chatKey));

        chatDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CommunityDetailData chatItem = snapshot.getValue(CommunityDetailData.class);
                if (chatItem != null) {
                    chatList.add(chatItem);
                    adapter.submitList(chatList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatEditText.getText().toString();
                chatEditText.setText(null);

                CommunityDetailData chatItem = new CommunityDetailData();
                chatItem.setMessage(message);

                chatDB.push().setValue(chatItem);
            }
        });

    }
}
