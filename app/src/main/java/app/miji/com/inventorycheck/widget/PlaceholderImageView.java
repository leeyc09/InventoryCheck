package app.miji.com.inventorycheck.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by isse on 02/01/2017.
 */

public class PlaceholderImageView extends ImageView {

    public PlaceholderImageView(Context context) {
        super(context);
    }

    public PlaceholderImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaceholderImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlaceholderImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth() / 2;
        float mAspectRatio = 1.5f;
        setMeasuredDimension(measuredWidth, (int) (measuredWidth / mAspectRatio));
    }
}
