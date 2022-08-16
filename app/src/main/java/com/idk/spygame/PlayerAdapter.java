package com.idk.spygame;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {
    List<PlayerData> players;
    PlayerNameEntryDialog dialogBox;
    EndGameDialog dialogBox2;
    ReminderDialog dialogBox3;
    TextView remindInstruct;
    PlaySound playSound;
    TextView startingPlayer;

    public PlayerAdapter(List<PlayerData> players, PlayerNameEntryDialog dialog, EndGameDialog dialog2, ReminderDialog dialog3,
                         TextView remindInstructs, PlaySound playSound, TextView startingPlayer) {
        this.players = players;
        dialogBox = dialog;
        dialogBox2 = dialog2;
        dialogBox3 = dialog3;
        remindInstruct = remindInstructs;
        this.playSound = playSound;
        this.startingPlayer = startingPlayer;
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final Button buttonView;

        public PlayerViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            buttonView = (Button) view.findViewById(R.id.player_icon);
        }

        public Button getButtonView() {
            return buttonView;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.game_cell, viewGroup, false);
        return new PlayerViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PlayerViewHolder viewHolder, final int pos) {

        final int position = viewHolder.getAdapterPosition();

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getButtonView().setText(players.get(position).playerName);
        if(players.get(position).playerName.equals("?")){
            viewHolder.getButtonView().setTextColor(Color.parseColor("#dba348"));
        } else {
            viewHolder.getButtonView().setTextColor(Color.parseColor("#8ba9d6"));
        }

        viewHolder.buttonView.setOnClickListener(new DoubleClickListener() {

            @Override
            public void onSingleClick(View v) {
                if(GameData.gameMode == 0){ // view word, single tap only
                    if (GameData.playerData.get(position).isWordViewed == false) {
                        playSound.playSound(R.raw.confirm);
                        dialogBox.showDialog(position);
                    }
                } else if(GameData.gameMode == 3){ // remind word, single tap
                    playSound.playSound(R.raw.confirm);
                    dialogBox3.showDialog(position, remindInstruct);
                    GameData.gameMode = 1;
                }
            }

            @Override
            public void onDoubleClick(View v) {
                if(GameData.gameMode == 1){ // voting, double tap only
                    if(!GameData.playerData.get(position).isClicked){
                        // GameData.lastVoted = position;
                        if(GameData.chaosMode){ // CHAOS MODE STUFF
                            if(GameData.playerData.get(position).isSpy){
                                playSound.playSound(R.raw.correct_vote);
                                viewHolder.getButtonView().setText("SPY");
                                GameData.playerData.get(position).isClicked = true;
                                viewHolder.getButtonView().setTextColor(Color.parseColor("#ff0000"));
                                GameData.spiesLeft--;
                            } else if(GameData.playerData.get(position).isBlankGuesser){
                                viewHolder.getButtonView().setText("BLANK");
                                playSound.playSound(R.raw.blank_vote);
                                GameData.playerData.get(position).isClicked = true;
                                viewHolder.getButtonView().setTextColor(Color.parseColor("#dadee6"));
                                GameData.civiliansLeft--;
                            } else {
                                viewHolder.getButtonView().setText("CIVILIAN");
                                playSound.playSound(R.raw.wrong_vote);
                                GameData.playerData.get(position).isClicked = true;
                                viewHolder.getButtonView().setTextColor(Color.parseColor("#fcba03"));
                                GameData.civiliansLeft--;
                            }

                            if(GameData.spiesLeft == 0){
                                dialogBox2.showDialog("All spies!");
                                playSound.playSound(R.raw.lose);
                                GameData.gameMode = 2;
                            } else if(GameData.spiesLeft >= GameData.civiliansLeft){
                                dialogBox2.showDialog("All civilians!");
                                playSound.playSound(R.raw.lose); // CHANGE ALL TO BLANK
                                GameData.gameMode = 2;
                            } else if(GameData.civiliansLeft == 0) {
                                dialogBox2.showDialog("All blank guessers!");
                                playSound.playSound(R.raw.lose);
                                GameData.gameMode = 2;
                            }
                        } else { // NORMAL STUFF
                            if (GameData.playerData.get(position).isSpy) {
                                playSound.playSound(R.raw.correct_vote);
                                viewHolder.getButtonView().setText("SPY");
                                GameData.playerData.get(position).isClicked = true;
                                viewHolder.getButtonView().setTextColor(Color.parseColor("#ff0000"));
                                GameData.spiesLeft--;
                            } else if (GameData.playerData.get(position).isBlankGuesser) {
                                viewHolder.getButtonView().setText("BLANK");
                                playSound.playSound(R.raw.blank_vote);
                                GameData.playerData.get(position).isClicked = true;
                                viewHolder.getButtonView().setTextColor(Color.parseColor("#dadee6"));
                                GameData.blankGuesser = false;
                            } else {
                                viewHolder.getButtonView().setText("CIVILIAN");
                                playSound.playSound(R.raw.wrong_vote);
                                GameData.playerData.get(position).isClicked = true;
                                viewHolder.getButtonView().setTextColor(Color.parseColor("#fcba03"));
                                GameData.civiliansLeft--;
                            }

                            if (GameData.spiesLeft == 0) {
                                dialogBox2.showDialog("Civilians have won!");
                                playSound.playSound(R.raw.win);
                                GameData.gameMode = 2;
                            } else if (GameData.spiesLeft >= GameData.civiliansLeft) {
                                if (GameData.civiliansLeft == 1 && GameData.blankGuesser) {
                                    dialogBox2.showDialog("Blank Guesser wins!");
                                    playSound.playSound(R.raw.blank_win); // CHANGE THIS
                                } else {
                                    dialogBox2.showDialog("Spies have won!");
                                    playSound.playSound(R.raw.lose);
                                }
                                GameData.gameMode = 2;
                            }
                        }
                        startingPlayer.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return players.size();
    }

}

interface PlaySound {
    void playSound(int sound);
}
