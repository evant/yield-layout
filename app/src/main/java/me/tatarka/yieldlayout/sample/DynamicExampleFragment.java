package me.tatarka.yieldlayout.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import me.tatarka.yieldlayout.YieldLayout;

public class DynamicExampleFragment extends ExampleFragment {
    @Override
    public String getTitle() {
        return "Dynamic Example";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.use_dynamic_yield, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadioGroup btnLayoutGroup = (RadioGroup) view.findViewById(R.id.btn_layout_group);
        final YieldLayout yieldLayout = (YieldLayout) view.findViewById(R.id.yield_layout);

        btnLayoutGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int layout = checkedId == R.id.btn_layout_1 ? R.layout.dynamic_layout_1 : R.layout.dynamic_layout_2;
                yieldLayout.setLayoutResource(layout);
            }
        });

        // listener doesn't get called if on the default option
        if (btnLayoutGroup.getCheckedRadioButtonId() == R.id.btn_layout_1) {
            yieldLayout.setLayoutResource(R.layout.dynamic_layout_1);
        }
    }
}
