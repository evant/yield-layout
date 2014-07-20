yield-layout
============

Have you ever had to create two almost-identical layout files because they had a few minor differences? Sure, you can fix it with `<include/>` but then you start ending up with layouts all over the place.

YieldLayout to the rescue! It works opposite of `<include/>` so you can combine one layout around another instead of inside it. After it does it's magic, you will have 0 extra views in your layout hierarchy, just like `<include/>`, so it's like it was never there.

![preview](https://raw.githubusercontent.com/evant/yield-layout/master/images/preview.png)

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

### ListViews

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

### Different numbers of children

You may be wondering what happens if you don't specify as many child views for `YieldLayout` as there are `Yield`s. Too many children is an error because there is no way to know where to put the extra ones. Too few, however, will simply cause the extra `Yields` to be removed.

Sometimes, however, you may depend on the `Yield` to correctly layout your views. For example,

```xml
<RelativeLayout
  xmlns:android="http://schemas.android.com/apk/res/android"
  xmlns:app="http://schemas.android.com/apk/res-auto"
  android:layout_width="match_parent"
  android:layout_height="wrap_content">

    <TextView
         android:id="@+id/content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_above="@+id/footer"/>

    <me.tatarka.yieldlayout.Yield
        android:id="@+id/footer"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"/>
</RelativeLayout>
```

If there is no matching child for `footer`, then it will be removed, and `content` won't lay out correctly. To fix this, you can add

```xml
    <me.tatarka.yieldlayout.Yield
      app:yield_keep_if_empty="true"/>
```

which will keep the `Yield` view even if it's not replaced. Note that in this case, the `Yield` view will always have a size of 0.

### Layout Preview

The layout preview in Android Studio will show `Yield` placholder views so that you can see how they fit in your layout. If you define your `yield_layout` for your `YieldLayout`, that will correctly show as well.

If you are setting your `yield_layout` dynamically, you would think you could preview it by setting `tools:yield_layout` in your xml. Unfortunately, the `tools` namespace doesn't work with custom attributes. To get around this, you can set `app:tools_yield_layout` instead which will give an equivalent result.
