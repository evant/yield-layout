package me.tatarka.yieldlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Marks a location where a {@link YieldLayout} may add a child. This view will has size 0, and will
 * be replaced by whatever child is provided.
 */
public final class Yield extends View {
    public Yield(Context context) {
        super(context);
        init();
    }

    public Yield(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public Yield(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            setVisibility(View.GONE);
            setWillNotDraw(true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isInEditMode()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(0, 0);
        }
    }
}
