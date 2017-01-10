package app.miji.com.inventorycheck.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import app.miji.com.inventorycheck.R;
import app.miji.com.inventorycheck.model.Utility;

public class NewItemsActivity extends AppCompatActivity {

    public static final String DETAIL = "detail";

    private LinearLayout card_detail;

    private boolean mSwitch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String detail = null;

        if (intent != null) {
            detail = intent.getStringExtra(DETAIL);
        }
        card_detail = (LinearLayout) findViewById(R.id.root_detail);
        TextView txtDetail = (TextView) findViewById(R.id.txt_details);
        txtDetail.setText(detail);

        //hide details when clicked
        txtDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDetails();
            }
        });


        //initially hide details
        showDetails();


        //show image view popup
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.setBackgroundColor(Color.BLACK);
        imagePopup.setWindowWidth(Utility.getScreenWidth(getWindowManager()));
        imagePopup.setWindowHeight(Utility.getScreenHeight(getWindowManager()));

        imageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //TODO change drawable image dynamically
                imagePopup.initiatePopup(getDrawable(R.drawable.warehouse));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_items, menu);
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

    private void showHideDetails(boolean isVisible) {
        if (isVisible) {
            showDetails();

        } else {
            hideDetails();

        }
    }


    private void showDetails() {
        card_detail.setVisibility(View.VISIBLE);
        mSwitch = false;
    }

    private void hideDetails() {
        card_detail.setVisibility(View.GONE);
        mSwitch = true;
    }
}
