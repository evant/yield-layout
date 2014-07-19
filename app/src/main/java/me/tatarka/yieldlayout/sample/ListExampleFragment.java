package me.tatarka.yieldlayout.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import me.tatarka.yieldlayout.YieldLayoutInflater;

public class ListExampleFragment extends ExampleFragment {
    @Override
    public String getTitle() {
        return "List Example";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(new MyAdapter());
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 50;
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
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                int layout = getItemViewType(position) == 0 ? R.layout.list_item_main_only : R.layout.list_item_with_action;
                convertView = YieldLayoutInflater.from(getActivity()).inflate(layout, parent, false);
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
