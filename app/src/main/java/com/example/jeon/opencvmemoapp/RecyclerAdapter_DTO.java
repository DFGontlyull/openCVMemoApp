package com.example.jeon.opencvmemoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RecyclerAdapter_DTO extends RecyclerView.Adapter<RecyclerAdapter_DTO.ViewHolder>  {
    Context context;
    List<ImageDTO> ImageDTOs;
    int item_layout;
    private FirebaseDatabase adapterdatabase = FirebaseDatabase.getInstance();
    LoadImage loadImage = new LoadImage();
    private Bitmap tempBitmap;

    public RecyclerAdapter_DTO(Context context, List<ImageDTO> imageDTOs, int item_layout) {
        this.context = context;
        this.ImageDTOs = imageDTOs;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ImageDTO item = ImageDTOs.get(position);
        Bitmap tempBitmap = DecodeBitmapFile(item.getImageUrl());
//        Drawable drawable = ContextCompat.getDrawable(context, item.getImagePath());
        Drawable drawable = new BitmapDrawable(tempBitmap);
//        tempBitmap = new LoadImage().doInBackground(item.getImageUrl());
//        new LoadImage(holder, drawable).execute(item.getImageUrl());
//        Picasso.with(context).load(item.getImageUrl()).into(holder.image);

//        holder.image.setBackground(getDrawableFromBitmap(tempBitmap));
        holder.title.setText(item.getTitle());
        holder.image.setText(item.getContents());
//        holder.date.setText(item.getDate());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), viewMemo.class);
                intent.putExtra("item", item);
                v.getContext().startActivity(intent);

                Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private Drawable drawableFromUrl(String url)
//            throws IOException {
//        Bitmap x;
//
//        HttpURLConnection connection =
//                (HttpURLConnection) new URL(url).openConnection();
//        connection.connect();
//        InputStream input = connection.getInputStream();
//
//        x = BitmapFactory.decodeStream(input);
//        return new BitmapDrawable(x);
//    }

    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);

        return d;
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

    @Override
    public int getItemCount() {
        return this.ImageDTOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView image;
        TextView title;
        TextView date;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
//            image = (ImageView) itemView.findViewById(R.id.image);
            image = (TextView) itemView.findViewById(R.id.image);
//            image.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.CLEAR);
            title = (TextView) itemView.findViewById(R.id.title);
//            date = (TextView) itemView.findViewById(R.id.date);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }





}