package com.idk.spygame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NormalGameCreate extends AppCompatActivity {

    Button playerAdd;
    Button playerSubtract;
    Button spyAdd;
    Button spySubtract;
    Button startGame;
    Switch blankGuesser;
    Switch chaosMode;
    TextView playerNumber;
    TextView spyNumber;
    TextView errors;
    SharedPreferences prefs;
    ArrayList<Pair<String, String>> wordsEng;
    ArrayList<Pair<String, String>> wordsCh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* try {
            InputStream file = getResources().getAssets().open("<my file name from assets folder");
            // read the file!
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        setContentView(R.layout.normal_game_create);
        prefs = this.getSharedPreferences("spy_game_prefs", Context.MODE_PRIVATE);

        playerAdd = findViewById(R.id.player_add);
        playerSubtract = findViewById(R.id.player_subtract);
        spyAdd = findViewById(R.id.spy_add);
        spySubtract = findViewById(R.id.spy_subtract);
        startGame = findViewById(R.id.start_game);
        playerNumber = findViewById(R.id.player_number);
        spyNumber = findViewById(R.id.spy_number);
        errors = findViewById(R.id.errors);
        blankGuesser = findViewById(R.id.blank_guess);
        chaosMode = findViewById(R.id.chaos_mode);
        playerNumber.setText(Integer.toString(prefs.getInt("playerCount", 3)));
        spyNumber.setText(Integer.toString(prefs.getInt("spyCount", 1)));
        createEnglishList();
        createChineseList();

        if(GameData.blankGuesserHistory){
            blankGuesser.setChecked(true);
        }

        playerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int playerNum = Integer.parseInt((String) playerNumber.getText());
                if(playerNum != 10){
                    Sounds.play(NormalGameCreate.this, R.raw.add_sub);
                    playerNum++;
                    playerNumber.setText(Integer.toString(playerNum));
                    errors.setText("");
                } else {
                    Sounds.play(NormalGameCreate.this, R.raw.error);
                    errors.setTextColor(Color.parseColor("#a85700"));
                    errors.setText("No more than 10 players!");
                }
            }
        });

        playerSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int playerNum = Integer.parseInt((String) playerNumber.getText());
                int spyNum = Integer.parseInt((String) spyNumber.getText());
                if(playerNum == 3) {
                    Sounds.play(NormalGameCreate.this, R.raw.error);
                    errors.setTextColor(Color.parseColor("#a85700"));
                    errors.setText("Must have at least 3 players!");
                } else if(spyNum*3 == playerNum){
                    Sounds.play(NormalGameCreate.this, R.raw.error);
                    errors.setTextColor(Color.parseColor("#a85700"));
                    errors.setText("Lower spy count first!");
                } else {
                    Sounds.play(NormalGameCreate.this, R.raw.add_sub);
                    playerNum--;
                    playerNumber.setText(Integer.toString(playerNum));
                    errors.setText("");
                }

                if (playerNum < 4) {
                    if(blankGuesser.isChecked()) {
                        blankGuesser.setChecked(false);
                    }

                }
            }
        });

        spyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int playerNum = Integer.parseInt((String) playerNumber.getText());
                int spyNum = Integer.parseInt((String) spyNumber.getText());
                if(spyNum < playerNum/3){
                    Sounds.play(NormalGameCreate.this, R.raw.add_sub);
                    spyNum ++;
                    spyNumber.setText(Integer.toString(spyNum));
                    errors.setText("");
                } else {
                    Sounds.play(NormalGameCreate.this, R.raw.error);
                    errors.setTextColor(Color.parseColor("#a85700"));
                    errors.setText("Too many spies!");
                }
            }
        });

        spySubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int spyNum = Integer.parseInt((String) spyNumber.getText());
                if(spyNum != 1){
                    Sounds.play(NormalGameCreate.this, R.raw.add_sub);
                    spyNum --;
                    spyNumber.setText(Integer.toString(spyNum));
                    errors.setText("");
                } else {
                    Sounds.play(NormalGameCreate.this, R.raw.error);
                    errors.setTextColor(Color.parseColor("#a85700"));
                    errors.setText("Must have at least one spy.");
                }
            }
        });

        blankGuesser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sounds.play(NormalGameCreate.this, R.raw.other_select);

                if(!isChecked){
                    errors.setTextColor(Color.parseColor("#1c4dd4"));
                    errors.setText("Blank Guesser disabled");
                } else {
                    if(Integer.parseInt((String) playerNumber.getText()) < 4){
                        Sounds.play(NormalGameCreate.this, R.raw.error);
                        errors.setTextColor(Color.parseColor("#1c4dd4"));
                        errors.setText("Not enough players for Blank Guesser!");
                        blankGuesser.setChecked(false);
                    } else {
                        errors.setTextColor(Color.parseColor("#1c4dd4"));
                        errors.setText("Blank Guesser enabled");
                    }

                }

            }
        });

        chaosMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Sounds.play(NormalGameCreate.this, R.raw.other_select);

                if(!isChecked){
                    errors.setTextColor(Color.parseColor("#12a197"));
                    errors.setText("Chaos Mode disabled");
                } else {
                    errors.setTextColor(Color.parseColor("#12a197"));
                    errors.setText("Chaos Mode enabled");
                }
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameData.playerData.clear();
                Sounds.play(NormalGameCreate.this, R.raw.select);
                int playerCount = Integer.parseInt((String) playerNumber.getText());
                int spyCount = Integer.parseInt((String) spyNumber.getText());
                String language = prefs.getString("language", "english");

                SharedPreferences.Editor edit = prefs.edit();
                edit.putInt("playerCount", playerCount);
                edit.putInt("spyCount", spyCount);
                edit.apply();

                for(int i = 0 ; i < playerCount; i ++) {
                    GameData.playerData.add(new PlayerData());
                }

                if(chaosMode.isChecked()){ // CHAOS MODE
                    int mode = 0;
                    GameData.chaosMode = true;

                    if(blankGuesser.isChecked()){
                        mode = (int) (Math.random()*3); // 0 for all spy, 1 for all civilian, 2 for all blank board
                    } else {
                        mode = (int) (Math.random()*2);
                    }

                    GameData.chaosGameMode = mode;

                    if(mode == 0){
                        for(int i = 0; i<playerCount; i++){
                            GameData.playerData.get(i).isSpy = true;
                        }

                        GameData.spiesLeft = playerCount-spyCount;
                        GameData.civiliansLeft = playerCount;
                        int pairNumber = (int) (Math.random()*wordsEng.size());
                        int random = (int) (Math.random()*2);

                        String word1;
                        String word2;
                        String word1tl;
                        String word2tl;

                        if(language.equals("english")){
                            word1 = wordsEng.get(pairNumber).first;
                            word1tl = wordsCh.get(pairNumber).first;
                            word2 = wordsEng.get(pairNumber).second;
                            word2tl = wordsCh.get(pairNumber).second;
                            GameData.civilianWord = "No civilians!";
                            GameData.civilianWordtl = "没有平民！";
                        } else {
                            word1 = wordsCh.get(pairNumber).first;
                            word1tl = wordsEng.get(pairNumber).first;
                            word2 = wordsCh.get(pairNumber).second;
                            word2tl = wordsEng.get(pairNumber).second;
                            GameData.civilianWordtl = "No civilians!";
                            GameData.civilianWord = "没有平民！";
                        }

                        if(random == 0){
                            GameData.spyWord = word2;
                            GameData.spyWordtl = word2tl;
                        } else {
                            GameData.spyWord = word1;
                            GameData.spyWordtl = word1tl;
                        }
                    } else if(mode == 1){
                        GameData.spiesLeft = spyCount;
                        GameData.civiliansLeft = playerCount;
                        int pairNumber = (int) (Math.random()*wordsEng.size());
                        int random = (int) (Math.random()*2);

                        String word1;
                        String word2;
                        String word1tl;
                        String word2tl;

                        if(language.equals("english")){
                            word1 = wordsEng.get(pairNumber).first;
                            word1tl = wordsCh.get(pairNumber).first;
                            word2 = wordsEng.get(pairNumber).second;
                            word2tl = wordsCh.get(pairNumber).second;
                            GameData.spyWord = "No spies!";
                            GameData.spyWordtl = "没有卧底！";
                        } else {
                            word1 = wordsCh.get(pairNumber).first;
                            word1tl = wordsEng.get(pairNumber).first;
                            word2 = wordsCh.get(pairNumber).second;
                            word2tl = wordsEng.get(pairNumber).second;
                            GameData.spyWordtl = "No spies!";
                            GameData.spyWord = "没有卧底！";
                        }

                        if(random == 0){
                            GameData.civilianWord = word2;
                            GameData.civilianWordtl = word2tl;
                        } else {
                            GameData.civilianWord = word1;
                            GameData.civilianWordtl = word1tl;
                        }
                    } else {
                        GameData.spiesLeft = -1;
                        GameData.civiliansLeft = playerCount-spyCount;
                        for(int i = 0; i<playerCount; i++){
                            GameData.playerData.get(i).isBlankGuesser = true;
                        }

                        if(language.equals("english")){
                            GameData.blankGuesserMessage = "BLANK GUESSER";
                            GameData.blankGuesserTranslated = "白板";
                            GameData.civilianWord = "No civilians!";
                            GameData.civilianWordtl = "没有平民！";
                            GameData.spyWord = "No spies!";
                            GameData.spyWordtl = "没有卧底！";
                        } else {
                            GameData.blankGuesserMessage = "白板";
                            GameData.blankGuesserTranslated = "BLANK GUESSER";
                            GameData.civilianWordtl = "No civilians!";
                            GameData.civilianWord = "没有平民！";
                            GameData.spyWordtl = "No spies!";
                            GameData.spyWord = "没有卧底！";
                        }
                    }
                } else { // NORMAL GAME
                    GameData.spiesLeft = spyCount;
                    GameData.chaosMode = false;

                    if(blankGuesser.isChecked()){
                        GameData.civiliansLeft = playerCount-spyCount-1;
                        GameData.blankGuesser = true;
                    } else {
                        GameData.civiliansLeft = playerCount-spyCount;
                    }

                    ArrayList<Integer> alreadySpy = new ArrayList<Integer>();

                    for(int i = 0; i<spyCount; i++){
                        while(true){
                            int index = (int) (Math.random()*playerCount);
                            if(!alreadySpy.contains(index)){
                                alreadySpy.add(index);
                                GameData.playerData.get(index).isSpy = true;
                                break;
                            }
                        }
                    }

                    if(blankGuesser.isChecked()){
                        GameData.blankGuesserHistory = true;
                        while(true){
                            int index = (int) (Math.random()*playerCount);
                            if(!alreadySpy.contains(index)){
                                alreadySpy.add(index);
                                GameData.playerData.get(index).isBlankGuesser = true;
                                break;
                            }
                        }
                    } else {
                        GameData.blankGuesserHistory = false;
                    }

                    int pairNumber = (int) (Math.random()*wordsEng.size());
                    int random = (int) (Math.random()*2);

                    String word1;
                    String word2;
                    String word1tl;
                    String word2tl;

                    if(language.equals("english")){
                        word1 = wordsEng.get(pairNumber).first;
                        word1tl = wordsCh.get(pairNumber).first;
                        word2 = wordsEng.get(pairNumber).second;
                        word2tl = wordsCh.get(pairNumber).second;
                        GameData.blankGuesserMessage = "BLANK GUESSER";
                        GameData.blankGuesserTranslated = "白板";
                    } else {
                        word1 = wordsCh.get(pairNumber).first;
                        word1tl = wordsEng.get(pairNumber).first;
                        word2 = wordsCh.get(pairNumber).second;
                        word2tl = wordsEng.get(pairNumber).second;
                        GameData.blankGuesserMessage = "白板";
                        GameData.blankGuesserTranslated = "BLANK GUESSER";
                    }

                    if(random == 0){
                        GameData.spyWord = word1;
                        GameData.spyWordtl = word1tl;
                        GameData.civilianWord = word2;
                        GameData.civilianWordtl = word2tl;
                    } else {
                        GameData.spyWord = word2;
                        GameData.spyWordtl = word2tl;
                        GameData.civilianWord = word1;
                        GameData.civilianWordtl = word1tl;
                    }
                }

                Intent playGameIntent = new Intent(getApplicationContext(), GamePlay.class);
                startActivity(playGameIntent);
            }
        });
    }

    public void createEnglishList(){
        wordsEng = new ArrayList<Pair<String, String>>();
        wordsEng.add(new Pair<String, String>("Fly", "Mosquito"));
        wordsEng.add(new Pair<String, String>("Apple", "Orange"));
        wordsEng.add(new Pair<String, String>("Bed", "Sofa"));
        wordsEng.add(new Pair<String, String>("Juice", "Soda"));
        wordsEng.add(new Pair<String, String>("Oven", "Microwave"));
        wordsEng.add(new Pair<String, String>("Socks", "Gloves"));
        wordsEng.add(new Pair<String, String>("Noodles", "Rice"));
        wordsEng.add(new Pair<String, String>("Chinese New Year", "Mid-Autumn Festival"));
        wordsEng.add(new Pair<String, String>("Fire", "Stove"));
        wordsEng.add(new Pair<String, String>("iPhone", "Google Pixel"));
        wordsEng.add(new Pair<String, String>("Bowl", "Plate"));
        wordsEng.add(new Pair<String, String>("Egg Yolk", "Egg White"));
        wordsEng.add(new Pair<String, String>("Chicken Egg", "Duck Egg"));
        wordsEng.add(new Pair<String, String>("Chicken Wing", "Chicken Leg"));
        wordsEng.add(new Pair<String, String>("Hen", "Rooster"));
        wordsEng.add(new Pair<String, String>("Tree Leaves", "Tree Branch"));
        wordsEng.add(new Pair<String, String>("Pajamas", "Robe"));
        wordsEng.add(new Pair<String, String>("Cream", "Milk"));
        wordsEng.add(new Pair<String, String>("Star", "Moon"));
        wordsEng.add(new Pair<String, String>("Japanese", "Korean"));
        wordsEng.add(new Pair<String, String>("Cute", "Pretty"));
        wordsEng.add(new Pair<String, String>("Newspaper", "Magazine"));
        wordsEng.add(new Pair<String, String>("Tomorrow", "Yesterday"));
        wordsEng.add(new Pair<String, String>("Milk Tea", "Coffee"));
        wordsEng.add(new Pair<String, String>("Boiling Water", "Warm Water"));
        wordsEng.add(new Pair<String, String>("Bee", "Wasp"));
        wordsEng.add(new Pair<String, String>("Snowflake", "Ice Cube"));
        wordsEng.add(new Pair<String, String>("Potato", "Radish"));
        wordsEng.add(new Pair<String, String>("Disappointment", "Regret"));
        wordsEng.add(new Pair<String, String>("Divorce", "Argue"));
        wordsEng.add(new Pair<String, String>("Tape", "Glue"));
        wordsEng.add(new Pair<String, String>("Climate", "Weather"));
        wordsEng.add(new Pair<String, String>("Scarf", "Hat"));
        wordsEng.add(new Pair<String, String>("Shampoo", "Soap"));
        wordsEng.add(new Pair<String, String>("Towel", "Paper Towel"));
        wordsEng.add(new Pair<String, String>("Sweater", "Jacket"));
        wordsEng.add(new Pair<String, String>("Soup", "Tea"));
        wordsEng.add(new Pair<String, String>("Aquarium", "Fish Tank"));
        wordsEng.add(new Pair<String, String>("Honey", "Sugar Water"));
        wordsEng.add(new Pair<String, String>("Joy", "Excitement"));
        wordsEng.add(new Pair<String, String>("Violin", "Viola"));
        wordsEng.add(new Pair<String, String>("Donut", "Bagel"));
        wordsEng.add(new Pair<String, String>("Concert", "Recital"));
        wordsEng.add(new Pair<String, String>("Ignorant", "Stupid"));
        /* wordsEng.add(new Pair<String, String>("", ""));
        wordsEng.add(new Pair<String, String>("", ""));
        wordsEng.add(new Pair<String, String>("", ""));
        wordsEng.add(new Pair<String, String>("", ""));
        wordsEng.add(new Pair<String, String>("", ""));
        wordsEng.add(new Pair<String, String>("", "")); */
    }

    public void createChineseList(){
        wordsCh = new ArrayList<Pair<String, String>>();
        wordsCh.add(new Pair<String, String>("苍蝇", "蚊子"));
        wordsCh.add(new Pair<String, String>("苹果", "橘子"));
        wordsCh.add(new Pair<String, String>("床", "沙发"));
        wordsCh.add(new Pair<String, String>("果汁", "苏打"));
        wordsCh.add(new Pair<String, String>("烤箱", "微波炉"));
        wordsCh.add(new Pair<String, String>("袜子", "手套"));
        wordsCh.add(new Pair<String, String>("面条", "米饭"));
        wordsCh.add(new Pair<String, String>("春节", "中秋节"));
        wordsCh.add(new Pair<String, String>("火", "炉子"));
        wordsCh.add(new Pair<String, String>("iPhone", "Google Pixel"));
        wordsCh.add(new Pair<String, String>("碗", "盘子"));
        wordsCh.add(new Pair<String, String>("蛋黄", "蛋清"));
        wordsCh.add(new Pair<String, String>("鸡蛋", "鸭蛋"));
        wordsCh.add(new Pair<String, String>("鸡翅", "鸡腿"));
        wordsCh.add(new Pair<String, String>("母鸡", "公鸡"));
        wordsCh.add(new Pair<String, String>("树叶", "树枝"));
        wordsCh.add(new Pair<String, String>("睡衣", "袍子"));
        wordsCh.add(new Pair<String, String>("奶油", "牛奶"));
        wordsCh.add(new Pair<String, String>("星星", "月亮"));
        wordsCh.add(new Pair<String, String>("日语", "韩国话"));
        wordsCh.add(new Pair<String, String>("可爱", "漂亮"));
        wordsCh.add(new Pair<String, String>("报纸", "杂志"));
        wordsCh.add(new Pair<String, String>("明天", "昨天"));
        wordsCh.add(new Pair<String, String>("奶茶", "咖啡"));
        wordsCh.add(new Pair<String, String>("开水", "温水"));
        wordsCh.add(new Pair<String, String>("蜜蜂", "黄蜂"));
        wordsCh.add(new Pair<String, String>("雪花", "冰块"));
        wordsCh.add(new Pair<String, String>("土豆", "萝卜"));
        wordsCh.add(new Pair<String, String>("失望", "后悔"));
        wordsCh.add(new Pair<String, String>("离婚", "吵架"));
        wordsCh.add(new Pair<String, String>("胶带纸", "胶水"));
        wordsCh.add(new Pair<String, String>("气候", "天气"));
        wordsCh.add(new Pair<String, String>("围巾", "帽子"));
        wordsCh.add(new Pair<String, String>("洗发水", "肥皂"));
        wordsCh.add(new Pair<String, String>("毛巾", "纸巾"));
        wordsCh.add(new Pair<String, String>("毛衣", "夹克"));
        wordsCh.add(new Pair<String, String>("汤", "茶"));
        wordsCh.add(new Pair<String, String>("水族馆", "鱼缸"));
        wordsCh.add(new Pair<String, String>("蜂蜜", "糖水"));
        wordsCh.add(new Pair<String, String>("愉快", "兴奋"));
        wordsCh.add(new Pair<String, String>("小提琴", "中提琴"));
        wordsCh.add(new Pair<String, String>("甜甜圈", "百吉饼"));
        wordsCh.add(new Pair<String, String>("音乐会", "独奏会"));
        wordsCh.add(new Pair<String, String>("无知", "愚蠢"));
        /* wordsCh.add(new Pair<String, String>("", ""));
        wordsCh.add(new Pair<String, String>("", ""));
        wordsCh.add(new Pair<String, String>("", ""));
        wordsCh.add(new Pair<String, String>("", ""));
        wordsCh.add(new Pair<String, String>("", ""));
        wordsCh.add(new Pair<String, String>("", "")); */
    }
}
