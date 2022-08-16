package com.idk.spygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionsPage1 extends AppCompatActivity {

    Button back_inst;
    Button next_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.instructions1);

        back_inst = findViewById(R.id.back_inst);
        next_page = findViewById(R.id.next_page);

        back_inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(InstructionsPage1.this, R.raw.back);
                finish();
            }
        });

        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(InstructionsPage1.this, R.raw.other_select);
                Intent instructionsIntent = new Intent(getApplicationContext(), InstructionsPage2.class);
                startActivity(instructionsIntent);
            }
        });
    }
}
