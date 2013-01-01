package com.itvirtuoso.pingpong.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.itvirtuoso.pingpong.R;

public class PaddleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paddle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_paddle, menu);
        return true;
    }

}
