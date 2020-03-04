package com.example.onscrollloading.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.onscrollloading.R;
import com.example.onscrollloading.adapter.ItemAdapter;
import com.example.onscrollloading.interfaceClass.LoadMore;
import com.example.onscrollloading.pojo.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private List<Item> itemList = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        randomData();

        showData();
    }

    private void showData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(recyclerView, this, itemList);
        recyclerView.setAdapter(itemAdapter);

        itemAdapter.setLoadMore(new LoadMore() {
            @Override
            public void onLoadMore() {
                if (itemList.size() <= 10) {
                    itemList.add(null);
                    itemAdapter.notifyItemInserted(itemList.size() - 1);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemList.remove(itemList.size() - 1);
                            itemAdapter.notifyItemRemoved(itemList.size());

                            int index = itemList.size();
                            int end = index + 10;

                            for (int i = index; i < end; i++) {
                                randomData();
                            }

                            itemAdapter.notifyDataSetChanged();
                            itemAdapter.setLoaded();
                        }
                    }, 5000);

                } else {
                    Toast.makeText(MainActivity.this, "Load data completed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerViewId);
    }

    private void randomData() {
        for (int i = 0; i < 10; i++) {
            String name = UUID.randomUUID().toString();
            Item item = new Item(name, name.length());
            itemList.add(item);
        }
    }
}
