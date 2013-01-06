package com.itvirtuoso.pingpong.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.itvirtuoso.pingpong.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button newGameButton = (Button) findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new NewGameButtonListener());
        Button trainingButton = (Button) findViewById(R.id.traningButton);
        trainingButton.setOnClickListener(new TraningButtonListener());
        Button socketButton = (Button) findViewById(R.id.socketButton);
        socketButton.setOnClickListener(new SocketButtonListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    class NewGameButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, BluetoothServerActivity.class);
            startActivity(intent);
        }
    }

    class TraningButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, TraningActivity.class);
            startActivity(intent);
        }
    }
    
    class SocketButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SocketActivity.class);
            startActivity(intent);
        }
    }
}
