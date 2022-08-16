package com.idk.spygame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionsPage2 extends AppCompatActivity {

    Button back_inst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.instructions2);

        back_inst = findViewById(R.id.back_inst2);

        back_inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(InstructionsPage2.this, R.raw.back);
                finish();
            }
        });
    }
}
