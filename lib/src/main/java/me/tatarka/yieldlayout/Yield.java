package me.tatarka.yieldlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Marks a location where a {@link YieldLayout} may add a child. This view will has size 0, and will
 * be replaced by whatever child is provided. Note: in edit mode, this will display as a nice
 * placeholder view, but at runtime it will be a view with 0 size.
 */
public final class Yield extends View {
    private boolean mKeepIfEmpty;

    private boolean mShowEditPreview;
    private int mEditModeMinimumWidth;
    private int mEditModeMinimumHeight;
    private TextPaint mEditModeTextPaint;
    private Paint mEditModeBorderPaint;

    public Yield(Context context) {
        super(context);
        init();
    }

    public Yield(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Yield(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Yield);
            mKeepIfEmpty = a.getBoolean(R.styleable.Yield_yield_keep_if_empty, mKeepIfEmpty);
            a.recycle();
        }

        init();
    }

    private void init() {
        mShowEditPreview = isInEditMode();

        if (mShowEditPreview) {
            setWillNotDraw(false);
            mEditModeMinimumWidth = dpToPx(100);
            mEditModeMinimumHeight = dpToPx(32);

            mEditModeTextPaint = new TextPaint();
            mEditModeTextPaint.setTextAlign(Paint.Align.CENTER);
            mEditModeTextPaint.setAntiAlias(true);

            mEditModeBorderPaint = new Paint();
            mEditModeBorderPaint.setColor(Color.rgb(180, 180, 180));
            mEditModeBorderPaint.setStyle(Paint.Style.STROKE);
            mEditModeBorderPaint.setStrokeWidth(dpToPx(1));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mEditModeTextPaint.setHinting(Paint.HINTING_ON);
            }
        } else {
            setVisibility(View.GONE);
            setWillNotDraw(true);
        }
    }

    public boolean getKeepIfEmpty() {
        return mKeepIfEmpty;
    }

    public void setKeepIfEmpty(boolean value) {
        mKeepIfEmpty = value;
    }

    void notifyKept() {
        mShowEditPreview = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mShowEditPreview) {
            setMeasuredDimension(getBetterDefaultSize(mEditModeMinimumWidth, widthMeasureSpec),
                    getBetterDefaultSize(mEditModeMinimumHeight, heightMeasureSpec));
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowEditPreview) {
            canvas.drawRGB(211, 211, 211);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mEditModeBorderPaint);
            canvas.drawText("Yield", getWidth() / 2, (int) (getHeight() / 2  - (mEditModeTextPaint.descent() + mEditModeTextPaint.ascent()) / 2), mEditModeTextPaint);
        }
    }

    private static int getBetterDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(size, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
