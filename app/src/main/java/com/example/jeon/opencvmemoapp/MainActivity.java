package com.example.jeon.opencvmemoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GET_IMAGE_PATH = 3;
    private static final int ADD_CARD_VIEW = 4;
    private static final int GET_AUTH_INFORM = 5;
    public static final int sub = 1001;
    private static final int MY_PERMISSION_STORAGE = 1111;
    List<Button> btnList = new ArrayList<>();
    final List<Item> itemList = new ArrayList<>();
    Button camBtn = null;
    ImageView Photo = null;
    private String imagePath = null;
    private Item tempItem;
    private Bitmap thisBitmap;
    private RecyclerView recyclerView;
    private Intent authIntent;
    private FirebaseAuth finalAuth;
    private GoogleSignInAccount currentUser;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        authIntent = new Intent(getApplicationContext(), getAuth.class);;
//        startActivityForResult(authIntent, GET_AUTH_INFORM);
        setContentView(R.layout.activity_main);

//        authIntent = getIntent();
//        currentUser = (GoogleSignInAccount) authIntent.getSerializableExtra("User");
        finalAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        btnList.add((Button)findViewById(R.id.makeList));
        btnList.add((Button)findViewById(R.id.deleteList));
        btnList.add((Button)findViewById(R.id.share));

        for(int i=0; i<itemList.size(); i++){
            items.add(itemList.get(i));
        }
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
                Intent intent = new Intent(getApplicationContext(), viewContents.class);
                startActivity(intent);
            }
        });
        Button btnNewActivity4 = (Button)findViewById(R.id.share);
        btnNewActivity4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), viewContents.class);
//                startActivity(intent);
                finalAuth.signOut();
                Toast.makeText(MainActivity.this, "로그인 계정을 다시 선택해 주세요", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), itemList, R.layout.activity_main));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
        if (requestCode == GET_IMAGE_PATH) {
            tempItem = new Item(data.getStringExtra("Path"), data.getStringExtra("Title"), data.getStringExtra("resultText"));
            itemList.add(tempItem);
            recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), itemList, R.layout.activity_main));

            Uri file = Uri.fromFile(new File(tempItem.getImagePath()));
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            final StorageReference riversRef = storageRef.child(finalAuth.getUid()+"/" + timeStamp +".jpg");
            UploadTask uploadTask = riversRef.putFile(file);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return riversRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String downloadURL = downloadUri.toString();
                    } else {
                        // Handle failures
                        // ...
                        Toast.makeText(MainActivity.this, "업로드 실 패!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
//
//            riversRef.putFile(file)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            // Get a URL to the uploaded content
////                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle unsuccessful uploads
//                            // ...
//                        }
//                    });
        }
    }

    public Bitmap getBitmapFromItem(Item thisItem){
        Bitmap thisBit = null;
        thisBit = DecodeBitmapFile(thisItem.getImagePath());

        return thisBit;
    }

    private Bitmap DecodeBitmapFile(String strFilePath) {
        final int IMAGE_MAX_SIZE = 1024;
        File file = new File(strFilePath);
        Bitmap rotatedBitmap = null;

        if (file.exists() == false) {
            return null;
        }
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strFilePath, bfo);

        if (bfo.outHeight * bfo.outWidth >= IMAGE_MAX_SIZE * IMAGE_MAX_SIZE) {
            bfo.inSampleSize = (int) Math.pow(2,
                    (int) Math.round(Math.log(IMAGE_MAX_SIZE
                            / (double) Math.max(bfo.outHeight, bfo.outWidth))
                            / Math.log(0.5)));
        }
        bfo.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(strFilePath, bfo);

        if (bitmap != null) {
            ExifInterface ei = null;
            try {
                ei = new ExifInterface(strFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
