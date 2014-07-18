yield-layout
============

A layout that allows you to "yield" spaces from one layout file to another. This way you can compose around views.

## Usage

You can create a layout file that has places to yield content.

```xml
<!-- yield_view.xml -->
<?xml version="1.0" encoding="utf-8"?>

<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <me.tatarka.yieldlayout.Yield
        android:id="@+id/first"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/in_between"
        android:padding="15dp"
        android:gravity="center_horizontal"
        />

    <me.tatarka.yieldlayout.Yield
        android:id="@+id/second"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</LinearLayout>
```

Then you can create another file that uses that layout, filling in the yields.

```xml
<!-- use_yield.xml -->
<?xml version="1.0" encoding="utf-8"?>

<me.tatarka.yieldlayout.YieldLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:yield_layout="@layout/yield_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="First"
        android:padding="15dp"
        />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Second"
        android:padding="15dp"
        android:gravity="right"
        />

</me.tatarka.yieldlayout.YieldLayout>
```

You can also define yield id's to specify which child views go with which yields.

```xml
<?xml version="1.0" encoding="utf-8"?>

<me.tatarka.yieldlayout.YieldLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:yield_layout="@layout/yield_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        app:layout_yield_id="@+id/second"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Second"
        android:padding="15dp"
        android:gravity="right"
        />

    <TextView
        app:layout_yield_id="@+id/first"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="First"
        android:padding="15dp"
        />
</me.tatarka.yieldlayout.YieldLayout>
```

If you want to use this in a list adapter, you can't use the `YieldLayout` directly because ListView will error when `YieldLayout` tries to add itself. Instead, call `inflate(parent)` on the `YieldLayout` to return the final view to return from the adapter.

```java
@Override
public View getView(int position, View convertView, ViewGroup parent) {
  if (convertView == null) {
    int layout = getItemViewType(position) == 0 ? R.layout.list_item_1 : R.layout.list_item_2;
    View view = LayoutInflater.from(mContext).inflate(layout, parent, false);
    convertView = ((YieldLayout) view).inflate(parent);
  }
  return convertView;
}
```
