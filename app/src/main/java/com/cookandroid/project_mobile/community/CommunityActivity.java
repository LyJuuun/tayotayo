package com.cookandroid.project_mobile.community;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.project_mobile.DBKey;
import com.cookandroid.project_mobile.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CommunityActivity extends AppCompatActivity {

    private DatabaseReference communityDB;
    private List<CommunityData> communityList;
    private CommunityAdapter communityAdapter;

    private ChildEventListener listener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            CommunityData communityModel = snapshot.getValue(CommunityData.class);
            if (communityModel == null){
                return;
            }

            communityList.add(communityModel);
            communityAdapter.submitList(communityList);
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
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //액티비티의 앱바(App Bar)로 지정
        ActionBar actionBar = getSupportActionBar(); //앱바 제어를 위해 툴바 액세스
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("설정하셈"); // 툴바 제목 설정
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // 앱바에 뒤로가기 버튼 만들기
        }

        FirebaseApp.initializeApp(this);

        communityList = new ArrayList<>(); //초기화
        communityDB = FirebaseDatabase.getInstance().getReference().child(DBKey.DB_COMMUNITY);

        communityAdapter = new CommunityAdapter(CommunityData -> {
            Intent intent = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
            intent.putExtra("chatKey", CommunityData.getKey());
            intent.putExtra("title", CommunityData.getText());
            intent.putExtra("content", CommunityData.getContent());
            startActivity(intent);
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        Button createCommunityButton = findViewById(R.id.createCommunityButton);
        createCommunityButton.setOnClickListener(view -> {
            Intent intent = new Intent(CommunityActivity.this, CreateCommunityActivity.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.communityRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(communityAdapter);

        communityDB.addChildEventListener(listener);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //Intent it = new Intent(getApplicationContext(), StartActivity.class);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        communityDB.removeEventListener(listener);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        communityAdapter.notifyDataSetChanged();
    }
}

