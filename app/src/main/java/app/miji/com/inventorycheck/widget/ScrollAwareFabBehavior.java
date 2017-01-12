package app.miji.com.inventorycheck.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

/**
 * This class solves error:
 * java.lang.IllegalArgumentException:
 * Rect should intersect with child's bounds
 */

public class ScrollAwareFabBehavior extends FloatingActionButton.Behavior {

    public ScrollAwareFabBehavior() {
    }

    public ScrollAwareFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean getInsetDodgeRect(@NonNull CoordinatorLayout parent, @NonNull FloatingActionButton child, @NonNull Rect rect) {
        super.getInsetDodgeRect(parent, child, rect);
        return false;
    }
}
