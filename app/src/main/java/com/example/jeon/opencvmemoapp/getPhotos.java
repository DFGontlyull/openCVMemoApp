package com.example.jeon.opencvmemoapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getPhotos extends Activity implements View.OnClickListener {
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int GET_CONTENTS_FROM_TEXTVIEW = 3;

    private Uri mImageCaptureUri;
    private ImageView mPhotoImageView;
    private Button mButton;
    private Button okButton;
    private ImageView cropView;
    private Activity mainActivity = this;
//    String mCurrentPhotoPath;
    private String selectedImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_view);
        mButton = (Button) findViewById(R.id.takeAPicture);
        okButton = (Button)findViewById(R.id.okbutton);
        mPhotoImageView = (ImageView) findViewById(R.id.photo);

        mButton.setOnClickListener(this);
        try {
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent data = new Intent();
                        data.putExtra("Path", selectedImagePath);
                        setResult(RESULT_OK, data);
//                        moveTaskToBack(true);
                        // 여기까지는 호출액티비티에 리턴해주는 코드..
                        Intent intent = new Intent(getApplicationContext(), getPhotos.class);
                        startActivityForResult(intent, GET_CONTENTS_FROM_TEXTVIEW);
                        finish();
                    }
                });
        }catch (Exception e){
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        selectedImagePath = image.getAbsolutePath();
        return image;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    private void doTakePhotoAction(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.jeon.opencvmemoapp.FileProvider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
            }
        }
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//            // 임시로 사용할 파일의 경로를 생성
//            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//            mImageCaptureUri = FileProvider.getUriForFile(this, "com.example.jeon.opencvmemoapp.FileProvider", new File(Environment.getExternalStorageDirectory(), url));
//            // mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
//
//            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//            // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
//            //intent.putExtra("return-data", true);
//            startActivityForResult(intent, PICK_FROM_CAMERA);
//




//        btn = (Button)findViewById(R.id.takeAPicture);
//        iv = (ImageView)findViewById(R.id.photo);
//
//        btn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,1);
//            }
//        });
    }

    private void doTakeAlbumAction()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), PICK_FROM_ALBUM);

//        // 앨범 호출
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult(intent, PICK_FROM_ALBUM);

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//            // 임시로 사용할 파일의 경로를 생성
//            String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
//            mImageCaptureUri = FileProvider.getUriForFile(this, "com.example.jeon.opencvmemoapp.FileProvider", new File(Environment.getExternalStorageDirectory(), url));
//            // mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
//
//            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//            // 특정기기에서 사진을 저장못하는 문제가 있어 다음을 주석처리 합니다.
//            //intent.putExtra("return-data", true);
//            startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public String getPath(Uri uri) {
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor cursor = managedQuery(uri, projection, null, null, null);
//        startManagingCursor(cursor);
//        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//
//        return cursor.getString(columnIndex);

        String wholeID = DocumentsContract.getDocumentId(uri);
        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);
        String filePath = "";
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK)
        {
            return;
        }

        switch(requestCode)
        {
            case CROP_FROM_CAMERA:
            {
                // 크롭이 된 이후의 이미지를 넘겨 받습니다.
                // 이미지뷰에 이미지를 보여준다거나 부가적인 작업 이후에
                // 임시 파일을 삭제합니다.
                final Bundle extras = data.getExtras();

                if(extras != null)
                {
                    Bitmap photo = extras.getParcelable("data");
                    mPhotoImageView.setImageBitmap(photo);
                }

                // 임시 파일 삭제
                File f = new File(mImageCaptureUri.getPath());
                if(f.exists())
                {
                    f.delete();
                }

                break;
            }

            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();
                selectedImagePath = getPath(mImageCaptureUri);

//               mPhotoImageView.setImageURI(mImageCaptureUri);
                try {
                    // 비트맵 이미지로 가져온다
//                    String imagePath = mImageCaptureUri.getPath();
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);

                    // 이미지를 상황에 맞게 회전시킨다
                    if (bitmap != null) {
                        ExifInterface ei = null;
                        try {
                            ei = new ExifInterface(selectedImagePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap rotatedBitmap = null;
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

                        // 변환된 이미지 사용
                        mPhotoImageView.setImageBitmap(rotatedBitmap);
                    }
                } catch(Exception e) {
                    Toast.makeText(this, "오류발생: " + e.getLocalizedMessage(),
                            Toast.LENGTH_LONG).show();
                }

                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

//                Intent intent = new Intent("com.android.camera.action.CROP");
//                intent.setDataAndType(mImageCaptureUri, "image/*");
//                                startActivityForResult(intent, CROP_FROM_CAMERA);
//                intent.putExtra("outputX", 90);
//                intent.putExtra("outputY", 90);
//                intent.putExtra("aspectX", 1);
//                intent.putExtra("aspectY", 1);
//                intent.putExtra("scale", true);
//                intent.putExtra("return-data", true);

                break;
            }

            case PICK_FROM_CAMERA:
            {
                if (resultCode == RESULT_OK) {
                    File file = new File(selectedImagePath);
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bitmap != null) {
                        ExifInterface ei = null;
                        try {
                            ei = new ExifInterface(selectedImagePath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_UNDEFINED);

                        Bitmap rotatedBitmap = null;
                        switch(orientation) {

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

                        mPhotoImageView.setImageBitmap(rotatedBitmap);
                    }
                }
                break;
            }
        }





//        super.onActivityResult(requestCode, resultCode, data);
//        // TODO Auto-generated method stub
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode!=0){
//            if(requestCode==1&&!data.equals(null)){
//                try{
//                    profileBitmap = (Bitmap)data.getExtras().get("data");
//                    iv.setImageBitmap(profileBitmap);
//                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
//                } catch(Exception e){
//                    return;
//                }
//            }
//        }
    }

    @Override
    public void onClick(View v){
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
                    int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(permissionCamera == PackageManager.PERMISSION_DENIED|| permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CAMERA}, 0);
                    } else {
//                        resultText.setText("camera/write permission authorized");
                        doTakePhotoAction();
                    }
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
            {

//                        btnNewActivity2.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getApplicationContext(), getPhotos.class);
//                    startActivity(intent);
//                }
//            });
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
                    int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if(permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
//                        resultText.setText("read/write storage permission authorized");
                        doTakeAlbumAction();

                    }
                }
            };

            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
//                    resultText.setText("취소 누름");
                    dialog.dismiss();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("업로드할 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .setNegativeButton("취소", cancelListener)
                    .show();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==0){
//            if (grantResults[0] == 0) {
//                Toast.makeText(this, "카메라 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(this, "카메라 권한이 거절 되었습니다. 카메라 이용을 원할시 권한을 승인하여야 합니다.", Toast.LENGTH_SHORT).show();;
//            }
//        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.CAMERA)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
//                            resultText.setText("camera/write permission authorized");
                            Toast.makeText(this, "카메라/쓰기 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "카메라/쓰기 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case 1:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "읽기/쓰기 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "읽기/쓰기 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }
}