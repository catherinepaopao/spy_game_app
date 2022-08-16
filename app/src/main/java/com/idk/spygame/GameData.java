package com.idk.spygame;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public static String civilianWord = "civilian word";
    public static String civilianWordtl = "civilian word translated";
    public static String spyWord = "spy word";
    public static String spyWordtl = "spy word translated";
    public static String blankGuesserMessage = "message";
    public static String blankGuesserTranslated = "translated";
    public static List<PlayerData> playerData = new ArrayList<PlayerData>();
    public static int gameMode = 0; // 0. name entry 1. voting 2. ended 3. word remind
    public static int spiesLeft = 0;
    public static int civiliansLeft = 0;
    public static boolean blankGuesser = false; // true if blank guesser is ALIVE, false if not added OR dead
    public static boolean blankGuesserHistory = false; // if blank guesser was used previously, this will be true
    public static boolean chaosMode = false;
    public static int chaosGameMode = 0; // 0 for all spy, 1 for all civ, 2 for all blank
    // public static int lastVoted = -1; // no last voted player
}
