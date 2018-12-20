package com.example.jeon.opencvmemoapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class getContents extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.get_contents);
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//}

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask<UploadTask.TaskSnapshot> mUploadTask;
    private Uri mImageUri;
    private FirebaseAuth finalAuth;
    private FirebaseStorage storage;







    private Button mBtnCameraView;
    private EditText mEditOcrResult;
    private String datapath = "";
    private String lang = "";
    private TessBaseAPI mTess;
    private Bitmap resultImage;
    private int ACTIVITY_REQUEST_CODE = 1;
    private EditText openCVText;
    private EditText titleText;
    private Button returnButton;

    static TessBaseAPI sTess;
    static String imagePath;
    boolean flag = false;
    public String getIntentData(){
        String path;
        Intent  intent = getIntent();
        path = intent.getStringExtra("Path");

        return path;
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
                ei = new ExifInterface(imagePath);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_contents);

        storage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("images");
        mDatabaseRef.keepSynced(true);
        finalAuth = FirebaseAuth.getInstance();

        openCVText = (EditText) findViewById(R.id.viewText);
        titleText = (EditText) findViewById(R.id.getTitle);
        returnButton = (Button) findViewById(R.id.takeContentsButton);

        imagePath = getIntentData();
        mImageUri = Uri.parse(imagePath);
        resultImage = DecodeBitmapFile(imagePath);

        ImageView iv = (ImageView) findViewById(R.id.openCVIv);
        iv.setImageBitmap(resultImage);

//        datapath = getFilesDir()+ "/tesseract/";
//        checkFile(new File(datapath + "tessdata/"));

//        String lang = "kor";
//        mTess = new TessBaseAPI();
//        mTess.init(datapath, lang);

        // 뷰 선언
//        mBtnCameraView = (Button) findViewById(R.id.btn_camera);
        mEditOcrResult = (EditText) findViewById(R.id.getTitle);
        sTess = new TessBaseAPI();


        // Tesseract 인식 언어를 한국어로 설정 및 초기화
        lang = "kor";
        datapath = getFilesDir()+ "/tesseract";

        if(checkFile(new File(datapath+"/tessdata")))
        {
            sTess.init(datapath, lang);
        }



        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("Path", imagePath);
                data.putExtra("Title", titleText.getText().toString());
                data.putExtra("resultText", openCVText.getText().toString());
//                setResult(RESULT_OK, data);
                if (getParent() == null) {
                    setResult(Activity.RESULT_OK, data);
                }
                else {
                    getParent().setResult(Activity.RESULT_OK, data);
                }



                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContents.this, "업로드 진행중", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }





                finish();
            }
        });

        if(flag == false) {
            titleText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String OCRresult = null;
                    sTess.setImage(resultImage);
                    OCRresult = sTess.getUTF8Text();
                    TextView OCRTextView = (TextView) findViewById(R.id.viewText);
//                ImageView iv = (ImageView) findViewById(R.id.openCVIv);
//                iv.setImageBitmap(resultImage);
                    OCRTextView.setText(OCRresult);
                    flag = true;
//        OCRTextView.setText(imagePath);
                }
            });

        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        mStorageRef = storage.getReference();
        if (imagePath != null) {
            Uri file = Uri.fromFile(new File(imagePath));
            final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            final StorageReference fileReference = mStorageRef.child(finalAuth.getCurrentUser().getEmail() + "/" + timeStamp + ".jpg");

            mUploadTask = fileReference.putFile(file);
            Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) throw task.getException();

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String downloadURL = downloadUri.toString();
                        ImageDTO imageDTO = new ImageDTO(downloadURL, titleText.getText().toString(), openCVText.getText().toString(), finalAuth.getCurrentUser().getUid());
                        mDatabaseRef.push().setValue(imageDTO);
                        Toast.makeText(getContents.this, "storage, database 저장 완료.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContents.this, "업로드 실 패!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void openImagesActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    boolean checkFile(File dir)
    {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if(!dir.exists() && dir.mkdirs()) {
            copyFiles();
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if(dir.exists()) {
            String datafilepath = datapath + "/tessdata/" + lang + ".traineddata";
            File datafile = new File(datafilepath);
            if(!datafile.exists()) {
                copyFiles();
            }
        }
        return true;
    }

    void copyFiles()
    {
        AssetManager assetMgr = this.getAssets();

        InputStream is = null;
        OutputStream os = null;

        try {
            is = assetMgr.open("tessdata/"+lang+".traineddata");

            String destFile = datapath + "/tessdata/" + lang + ".traineddata";

            os = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            is.close();
            os.flush();
            os.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processImage(View view){
        String OCRresult = null;
        mTess.setImage(resultImage);
        OCRresult = mTess.getUTF8Text();
        TextView OCRTextView = (TextView) findViewById(R.id.viewText);
        ImageView iv = (ImageView) findViewById(R.id.openCVIv);
        iv.setImageBitmap(resultImage);
        OCRTextView.setText(OCRresult);
//        OCRTextView.setText(imagePath);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode==RESULT_OK)
//        {
//            if(requestCode== ACTIVITY_REQUEST_CODE)
//            {
//                // 받아온 OCR 결과 출력
//                mEditOcrResult.setText(data.getStringExtra("STRING_OCR_RESULT"));
//            }
//        }
    }
}