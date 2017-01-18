package app.miji.com.inventorycheck.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.fabtransitionactivity.SheetLayout;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.fragment.DeliveryListFragment;
import app.miji.com.inventorycheck.fragment.TransferListFragment;

public class StockInActivity extends AppCompatActivity implements SheetLayout.OnFabAnimationEndListener, DeliveryListFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private FloatingActionButton fabDelivery;
    private FloatingActionButton fabTransfer;

    private SheetLayout mSheetLayout;


    private static final int REQUEST_CODE = 1;
    private int selectedTab;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup floating action buttons
        fabDelivery = (FloatingActionButton) findViewById(R.id.fab_delivery);
        fabTransfer = (FloatingActionButton) findViewById(R.id.fab_transfer);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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
        mSheetLayout.setFab(fabDelivery);
        mSheetLayout.setFab(fabTransfer);
        mSheetLayout.setFabAnimationEndListener(StockInActivity.this);

        //fabDelivery click
        fabDelivery.setOnClickListener(new View.OnClickListener() {
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
                        fabDelivery.show();
                    }
                });
                break;

            case 1:
                fabDelivery.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override
                    public void onHidden(FloatingActionButton fab) {
                        fabTransfer.show();
                    }
                });
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stock_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFabAnimationEnd() {
        Intent intent;
        switch (selectedTab) {
            case 0:
                //DELIVERY ACTIVITY
                intent = new Intent(this, DeliveryActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case 1:
                //TRANSFER ACTIVITY
                intent = new Intent(this, TransferActivity.class);
                //0 -> from stockin
                intent.putExtra(TransferActivity.FLAG, 0);
                startActivityForResult(intent, REQUEST_CODE);
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
    public void onFragmentInteraction(Uri uri) {

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
                    fragment = DeliveryListFragment.newInstance(null, null);
                    break;
                case 1:
                    fragment = TransferListFragment.newInstance(0);
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
            String delivery = getString(R.string.delivery);
            String transfer = getString(R.string.transfer);

            switch (position) {
                case 0:
                    return delivery;
                case 1:
                    return transfer;
            }
            return null;
        }
    }
}
