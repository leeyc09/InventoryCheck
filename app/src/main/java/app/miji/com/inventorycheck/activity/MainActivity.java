package app.miji.com.inventorycheck.activity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

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

        startLogoAnimation();

    }

    private void startLogoAnimation() {
        final ImageView mImageLogo = (ImageView) findViewById(R.id.image_logo);


        final long DEFAULT_ANIMATION_DURATION = 1750L;

        int appBarHeight = (int) getResources().getDimension(R.dimen.app_bar_height);


        //Create an instance of ValueAnimator by calling the static method ofFloat.
        // In this case, the values start at 0 and end with appBarHeight.
        // Android starts screen coordinates at the top-left corner,
        // it moves  top to bottom.
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, appBarHeight);

        //ValueAnimator calls this listener with every update to the animated value
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //Get the current value from the animator and cast it to float;
                // current value type is float because you created the ValueAnimator with ofFloat.
                float value = (float) animation.getAnimatedValue();
                //Change the image's position by using the setTranslationY().
                mImageLogo.setTranslationY(value / 3);
            }
        });

        //Set up the animator’s duration and interpolator.
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
        //start animation
        valueAnimator.start();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        mAdapter = new MyHomeItemRecyclerViewAdapter(this, HomeContent.HOME_ITEMS);
        recyclerView.setAdapter(mAdapter);
    }

}
