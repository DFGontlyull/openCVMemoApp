//package com.example.jeon.opencvmemoapp;
////
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//import java.io.InputStream;
//
////
////
//
////
//class getAlbum extends AppCompatActivity {
//
//    ImageView imageView;
//    Button button;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        imageView = (ImageView)findViewById(R.id.image);
//
//        button = (Button)findViewById(R.id.button1);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent, 1);
//            }
//        });
//    }
//
//    private String getRealPathFromURI(Uri contentURI) {
//
//        String result;
//        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
//
//        if (cursor == null) { // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//
//        return result;
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // Check which request we're responding to
//        if (requestCode == 1) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                try {
//                    // 선택한 이미지에서 비트맵 생성
//                    InputStream in = getContentResolver().openInputStream(data.getData());
//                    Bitmap img = BitmapFactory.decodeStream(in);
//                    in.close();
//                    // 이미지 표시
//                    imageView.setImageBitmap(img);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//}