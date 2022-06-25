package com.yoursole.HypixelSays.Data;

public class GameData {
    public static boolean isEnabled = false;
    public static boolean tellraw = false;
    public static boolean lagMode = false;
    public static boolean queueOnLoss = true;

    public static boolean inHypixelSays = false;

    public static int round = 0;
    public static int score = 0;
    public static int firstPlaceScore = 0;
    public static int secondPlaceScore = 0;

    public static void reset(){
        round = 0;
        score = 0;
        firstPlaceScore = 0;
        secondPlaceScore = 0;
    }
}
