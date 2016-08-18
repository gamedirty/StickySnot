package com.example.sovnem.stickysnot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gamedirty.snotviewlib.SnotPanel;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        SnotPanel root = SnotPanel.attachToWindow(this);
        root.makeViewSoft(this, R.id.snot);
        root.makeViewSoft(this,R.id.lalalal);
    }
}
