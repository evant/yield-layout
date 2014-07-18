package me.tatarka.yieldlayout.sample;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import me.tatarka.yieldlayout.YieldLayout;

public class MyActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MyListAdapter());
    }

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                int layout = getItemViewType(position) == 0 ? R.layout.list_item_main_only : R.layout.list_item_with_sidebar;
                View view = LayoutInflater.from(MyActivity.this).inflate(layout, parent, false);
                convertView = ((YieldLayout) view).inflate(parent);
            }
            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            return position % 2;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }
    }
}
