package com.idk.spygame;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class PlayerNameEntryDialog extends Dialog {

    UpdateRecycler updateRecycler = null;

    public PlayerNameEntryDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.name_entry_dialog);
    }

    public void setUpdateRecycler(UpdateRecycler updateRecycler) {
        this.updateRecycler = updateRecycler;
    }

    public void showDialog(int position) {
        TextView playerWord = findViewById(R.id.player_word);
        TextView playerWordtl = findViewById(R.id.translated_word);

        if (GameData.playerData.get(position).isSpy) {
            playerWord.setText(GameData.spyWord);
            playerWordtl.setText("(" + GameData.spyWordtl + ")");
        } else if (GameData.playerData.get(position).isBlankGuesser){
            playerWord.setText(GameData.blankGuesserMessage);
            playerWordtl.setText("(" + GameData.blankGuesserTranslated + ")");
        } else {
            playerWord.setText(GameData.civilianWord);
            playerWordtl.setText("(" + GameData.civilianWordtl + ")");
        }

        EditText editText = findViewById(R.id.player_name);
        editText.setText("");
        Button button = findViewById(R.id.submit_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView errorMessage = findViewById(R.id.error_name);
                String enteredName = editText.getText().toString();

                if(enteredName.matches("^.*[^a-zA-Z0-9 ].*$")){
                    Sounds.play(PlayerNameEntryDialog.this.getContext(), R.raw.error);
                    errorMessage.setText("Invalid characters!");
                } else if(enteredName.length() >= 7){
                    Sounds.play(PlayerNameEntryDialog.this.getContext(), R.raw.error);
                    errorMessage.setText("Too long, shorten name!");
                } else if(enteredName.length() < 1){
                    Sounds.play(PlayerNameEntryDialog.this.getContext(), R.raw.error);
                    errorMessage.setText("Please enter a name!");
                } else {
                    errorMessage.setText("");
                    GameData.playerData.get(position).playerName = editText.getText().toString();
                    GameData.playerData.get(position).isWordViewed = true;
                    updateRecycler.updateDisplay();
                    dismiss();
                }
            }
        });

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Sounds.play(PlayerNameEntryDialog.this.getContext(), R.raw.confirm);
            }
        });

        show();
    }

    interface UpdateRecycler{
        void updateDisplay();
    }

}
