package com.tuesda.circlerefreshlayout;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AboutActivity extends TitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);

        setTitle(null);
        showBackwardView("◁",true);
        showForwardView("",false);
        showLocationIcon(false);
    }
}
