package app.miji.com.inventorycheck.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.adapter.MyHomeItemRecyclerViewAdapter;
import app.miji.com.inventorycheck.model.HomeContent;

public class MainActivity extends AppCompatActivity {


    private MyHomeItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setup recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_home);
        int mColumnCount = getResources().getInteger(R.integer.list_column_count);
        //set Layout Manager
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, mColumnCount));
        }

        assert recyclerView != null;
        setupRecyclerView(recyclerView);


    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new MyHomeItemRecyclerViewAdapter(this, HomeContent.HOME_ITEMS);
        recyclerView.setAdapter(mAdapter);
    }

}
