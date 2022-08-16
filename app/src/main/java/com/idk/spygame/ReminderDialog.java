package com.idk.spygame;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ReminderDialog extends Dialog {

    public ReminderDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.reminder_dialog);
    }

    public void showDialog(int position, TextView remindInstruct){
        TextView playerWord = findViewById(R.id.player_word_remind);
        TextView playerWordtl = findViewById(R.id.translated_word_remind);
        Button remember = findViewById(R.id.remember);

        if(GameData.playerData.get(position).isSpy){
            playerWord.setText(GameData.spyWord);
            playerWordtl.setText(GameData.spyWordtl);
        } else if(GameData.playerData.get(position).isBlankGuesser){
            playerWord.setText(GameData.blankGuesserMessage);
            playerWordtl.setText(GameData.blankGuesserTranslated);
        } else {
            playerWord.setText(GameData.civilianWord);
            playerWordtl.setText(GameData.civilianWordtl);
        }

        remember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Sounds.play(ReminderDialog.this.getContext(), R.raw.confirm);
                remindInstruct.setText("");
            }
        });

        show();
    }
}
