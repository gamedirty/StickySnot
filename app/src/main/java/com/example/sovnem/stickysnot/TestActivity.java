package com.example.sovnem.stickysnot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.View;

import com.gamedirty.snotviewlib.SnotPanel;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        SnotPanel root = SnotPanel.attachToWindow(this);
        root.makeViewSoft(this, R.id.snot);
        root.makeViewSoft(this,R.id.lalalal);
        root.makeViewSoft(this,R.id.rect);


    }

    public void showPop(View v){
        PopupMenu popupMenu = new PopupMenu(this,v, Gravity.START|Gravity.BOTTOM);
        popupMenu.inflate(R.menu.lalala);
        popupMenu.show();
    }
}







