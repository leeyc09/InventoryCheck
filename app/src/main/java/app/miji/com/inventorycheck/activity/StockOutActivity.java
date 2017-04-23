package app.miji.com.inventorycheck.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.fragment.SalesListFragment;
import app.miji.com.inventorycheck.fragment.TransferListFragment;

public class StockOutActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private int selectedTab;

    private FloatingActionButton fabSales;
    private FloatingActionButton fabTransfer;
    private SheetLayout mSheetLayout;

    private static final int REQUEST_CODE = 1;
    public static final String TAB = "tab";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sometimes this activity can start on the transfer fragment
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(TAB)) {
                selectedTab = intent.getIntExtra(TAB, 0);
            }
        }

        //setup floating action buttons
        fabSales = (FloatingActionButton) findViewById(R.id.fab_sales);
        fabTransfer = (FloatingActionButton) findViewById(R.id.fab_transfer);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.setCurrentItem(selectedTab);
        showProperFab(selectedTab);

        //when tab changes, fab should also change
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                selectedTab = tab.getPosition();
                showProperFab(selectedTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //setup sheet layout
        mSheetLayout = (SheetLayout) findViewById(R.id.bottom_sheet);
        mSheetLayout.setFab(fabSales);
        mSheetLayout.setFab(fabTransfer);
        mSheetLayout.setFabAnimationEndListener(StockOutActivity.this);

        //fabDelivery click
        fabSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });

        //setup fabTransfer click
        fabTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSheetLayout.expandFab();
            }
        });


    }

    private void showProperFab(int tab) {
        switch (tab) {
            case 0:
                fabTransfer.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        fabSales.show();
                    }
                });
                break;

            case 1:
                fabSales.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        fabTransfer.show();
                    }
                });
                break;
        }
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
        getMenuInflater().inflate(R.menu.menu_stock_out, menu);

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
    public void onFabAnimationEnd() {
        Intent intent;
        switch (selectedTab) {
            case 0:
                //Sales activity
                intent = new Intent(this, SalesActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case 1:
                //Transfer activity
                intent = new Intent(this, TransferActivity.class);
                //1 -> from stockout
                intent.putExtra(TransferActivity.FLAG, 1);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {

                case 0:
                    fragment = new SalesListFragment();
                    break;
                case 1:
                    fragment = TransferListFragment.newInstance(1);
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String sales = getString(R.string.sales);
            String transfer = getString(R.string.transfer);

            switch (position) {
                case 0:
                    return sales;
                case 1:
                    return transfer;
            }
            return null;
        }
    }
}
