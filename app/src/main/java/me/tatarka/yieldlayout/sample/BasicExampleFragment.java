package me.tatarka.yieldlayout.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BasicExampleFragment extends ExampleFragment {
    @Override
    public String getTitle() {
        return "Basic Example";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.use_linear_layout, container, false);
    }
}
