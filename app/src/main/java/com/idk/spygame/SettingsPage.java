package com.idk.spygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsPage extends AppCompatActivity {

    Button back;
    Switch language;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = this.getSharedPreferences("spy_game_prefs", Context.MODE_PRIVATE);
        /* Locale locale = new Locale("cn");
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics()); */

        setContentView(R.layout.settings);

        back = findViewById(R.id.back);
        language = findViewById(R.id.switch_language);

        if(prefs.getString("language", "english").equals("chinese")){
            language.setChecked(true);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(SettingsPage.this, R.raw.back);
                finish();
            }
        });

        language.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sounds.play(SettingsPage.this, R.raw.other_select);
                SharedPreferences.Editor edit = prefs.edit();

                if(isChecked){
                    edit.putString("language", "chinese");
                } else {
                    edit.putString("language", "english");
                }
                edit.apply();
            }
        });
    }
}
