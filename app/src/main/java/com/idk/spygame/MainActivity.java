package com.idk.spygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button createNormalGame;
    Button settings;
    Button instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        createNormalGame = findViewById(R.id.create_normal_game);
        settings = findViewById(R.id.settings);
        instructions = findViewById(R.id.instructions);

        createNormalGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(MainActivity.this, R.raw.select);
                Intent normalGameCreationIntent = new Intent(getApplicationContext(), NormalGameCreate.class);
                startActivity(normalGameCreationIntent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(MainActivity.this, R.raw.other_select);
                Intent settingsIntent = new Intent(getApplicationContext(), SettingsPage.class);
                startActivity(settingsIntent);
            }
        });

        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(MainActivity.this, R.raw.other_select);
                Intent instructionsIntent = new Intent(getApplicationContext(), InstructionsPage1.class);
                startActivity(instructionsIntent);
            }
        });
    }
}