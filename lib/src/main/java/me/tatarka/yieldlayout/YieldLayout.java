package me.tatarka.yieldlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.List;

/**
 * A layout that replaces itself with a given layout resource yielding children in places defined
 * by that layout. This way you can compose around views, complementary to how {@code <include/>}
 * composes views.
 * <p/>
 * The children of this layout may have {@code layout_yield_id}'s to define which {@link Yield} view
 * that they will replace. If not, then they will be replaced in the order that they are defined.
 * <p/>
 * It is an error to have more children than the layout has places for. However the reverse is
 * allowed. Any {@link Yield}'s that aren't replaced will simply be removed from the view hierarchy.
 */
public class YieldLayout extends ViewGroup {
    private static final int YIELD_LAYOUT_ID_TAG = 54231097;

    private int mLayoutResource;
    private View mLayoutView;
    private List<View> mChildren;

    public YieldLayout(Context context) {
        super(context);
        init(context);
    }

    public YieldLayout(Context context, int layoutResource) {
        super(context);
        mLayoutResource = layoutResource;
        init(context);
    }

    public YieldLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YieldLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.YieldLayout);
            mLayoutResource = a.getResourceId(R.styleable.YieldLayout_yield_layout, 0);
            if (isInEditMode()) {
                mLayoutResource = a.getResourceId(R.styleable.YieldLayout_tools_yield_layout, mLayoutResource);
            }
            a.recycle();
        }

        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mLayoutResource != 0) {
            inflate();
        }
    }

    private void init(Context context) {
        setVisibility(View.GONE);
        setWillNotDraw(true);
    }

    public int getLayoutResource() {
        return mLayoutResource;
    }

    /**
     * Sets the layout resource for the layout. If the {@code YieldLayout} has already been added to
     * the view hierarchy, this will update the shown view.
     *
     * @param layoutResource the layout resource
     */
    public void setLayoutResource(int layoutResource) {
        mLayoutResource = layoutResource;
        inflate();
    }

    public View inflate(ViewGroup root) {
        return inflate(root, true);
    }

    public View inflate(ViewGroup root, boolean attachToParent) {
        View currentView = mLayoutView;
        if (currentView == null) currentView = this;
        return inflate(currentView, root, attachToParent);
    }

    private void inflate() {
        ViewParent viewParent = null;
        View currentView = this;

        if (mLayoutView != null) {
            currentView = mLayoutView;
            viewParent = mLayoutView.getParent();
        }

        if (viewParent == null) {
            viewParent = getParent();
        }

        if (viewParent == null) {
            return;
        }

        if (!(viewParent instanceof ViewGroup)) {
            throw new IllegalStateException("YieldLayout must have a non-null ViewGroup viewParent (Instead parent was: '" + viewParent + "')");
        }

        ViewGroup parent = (ViewGroup) viewParent;
        inflate(currentView, parent, true);
    }

    private View inflate(View currentView, ViewGroup root, boolean attachToParent) {
        if (mLayoutResource == 0) {
            throw new IllegalStateException("YieldLayout must have a valid layoutResource");
        }

        LayoutInflater factory = LayoutInflater.from(getContext());
        View view = factory.inflate(mLayoutResource, root, false);

        if (view instanceof ViewGroup) {
            replaceYieldWithChildren((ViewGroup) view);
        } else if (getChildCount() > 0) {
            throw new IllegalArgumentException("YieldLayout layout is not a ViewGroup and so does not expect children");
        }

        if (attachToParent && root != null) {
            replaceView(currentView, view);
        }

        return mLayoutView = view;
    }

    private void replaceYieldWithChildren(ViewGroup viewLayout) {
        List<View> children = takeChildren();
        List<Yield> yieldViews = collectYieldViews(viewLayout, new ArrayList<Yield>(children.size()));

        if (children.size() > yieldViews.size()) {
            throw new IllegalArgumentException("YieldLayout you have added more children (" + children.size() + ") than expected (" + yieldViews.size() + ")");
        }

        if (yieldViews.isEmpty()) return;

        if (children.isEmpty()) {
            removeEmptyYieldViews(yieldViews);
            return;
        }

        boolean hasExplicitYieldIds = getYieldId(children.get(0)) != 0;

        if (hasExplicitYieldIds) {
            for (View child : children) {
                int childYieldId = getYieldId(child);
                if (childYieldId == 0) {
                    throw new IllegalArgumentException("Expected layout_yield_id for " + child.getClass().getSimpleName() + " (If at least one child has a layout_yield_id, they all must)");
                }

                Yield yield = takeYieldWithId(yieldViews, childYieldId);
                if (yield == null) {
                    throw new IllegalArgumentException("YieldLayout includes child with layout_yield_id which is not in the layout:");
                }

                replaceView(yield, child);
            }
        } else {
            for (int i = children.size() - 1; i >= 0; i--) {
                Yield yield = yieldViews.remove(i);
                View child = children.get(i);
                int childYieldId = getYieldId(child);
                if (childYieldId != 0) {
                    //Means the previous one was 0
                    throw new IllegalArgumentException("Expected layout_yield_id for " + children.get(i - 1).getClass().getSimpleName() + " (If at least one child has a layout_yield_id, they all must)");
                }

                replaceView(yield, child);
            }
        }

        removeEmptyYieldViews(yieldViews);
    }

    private List<View> takeChildren() {
        if (mLayoutView == null) {
            mChildren = new ArrayList<View>(getChildCount());
            for (int i = 0; i < getChildCount(); i++) {
                mChildren.add(getChildAt(i));
            }
            removeAllViewsInLayout();
        } else {
            for (View child : mChildren) {
                ViewParent viewParent = child.getParent();
                if (viewParent instanceof ViewGroup) {
                    ((ViewGroup) viewParent).removeViewInLayout(child);
                }
            }
        }
        return mChildren;
    }

    private static int getYieldId(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof LayoutParams) {
            int yieldId = ((LayoutParams) params).yieldId;
            view.setTag(YIELD_LAYOUT_ID_TAG, yieldId);
            return yieldId;
        } else {
            Integer yieldIdTag = (Integer) view.getTag(YIELD_LAYOUT_ID_TAG);
            if (yieldIdTag != null) return yieldIdTag;
        }
        return 0;
    }

    private void removeEmptyYieldViews(List<Yield> yieldViews) {
        for (Yield yield : yieldViews) {
            yield.notifyKept();
            if (yield.getKeepIfEmpty()) continue;

            ViewParent viewParent = yield.getParent();
            if (viewParent instanceof ViewGroup) {
                ((ViewGroup) viewParent).removeViewInLayout(yield);
            }
        }
    }

    private List<Yield> collectYieldViews(ViewGroup viewLayout, List<Yield> yieldViews) {
        for (int i = 0; i < viewLayout.getChildCount(); i++) {
            View child = viewLayout.getChildAt(i);
            if (child instanceof Yield) {
                yieldViews.add((Yield) child);
            } else if (child instanceof ViewGroup) {
                collectYieldViews((ViewGroup) child, yieldViews);
            }
        }
        return yieldViews;
    }

    private static Yield takeYieldWithId(List<Yield> list, int yieldId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == yieldId) {
                return list.remove(i);
            }
        }
        return null;
    }

    private static void replaceView(View toReplace, View replacement) {
        ViewGroup yieldParent = (ViewGroup) toReplace.getParent();
        int yieldIndex = yieldParent.indexOfChild(toReplace);
        yieldParent.removeViewInLayout(toReplace);
        ViewGroup.LayoutParams yieldParams = toReplace.getLayoutParams();

        if (replacement.getId() == NO_ID) {
            replacement.setId(toReplace.getId());
        }

        if (yieldParams != null) {
            yieldParent.addView(replacement, yieldIndex, yieldParams);
        } else {
            yieldParent.addView(replacement, yieldIndex);
        }
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(0, 0);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int yieldId;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.YieldLayout);
            yieldId = a.getResourceId(R.styleable.YieldLayout_layout_yield_id, 0);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            if (source instanceof LayoutParams) {
                yieldId = ((LayoutParams) source).yieldId;
            }
        }
    }
}
