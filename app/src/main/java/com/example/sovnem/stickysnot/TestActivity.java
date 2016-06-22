package com.example.sovnem.stickysnot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        SnotPanel root = SnotPanel.attachToWindow(this);

        root.makeViewSoft(R.id.snot);
        root.makeViewSoft(R.id.rect);
    }
}
