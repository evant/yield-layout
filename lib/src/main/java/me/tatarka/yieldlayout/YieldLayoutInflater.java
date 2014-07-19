package me.tatarka.yieldlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A layout inflater for {@link YieldLayout} that matches the api of {@link android.view.LayoutInflater}.
 * By using this instead, inflating the YieldLayout and it's layoutResource can be done in one step.
 */
public class YieldLayoutInflater {
    private LayoutInflater mInflater;

    /**
     * Constructs a new {@code YieldLayoutInflater} from the given {@link android.view.LayoutInflater}.
     *
     * @param inflater the inflater
     */
    public YieldLayoutInflater(LayoutInflater inflater) {
        mInflater = inflater;
    }

    /**
     * Constructs a new {@code YieldLayoutInflater} from the given context.
     *
     * @param context the context
     * @return the new {@code YieldLayoutInflater}
     */
    public static YieldLayoutInflater from(Context context) {
        return new YieldLayoutInflater(LayoutInflater.from(context));
    }

    /**
     * Inflates the {@link YieldLayout} from given resource, optionally attaching itself to the
     * given root. Just like {@link LayoutInflater#inflate(int, android.view.ViewGroup, boolean)},
     * it's good practice to always provide the root.
     *
     * @param resource     The resource to inflate. This resource must have a root of {@link YieldLayout}
     *                     which defines a layoutResource.
     * @param root         the view root
     * @param attachToRoot If true, the layout is added to the given root.
     * @return The inflated View. Note: this is <i>not</i> an instance of {@link YieldLayout} but is
     * instead the root View of it's layoutResource.
     */
    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
        YieldLayout yieldLayout = inflateYieldLayout(resource, root, attachToRoot);
        return yieldLayout.inflate(root, attachToRoot);
    }

    /**
     * Inflates the {@link YieldLayout} from given resource, attaching itself to the
     * given root if it's not null.
     *
     * @param resource The resource to inflate. This resource must have a root of {@link YieldLayout}
     *                 which defines a layoutResource.
     * @param root     the view root
     * @return The inflated View. Note: this is <i>not</i> an instance of {@link YieldLayout} but is
     * instead the root View of it's layoutResource.
     */
    public View inflate(int resource, ViewGroup root) {
        return inflate(resource, root, root != null);
    }

    /**
     * Inflates the {@link YieldLayout} from given resource, optionally attaching itself to the
     * given root. Just like {@link LayoutInflater#inflate(int, android.view.ViewGroup, boolean)},
     * it's good practice to always provide the root.
     *
     * @param resource       The resource to inflate. This resource must have a root of {@link YieldLayout}.
     * @param layoutResource the layoutResource to inflate the {@link YieldLayout} with.
     * @param root           the view root
     * @param attachToRoot   If true, the layout is added to the given root.
     * @return The inflated View. Note: this is <i>not</i> an instance of {@link YieldLayout} but is
     * instead the root View of it's layoutResource.
     */
    public View inflate(int resource, int layoutResource, ViewGroup root, boolean attachToRoot) {
        YieldLayout yieldLayout = inflateYieldLayout(resource, root, attachToRoot);
        yieldLayout.setLayoutResource(layoutResource);
        if (root == null) {
            return yieldLayout;
        }
        return yieldLayout.inflate(root, attachToRoot);
    }

    /**
     * Inflates the {@link YieldLayout} from given resource, attaching itself to the
     * given root if it's not null.
     *
     * @param resource       The resource to inflate. This resource must have a root of {@link YieldLayout}.
     * @param layoutResource the layoutResource to inflate the {@link YieldLayout} with.
     * @param root           the view root
     * @return The inflated View. Note: this is <i>not</i> an instance of {@link YieldLayout} but is
     * instead the root View of it's layoutResource.
     */
    public View inflate(int resource, int layoutResource, ViewGroup root) {
        return inflate(resource, layoutResource, root, root != null);
    }

    private YieldLayout inflateYieldLayout(int resource, ViewGroup root, boolean attachToRoot) {
        View inflatedView = mInflater.inflate(resource, root, attachToRoot);
        if (!(inflatedView instanceof YieldLayout)) {
            throw new IllegalArgumentException("YieldLayoutInflater resource must have root YieldLayout (found: '" + inflatedView.getClass().getSimpleName() + "')");
        }
        return (YieldLayout) inflatedView;
    }
}
