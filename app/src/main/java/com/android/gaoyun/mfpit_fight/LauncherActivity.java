package com.android.gaoyun.mfpit_fight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

public class LauncherActivity extends AppCompatActivity {

    Button newGameButton;

    EditText ipAddrText;
    EditText portText;

    public static String ipAddr = "";
    public static int port = 0;

    public static UUID client_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        setTitle(R.string.app_title);

        newGameButton = (Button)findViewById(R.id.buttonNewGame);

        ipAddrText = (EditText)findViewById(R.id.ipText);
        portText = (EditText)findViewById(R.id.portText);

        ipAddrText.setInputType(InputType.TYPE_CLASS_PHONE);
        portText.setInputType(InputType.TYPE_CLASS_NUMBER);

    }

    public void startNewGame(View view) {

        ipAddr = ipAddrText.getText().toString();
        port = Integer.parseInt(portText.getText().toString());

        Intent roundIntent = new Intent(LauncherActivity.this, RoundActivity.class);
        startActivity(roundIntent);

        //finish();

    }
}
