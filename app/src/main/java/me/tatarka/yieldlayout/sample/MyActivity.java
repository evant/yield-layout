package me.tatarka.yieldlayout.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyActivity extends ActionBarActivity {
    private static final String INDEX = "index";

    private int mCurrentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(INDEX, 0);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ActionBarSpinnerAdapter adapter = new ActionBarSpinnerAdapter();
        actionBar.setListNavigationCallbacks(adapter, adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INDEX, mCurrentIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class ActionBarSpinnerAdapter extends BaseAdapter implements ActionBar.OnNavigationListener {
        ExampleFragment[] fragments = new ExampleFragment[] {
                new BasicExampleFragment(),
                new ListExampleFragment(),
                new DynamicExampleFragment()
        };

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public ExampleFragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MyActivity.this).inflate(R.layout.list_item_actionbar, parent, false);
            }

            ((TextView) convertView).setText(getItem(position).getTitle());

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MyActivity.this).inflate(R.layout.list_item_actionbar_dropdown, parent, false);
            }

            ((TextView) convertView).setText(getItem(position).getTitle());

            return convertView;
        }

        @Override
        public boolean onNavigationItemSelected(int position, long id) {
            if (mCurrentIndex == position) return true;
            mCurrentIndex = position;

            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, getItem(position))
                    .commit();

            return true;
        }
    }
}
