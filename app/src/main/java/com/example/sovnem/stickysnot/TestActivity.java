package com.example.sovnem.stickysnot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SnotPanel root = SnotPanel.attachToWindow(this, R.layout.activity_test);
        setContentView(root);
        View snot = findViewById(R.id.snot);
        root.makeViewSoft(snot);
    }
}
