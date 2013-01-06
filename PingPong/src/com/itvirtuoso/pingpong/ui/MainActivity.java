package com.itvirtuoso.pingpong.ui;

import android.app.Activity;
import android.app.AlertDialog;
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
        Button joinGameButton = (Button) findViewById(R.id.joinGameButton);
        joinGameButton.setOnClickListener(new JoinGameButtonListener());
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
    
    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("警告");
        builder.setMessage("実装中です");
        builder.setPositiveButton("OK", null);
        builder.show();
    }
    
    class NewGameButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(MainActivity.this, BluetoothServerActivity.class);
//            startActivity(intent);
            showDialog();
        }
    }
    
    class JoinGameButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            showDialog();
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
