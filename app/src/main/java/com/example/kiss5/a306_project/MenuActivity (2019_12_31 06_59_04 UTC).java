package com.example.kiss5.a306_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        configureMultiPlayerButton();
        configureSinglePlayerButton();
    }

    public void configureMultiPlayerButton(){
        Button multiplayer_button = (Button)findViewById(R.id.multiplayer_button);
        multiplayer_button.setText("Multiplayer");

        multiplayer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MultiPlayerActivity.class));
            }
        });
    }

    public void configureSinglePlayerButton(){
        Button singleplayer_button = (Button)findViewById(R.id.singleplayer_button);
        singleplayer_button.setText("Singleplayer");

        singleplayer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, SinglePlayerActivity.class));
            }
        });
    }

}
