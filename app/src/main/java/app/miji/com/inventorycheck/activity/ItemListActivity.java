package app.miji.com.inventorycheck.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.fragment.ItemListFragment;
import app.miji.com.inventorycheck.fragment.NewItemsFragment;
import app.miji.com.inventorycheck.model.Delivery;
import app.miji.com.inventorycheck.model.Sales;
import app.miji.com.inventorycheck.model.Transfer;
import app.miji.com.inventorycheck.utility.Utility;

public class ItemListActivity extends AppCompatActivity {

    public static final String DETAILS = "details";
    public static final String BASE64IMAGE = "image";

    private LinearLayout card_detail;
    private boolean mSwitch = false;

    private Delivery mDelivery;
    private Transfer mTransfer;
    private Sales mSales;

    private Object mObjectToPass;

    private String mFromActivity = null;

    private String detail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String base64Image = null;

        if (intent != null) {
            detail = intent.getStringExtra(DETAILS);
            //TODO get image
            //base64Image = intent.getStringExtra(BASE64IMAGE);


            //if from delivery
            if (intent.hasExtra(ItemListFragment.DELIVERY)) {
                mDelivery = intent.getParcelableExtra(ItemListFragment.DELIVERY);
                mObjectToPass = mDelivery;
                mFromActivity = NewItemsFragment.DELIVERY; //means this is from delivery
            }

            //if from transfer
            if (intent.hasExtra(ItemListFragment.TRANSFER)) {
                mTransfer = intent.getParcelableExtra(ItemListFragment.TRANSFER);
                mObjectToPass = mTransfer;
                mFromActivity = NewItemsFragment.TRANSFER; //means this is from delivery
            }

            //if from sales
            if (intent.hasExtra(ItemListFragment.SALES)) {
                mSales = intent.getParcelableExtra(ItemListFragment.SALES);
                mObjectToPass = mSales;
                mFromActivity = NewItemsFragment.TRANSFER; //means this is from sales
            }


        }


        //edit fab is clicked
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemListActivity.this, NewItemsActivity.class);
                intent.putExtra(NewItemsActivity.FLAG, 1);//add flag for editing items
                intent.putExtra(NewItemsFragment.ITEMS, NewItemsFragment.ITEMS);
                intent.putExtra(NewItemsActivity.ACTIVITY, mFromActivity); //means this is either from delivery,sales,transfer
                intent.putExtra(NewItemsFragment.OBJECT, (Parcelable) mObjectToPass); //pass object
                intent.putExtra(NewItemsActivity.DETAIL, detail);//pass details
                startActivity(intent);
            }
        });


        //details
        card_detail = (LinearLayout) findViewById(R.id.cardview_details);
        TextView txtDetail = (TextView) findViewById(R.id.txt_details);
        txtDetail.setText(detail);

        //hide details when clicked
        card_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDetails();
            }
        });


        //initially hide details
        showDetails();

        //show image view popup
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        //image
        if (base64Image != null) {
            //show imageview
            imageView.setVisibility(View.VISIBLE);
            Bitmap bitmap = Utility.decodeBase64Image(base64Image);
            imageView.setImageBitmap(bitmap);
        } else {
            //no image, hide it
            imageView.setVisibility(View.GONE);
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //show imageview popup
                Drawable drawable = imageView.getDrawable();
                Utility.showImagePopup(ItemListActivity.this, getWindowManager(), drawable);
            }
        });
    }


    /*
    * go back to the parent activity
    * */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void hideDetails() {
        mSwitch = true;
        // Start the animation
        card_detail.animate()
                .translationY(card_detail.getHeight() / 2)
                .alpha(0.0f)
                .setDuration(300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card_detail.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        //shows the overflow menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_details:
                showHideDetails(mSwitch);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDetails() {
        mSwitch = false;

        // Start the animation
        card_detail.animate()
                .translationY(0)
                .alpha(1.0f)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card_detail.setVisibility(View.VISIBLE);
                    }
                });
    }


    private void showHideDetails(boolean isVisible) {
        if (isVisible) {
            showDetails();

        } else {
            hideDetails();

        }
    }

}
