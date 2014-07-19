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
        android:layout_weight="1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/divider_black"
        />

    <TextView
        android:layout_weight="1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/between"
        android:gravity="center"
        android:textSize="@dimen/text_display_2"
        android:textColor="@color/text_black"
        />

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="@color/divider_black"
        />

    <me.tatarka.yieldlayout.Yield
        android:id="@+id/second"
        android:layout_weight="1"
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
    app:yield_layout="@layout/yield_linear_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:text="@string/first"
        android:textColor="@color/text_black"
        android:textSize="@dimen/text_display_2"
        />

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/second"
        android:textColor="@color/text_black"
        android:textSize="@dimen/text_display_2"
        android:gravity="center"
        />

</me.tatarka.yieldlayout.YieldLayout>
```

You can also define yield id's to specify which child views go with which yields.

```xml
<?xml version="1.0" encoding="utf-8"?>

<me.tatarka.yieldlayout.YieldLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    app:yield_layout="@layout/yield_linear_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <TextView
        app:layout_yield_id="@+id/second"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/second"
        android:textColor="@color/text_black"
        android:textSize="@dimen/text_display_2"
        android:gravity="center"
        />

    <TextView
        app:layout_yield_id="@+id/first"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:text="@string/first"
        android:textColor="@color/text_black"
        android:textSize="@dimen/text_display_2"
        />
</me.tatarka.yieldlayout.YieldLayout>

```

If you want to use this in a list adapter, you can't use the `YieldLayout` directly because it will try to add it's layout to it's parent which a listview doesn't allow. Instead, you can use `YieldLayoutInflater` which will return the final view to add instead of the `YieldLayout`.

```java
@Override
public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
        int layout = getItemViewType(position) == 0 ? R.layout.list_item_1 : R.layout.list_item_2;  
        convertView = YieldLayoutInflater.from(mContext).inflate(layout, parent, false);
    }
    return convertView;
}
```
