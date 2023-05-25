package com.cookandroid.project_mobile.community;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cookandroid.project_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateCommunityActivity extends AppCompatActivity {

    private Uri selectedUri;
    private FirebaseStorage storage;
    private DatabaseReference articleDB;
    private static final String DB_COMMUNITY = "Community";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        storage = FirebaseStorage.getInstance();
        articleDB = FirebaseDatabase.getInstance().getReference().child(DB_COMMUNITY);

        findViewById(R.id.imageAddButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(CreateCommunityActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(CreateCommunityActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    startContentProvider();
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showPermissionContextPopup();
                } else {
                    ActivityCompat.requestPermissions(CreateCommunityActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, 1010);
                }
            }
        });

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText) findViewById(R.id.titleEditText)).getText().toString();
                String content = ((EditText) findViewById(R.id.contentEditText)).getText().toString();
                long key = System.currentTimeMillis();

                showProgress();

                if (selectedUri != null) {
                    uploadPhoto(selectedUri, new SuccessHandler() {
                        @Override
                        public void onSuccess(String uri) {
                            uploadCommunity(title, content, uri, key);
                        }
                    }, new ErrorHandler() {
                        @Override
                        public void onError() {
                            Toast.makeText(CreateCommunityActivity.this, "사진 업로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            hideProgress();
                        }
                    });
                } else {
                    uploadCommunity(title, content, "", key);
                }
            }
        });
    }

    private void uploadPhoto(Uri uri, final SuccessHandler successHandler, final ErrorHandler errorHandler) {
        final String fileName = System.currentTimeMillis() + ".png";
        StorageReference storageRef = storage.getReference().child("noticeBoard/photo").child(fileName);
        UploadTask uploadTask = storageRef.putFile(uri);

        uploadTask.addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            successHandler.onSuccess(uri.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            errorHandler.onError();
                        }
                    });
                } else {
                    errorHandler.onError();
                }
            }
        });
    }

    private void uploadCommunity(String title, String content, String imageUri, long key) {
        CommunityData model = new CommunityData(title, content, imageUri, key);
        articleDB.push().setValue(model);

        hideProgress();
        finish();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1010) {
            if (grantResults.length > 0) {
                startContentProvider();
            } else {
                Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == 2020) {
            Uri uri = data.getData();
            if (uri != null) {
                ImageView imageView = findViewById(R.id.photoImageView);
                imageView.setImageURI(uri);
                selectedUri = uri;
            } else {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress() {
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    private void startContentProvider() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 2020);
    }

    private void showPermissionContextPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("권한이 필요합니다.");
        builder.setMessage("사진을 가져오기 위해 필요합니다.");
        builder.setPositiveButton("동의", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(CreateCommunityActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1010);
            }
        });
        builder.create().show();
    }

    private interface SuccessHandler {
        void onSuccess(String uri);
    }

    private interface ErrorHandler {
        void onError();
    }
}
