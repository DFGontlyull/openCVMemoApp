package com.example.jeon.opencvmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int GET_IMAGE_PATH = 4;
    public static final int sub = 1001;
    private static final int MY_PERMISSION_STORAGE = 1111;
    List<Button> btnList = new ArrayList<>();
    final List<Item> itemList = new ArrayList<>();
    Button camBtn = null;
    ImageView Photo = null;
    private String imagePath = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==GET_IMAGE_PATH){
            if(resultCode==RESULT_OK){
                imagePath = data.getExtras().getString("Path");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        btnList.add((Button)findViewById(R.id.makeList));
        btnList.add((Button)findViewById(R.id.deleteList));
        btnList.add((Button)findViewById(R.id.share));

        itemList.add(new Item(R.drawable.ic_launcher_foreground, "타이틀", "2018-12-04"));

//        Item[] item = new Item[ITEM_SIZE];
//        item[0] = new Item(R.drawable.a, "#1");
//        item[1] = new Item(R.drawable.b, "#2");
//        item[2] = new Item(R.drawable.c, "#3");
//        item[3] = new Item(R.drawable.d, "#4");
//        item[4] = new Item(R.drawable.e, "#5");

        for(int i=0; i<itemList.size(); i++){
            items.add(itemList.get(i));
        }
        Button btnNewActivity2 = (Button)findViewById(R.id.makeList);
        btnNewActivity2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), getPhotos.class);
                startActivityForResult(intent, GET_IMAGE_PATH);
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

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_main));



//        Button.OnTouchListener onTouchListener = new Button.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                    btnList.get(0).setBackgroundColor(Color.TRANSPARENT);
//                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
//                    btnList.get(0).setBackgroundColor(Color.LTGRAY);
//                }
//                return false;
//            }
//        };
//        Button.OnClickListener onClickListener = new Button.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                TextView textView1 = (TextView) findViewById(R.id.textView1);
//                switch (view.getId()) {
//                    case R.id.button1 :
//                        textView1.setText("Red") ;
//                        textView1.setBackgroundColor(Color.rgb(255, 0, 0));
//                        break ;
//                        case R.id.button2 : textView1.setText("Green") ;
//                        textView1.setBackgroundColor(Color.rgb(0, 255, 0));
//                        break ;
//                        case R.id.button3 : textView1.setText("Blue") ;
//                        textView1.setBackgroundColor(Color.rgb(0, 0, 255));
//                        break ;
//                        case R.id.button4 : textView1.setText("Blue") ;
//                        textView1.setBackgroundColor(Color.rgb(0, 0, 255));
//                        break ;
//                }
//            }
//        } ;
    }
}
