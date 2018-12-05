package com.example.jeon.opencvmemoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_STORAGE = 1111;
    List<Button> btnList = new ArrayList<>();
    final List<Item> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        btnList.add((Button)findViewById(R.id.button1));
        btnList.add((Button)findViewById(R.id.button2));
        btnList.add((Button)findViewById(R.id.button3));
        btnList.add((Button)findViewById(R.id.button4));

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

        Button btnNewActivity1 = (Button)findViewById(R.id.button1);
        btnNewActivity1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), viewContents.class);
                startActivity(intent);
            }
        });

        Button btnNewActivity2 = (Button)findViewById(R.id.button2);
        btnNewActivity2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), getPhotos.class);
                startActivity(intent);
            }
        });

        Button btnNewActivity3 = (Button)findViewById(R.id.button3);
        btnNewActivity3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), viewContents.class);
                startActivity(intent);
            }
        });

        Button btnNewActivity4 = (Button)findViewById(R.id.button4);
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

//    private void checkPermission(){
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            // 다시 보지 않기 버튼을 만드려면 이 부분에 바로 요청을 하도록 하면 됨 (아래 else{..} 부분 제거)
//            // ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_CAMERA);
//
//            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -&gt; else{..}의 요청으로 넘어감
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                new AlertDialog.Builder(this)
//                        .setTitle("알림")
//                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
//                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                intent.setData(Uri.parse("package:" + getPackageName()));
//                                startActivity(intent);
//                            }
//                        })
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                finish();
//                            }
//                        })
//                        .setCancelable(false)
//                        .create()
//                        .show();
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSION_STORAGE:
//                for (int i = 0; i  < grantResults.length; i++) {
//                // grantResults[] : 허용된 권한은 0, 거부한 권한은 -1
//                if (grantResults[i] < 0) {
//                    Toast.makeText(MainActivity.this, "해당 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//            // 허용했다면 이 부분에서..
//
//            break;
//        }
//    }


}
