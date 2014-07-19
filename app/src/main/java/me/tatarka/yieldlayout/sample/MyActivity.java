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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ActionBarSpinnerAdapter adapter = new ActionBarSpinnerAdapter();
        actionBar.setListNavigationCallbacks(adapter, adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private class ActionBarSpinnerAdapter extends BaseAdapter implements ActionBar.OnNavigationListener {
        ExampleFragment[] fragments = new ExampleFragment[] {
                new BasicExampleFragment(),
                new ListExampleFragment()
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
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, getItem(position))
                    .commit();
            return true;
        }
    }
}
