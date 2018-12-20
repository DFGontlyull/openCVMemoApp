package com.example.jeon.opencvmemoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class viewMemo extends Activity {
    private PopupWindow mPopupWindow ;
    private Intent viewContentsIntent;
    private ImageDTO viewContentsItem;
    private EditText mainedittext;
    private ImageView mainimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_memo_page);

        viewContentsIntent = getIntent();
        viewContentsItem = (ImageDTO) viewContentsIntent.getSerializableExtra("item");

        mainedittext = (EditText)findViewById(R.id.main_edit);
        mainedittext.setText(viewContentsItem.getContents());
        mainimage = (ImageView)findViewById(R.id.main_image);
        Bitmap tempBitmap = null;
        new LoadImage(mainimage).execute(viewContentsItem.getImageUrl());
//            mainimage.setBackground(drawableFromUrl(viewContentsItem.getImageUrl()));
//            tempBitmap = DecodeBitmapFile(drawableFromUrl(viewContentsItem.getImageUrl()));
        //        Drawable drawable = ContextCompat.getDrawable(context, item.getImagePath());
//        Drawable drawable = new BitmapDrawable(tempBitmap);
//        mainimage.setImageBitmap(tempBitmap);

//        Button popup = (Button) findViewById(R.id.view_image);
//        popup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertadd = new AlertDialog.Builder(viewMemo.this);
//                LayoutInflater factory = LayoutInflater.from(viewMemo.this);
//                final View view = factory.inflate(R.layout.image_popup, null);
//                alertadd.setView(view);
//                alertadd.setNeutralButton("Here!", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dlg, int sumthin) {
//
//                    }
//                });
//
//                alertadd.show();
//                View popupView = getLayoutInflater().inflate(R.layout.image_popup, null);
//
//                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
//
//                mPopupWindow.setFocusable(true);
//                // 외부 영역 선택히 PopUp 종료
//
//                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
//
//                popupView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mPopupWindow.dismiss();
//                    }
//                });
//
//                Button cancel = (Button) popupView.findViewById(R.id.close_popup_button);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mPopupWindow.dismiss();
//                    }
//                });
//    }
//});

//
//// supprot Uri for image
//                imagePopup.initiatePopupWithPicasso(viewContentsItem.getImagePath());
//
//// supprot File for image
//                imagePopup.initiatePopupWithPicasso(viewContentsItem.getImagePath());




//        Button opencv = (Button) findViewById(R.id.re_opencv);
//        opencv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//
//        });
//
//        Button selectImage = (Button) findViewById(R.id.edit_image);
//        selectImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//
//        });
    }

    private Bitmap DecodeBitmapFile(String strFilePath) {
        final int IMAGE_MAX_SIZE = 1024;
        File file = new File(strFilePath);

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
        Bitmap rotatedBitmap = null;
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

    private Drawable drawableFromUrl(String url) throws IOException {
        final Bitmap[] x = new Bitmap[1];
        final String thisurl = url;

        new Thread()  {
            public void run() {

                HttpURLConnection connection =
                        null;
                try {
                    connection = (HttpURLConnection) new URL(thisurl).openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    connection.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                InputStream input = null;
                try {
                    input = connection.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                x[0] = BitmapFactory.decodeStream(input);
            }
        }.start();


        return new BitmapDrawable(x[0]);
    }

}
