package com.example.jeon.opencvmemoapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int GET_IMAGE_PATH = 3;

    List<Button> btnList = new ArrayList<>();
    final List<Item> itemList = new ArrayList<>();
    private Item tempItem;
    private RecyclerView recyclerView;
    private Intent authIntent;
    private FirebaseAuth finalAuth;
    private GoogleSignInAccount currentAccount;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private DatabaseReference database;
    private List<ImageDTO> ImageDTOs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        authIntent = new Intent(getApplicationContext(), getAuth.class);;
//        startActivityForResult(authIntent, GET_AUTH_INFORM);
        setContentView(R.layout.activity_main);

        authIntent = getIntent();
        currentAccount = (GoogleSignInAccount) authIntent.getSerializableExtra("User");
        finalAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("images");
        database.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        final List<Item> items = new ArrayList<>();
        btnList.add((Button)findViewById(R.id.makeList));
        btnList.add((Button)findViewById(R.id.deleteList));
        btnList.add((Button)findViewById(R.id.share));

        for(int i=0; i<itemList.size(); i++){
            items.add(itemList.get(i));
        }

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ImageDTOs.clear();
                for(DataSnapshot snapshot :  dataSnapshot.getChildren()){
//                    Log.v("imageUrl", snapshot.getValue(String.class));
                    ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                    ImageDTOs.add(imageDTO);

                    Toast.makeText(getApplicationContext(), " 테스트2", Toast.LENGTH_SHORT).show();
                }
                recyclerView.setAdapter(new RecyclerAdapter_DTO(getApplicationContext(), ImageDTOs, R.layout.activity_main));
//                        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_main));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), " 테스트3", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnNewActivity2 = (Button)findViewById(R.id.makeList);
        btnNewActivity2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), getPhotos.class);
                startActivityForResult(intent, GET_IMAGE_PATH);
//                if(tempItem!=null) {
//                    thisBitmap = getBitmapFromItem(tempItem);
//                }
            }
        });
        Button btnNewActivity3 = (Button)findViewById(R.id.deleteList);
        btnNewActivity3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), " 테스트1", Toast.LENGTH_SHORT).show();
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ImageDTOs.clear();
                        for(DataSnapshot snapshot :  dataSnapshot.getChildren()){
                            Log.v("time", snapshot.getValue(String.class));
                            ImageDTO imageDTO = snapshot.getValue(ImageDTO.class);
                            if(imageDTO.getUid() == finalAuth.getCurrentUser().getUid()) {
                                ImageDTOs.add(imageDTO);
                            }
                        }
                        recyclerView.setAdapter(new RecyclerAdapter_DTO(getApplicationContext(), ImageDTOs, R.layout.activity_main));
//                        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_main));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });

        Button btnNewActivity4 = (Button)findViewById(R.id.share);
        btnNewActivity4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finalAuth.signOut();
                Toast.makeText(MainActivity.this, "로그인 계정을 다시 선택해 주세요", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        recyclerView.setAdapter(new RecyclerAdapter_DTO(getApplicationContext(), ImageDTOs, R.layout.activity_main));
//        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), itemList, R.layout.activity_main));
    }

//    public void upload(Item item) {
//        storageRef = storage.getReference();
//        if (item.getImagePath() != null) {
//            Uri file = Uri.fromFile(new File(item.getImagePath()));
//            final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            final StorageReference riversRef = storageRef.child(finalAuth.getCurrentUser().getEmail() + "/" + timeStamp + ".jpg");
////            final StorageReference riversRef = storageRef.child(finalAuth.getCurrentUser().getEmail() + file.getLastPathSegment());
//            UploadTask uploadTask = riversRef.putFile(file);
//
//            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful()) {
//                        throw task.getException();
//                    }
//
//                    // Continue with the task to get the download URL
//                    return riversRef.getDownloadUrl();
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()) {
//                        Uri downloadUri = task.getResult();
//                        String downloadURL = downloadUri.toString();
//                        ImageDTO imageDTO = new ImageDTO(downloadURL, tempItem.getTitle(), tempItem.getContent(), finalAuth.getCurrentUser().getUid(), finalAuth.getCurrentUser().getEmail(), timeStamp);
//                        ImageDTOs.add(imageDTO);
//                        database.push().setValue(imageDTO);
//                        recyclerView.setAdapter(new RecyclerAdapter_DTO(getApplicationContext(), ImageDTOs, R.layout.activity_main));
//
//                        Toast.makeText(MainActivity.this, "storage, database 저장 완료.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(MainActivity.this, "업로드 실 패!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
        if (requestCode == GET_IMAGE_PATH) {
            tempItem = new Item(data.getStringExtra("Path"), data.getStringExtra("Title"), data.getStringExtra("resultText"));
//            itemList.add(tempItem);
//            recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), itemList, R.layout.activity_main));
//            upload(tempItem);
//            recyclerView.setAdapter(new RecyclerAdapter_DTO(getApplicationContext(), ImageDTOs, R.layout.activity_main));
        }
    }
}
