package com.example.layoutmanagergroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.layoutmanagergroup.Config.ItemConfig;
import com.example.layoutmanagergroup.adapter.CardAdapter;
import com.example.layoutmanagergroup.base.ItemTouchHelperCallback;
import com.example.layoutmanagergroup.base.OnSlideListener;
import com.example.layoutmanagergroup.layoutmanger.CardLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardLayoutManager cardLayoutManager;

    private CardAdapter cardAdapter;

    private ItemTouchHelperCallback mItemTouchHelperCallback;
    private ItemTouchHelper itemTouchHelper;

    private List<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        ids = new ArrayList<>();
        ids.add(R.mipmap.bg_1);
        ids.add(R.mipmap.bg_2);
        ids.add(R.mipmap.bg_3);
        ids.add(R.mipmap.bg_4);
        addData();
        addData();
        cardAdapter = new CardAdapter(this, ids);
        recyclerView.setAdapter(cardAdapter);
        mItemTouchHelperCallback = new ItemTouchHelperCallback(cardAdapter, ids);
        itemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);

        cardLayoutManager = new CardLayoutManager(recyclerView, itemTouchHelper);
        recyclerView.setLayoutManager(cardLayoutManager);

        itemTouchHelper.attachToRecyclerView(recyclerView);
        initListener();
    }

    private void initListener() {
        mItemTouchHelperCallback.setOnSlideListener(new OnSlideListener() {
            @Override
            public void onSliding(RecyclerView.ViewHolder viewHolder, float ratio, int direction) {
                if (direction == ItemConfig.SLIDING_LEFT) {
                } else if (direction == ItemConfig.SLIDING_RIGHT) {
                }
            }

            @Override
            public void onSlided(RecyclerView.ViewHolder viewHolder, Object o, int direction) {
                if (direction == ItemConfig.SLIDED_LEFT) {
                    Toast.makeText(MainActivity.this, "滑到左边", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemConfig.SLIDED_RIGHT) {
                    Toast.makeText(MainActivity.this, "滑到右边", Toast.LENGTH_SHORT).show();
                }
                int position = viewHolder.getAdapterPosition();
                Log.e("LHD", "onSlided--position:" + position);
            }

            @Override
            public void onClear() {
                addData();
            }
        });
    }

    private void addData() {
        Log.i("LHD", "addData");
        ids.add(R.mipmap.bg_3);
        ids.add(R.mipmap.bg_4);
        ids.add(R.mipmap.bg_1);
        ids.add(R.mipmap.bg_2);
    }

}
