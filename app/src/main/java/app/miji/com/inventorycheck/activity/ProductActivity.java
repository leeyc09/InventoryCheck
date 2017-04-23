package app.miji.com.inventorycheck.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;

import app.miji.com.inventorycheck.R;

public class ProductActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {

    private static final int REQUEST_CODE = 1;
    private SheetLayout mSheetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup sheet layout
        mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
        mSheetLayout.setFab(fab);
        mSheetLayout.setFab(fab);
        mSheetLayout.setFabAnimationEndListener(ProductActivity.this);

        //fabDelivery click
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });

    }

    @Override
    public void onFabAnimationEnd() {
        //New Product Activity
        Intent intent = new Intent(this, NewProductActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mSheetLayout.contractFab();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        //-------------Search Widget ------------------------
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class)));
        // Do not iconify the widget; expand it by default
        searchView.setIconifiedByDefault(false);
        // Add submit button on search widget
        searchView.setSubmitButtonEnabled(true);
        //Query refinement for search suggestions
        searchView.setQueryRefinementEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            //onSearchRequested();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //shows the overflow menu
        return true;
    }
}
