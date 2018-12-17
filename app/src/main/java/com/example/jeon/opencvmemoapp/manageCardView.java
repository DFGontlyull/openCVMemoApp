//package com.example.jeon.opencvmemoapp;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//
//import java.util.ArrayList;
//
//public class manageCardView extends Activity {
//    private static final int ADD_CARD_VIEW = 4;
//    private Item tempItem;
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//    private ArrayList<myData> myDataset;
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == ADD_CARD_VIEW) {
//            if (resultCode == RESULT_OK) {
//                tempItem = (Item) data.getSerializableExtra("Item");
//            }
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);
//
//        // use a linear layout manager
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        // specify an adapter (see also next example)
//        mAdapter = new myAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);
//
//        myDataset.add(new myData("#InsideOut", tempItem);
////        myDataset.add(new myData("#Mini", R.mipmap.mini));
////        myDataset.add(new myData("#ToyStroy", R.mipmap.toystory));
//    }
//}
