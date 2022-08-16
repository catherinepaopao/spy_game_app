package com.idk.spygame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Locale;

public class GamePlay extends AppCompatActivity implements PlaySound{

    RecyclerView players;
    PlayerAdapter playerAdapter;
    int numPlayersInitiated;
    TextView instructions;
    Button forgotWord;
    TextView reminderInstruct;
    TextView startingPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board);

        PlayerNameEntryDialog dialog = new PlayerNameEntryDialog(this);
        EndGameDialog dialog2 = new EndGameDialog(this);
        ReminderDialog dialog3 = new ReminderDialog(this);
        reminderInstruct = findViewById(R.id.remind_instruct);
        startingPlayer = findViewById(R.id.starting);

        playerAdapter = new PlayerAdapter(GameData.playerData, dialog, dialog2, dialog3, reminderInstruct, this::playSound, startingPlayer);
        numPlayersInitiated = 0;
        instructions = findViewById(R.id.instructions);
        GameData.gameMode = 0;
        forgotWord = findViewById(R.id.forgot_word);

        forgotWord.setVisibility(View.GONE);
        startingPlayer.setVisibility(View.GONE);

        dialog.setUpdateRecycler(new PlayerNameEntryDialog.UpdateRecycler() {
            @Override
            public void updateDisplay() {
                numPlayersInitiated++;
                if(numPlayersInitiated >= GameData.playerData.size()){
                    instructions.setText("Tap to vote spy!");
                    forgotWord.setVisibility(View.VISIBLE);
                    startingPlayer.setVisibility(View.VISIBLE);
                    GameData.gameMode = 1;

                    int starting = (int) (Math.random()*numPlayersInitiated);
                    String message = GameData.playerData.get(starting).playerName.toUpperCase(Locale.ROOT) + " starts!";
                    startingPlayer.setText(message);

                    /*if(GameData.lastVoted == -1){

                    } else {
                        startingPlayer.setText("");
                    } else {
                        String message = "";
                        for(int i = GameData.lastVoted; i<numPlayersInitiated; i++){
                            if(!GameData.playerData.get(i).isClicked){
                                message = GameData.playerData.get(i).playerName.toUpperCase(Locale.ROOT) + " STARTS!";
                            }
                        }

                        if(message.equals("")){
                            for(int i = 0; i<GameData.lastVoted; i++){
                                if(!GameData.playerData.get(i).isClicked){
                                    message = GameData.playerData.get(i).playerName.toUpperCase(Locale.ROOT) + " STARTS!";
                                }
                            }
                        }

                        startingPlayer.setText(message);
                    } */
                }
                playerAdapter.notifyDataSetChanged();
            }
        });

        dialog2.setReturnToMainMenu(new EndGameDialog.ReturnToMainMenu() {
            @Override
            public void returnToMainMenu() {
                GameData.gameMode = 0;
                GameData.playerData.clear();
                Intent returnToMenu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(returnToMenu);
            }
        });

        players = findViewById(R.id.players);
        players.setLayoutManager(new GridLayoutManager(this, 3));
        players.setAdapter(playerAdapter);
        playerAdapter.notifyDataSetChanged();

        forgotWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sounds.play(GamePlay.this, R.raw.other_select);
                GameData.gameMode = 3;
                reminderInstruct.setText("Tap the player whose word you want to view.");
            }
        });
    }

    @Override
    public void playSound(int sound) {
        Sounds.play(this, sound);
    }
}
