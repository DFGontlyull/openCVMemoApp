package com.example.jeon.opencvmemoapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GET_IMAGE_PATH = 3;
    private static final int ADD_CARD_VIEW = 4;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
        if (requestCode == 3) {
//                Intent intent = data;
                tempItem = new Item(data.getStringExtra("Path"), data.getStringExtra("Title"), data.getStringExtra("resultText"));
//                tempItem = (Item) intent.getSerializableExtra("Item");
                itemList.add(tempItem);
            recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), itemList, R.layout.activity_main));

//                Intent intent = new Intent(getApplicationContext(), manageCardView.class);
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("Item", tempItem);
////                intent.putExtra("Item", bundle);
////                startActivityForResult(intent, ADD_CARD_VIEW);
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

        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        btnList.add((Button)findViewById(R.id.makeList));
        btnList.add((Button)findViewById(R.id.deleteList));
        btnList.add((Button)findViewById(R.id.share));

//        itemList.add(new Item(R.drawable.ic_launcher_foreground, "타이틀", "2018-12-04"));

//        Item[] item = new Item[ITEM_SIZE];
//        item[0] = new Item(R.drawable.a, "#1");
////        item[1] = new Item(R.drawable.b, "#2");
////        item[2] = new Item(R.drawable.c, "#3");
////        item[3] = new Item(R.drawable.d, "#4");
////        item[4] = new Item(R.drawable.e, "#5");

        for(int i=0; i<itemList.size(); i++){
            items.add(itemList.get(i));
        }
        Button btnNewActivity2 = (Button)findViewById(R.id.makeList);
        btnNewActivity2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), getPhotos.class);
                startActivityForResult(intent, GET_IMAGE_PATH);
                if(tempItem!=null) {
                    thisBitmap = getBitmapFromItem(tempItem);
                }
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
                Intent intent = new Intent(getApplicationContext(), viewContents.class);
                startActivity(intent);
            }
        });
//        for (int i = 0; i < ITEM_SIZE; i++) {
//            items.add(item[i]);
//        }

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), itemList, R.layout.activity_main));
    }
}
