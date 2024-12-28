package com.example.generaltemplate;

import java.util.ArrayList;

public class accounts {
    int coins;
    int numWins;
    int winCoins;
    int permTimeInc;
    int highScore;
    int gamePoints;
    boolean done = false;
    String accountName;
    ArrayList<ownedPowerUps> boughtPowerUps = new ArrayList<>();
    ArrayList<String> wordsUsed = new ArrayList<>();
    public accounts(String inputName){
        accountName = inputName;
        coins=0;
        numWins=0;
        highScore=0;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public accounts(String inputName, int coins, int numWins, int highScore,int winCoins,int permTimeInc){
        accountName = inputName;
        this.coins=coins;
        this.numWins=numWins;
        this.highScore=highScore;
        this.winCoins=winCoins;
        this.permTimeInc=permTimeInc;
    }
    public void incPermTimeInc(){
        permTimeInc+=2;
    }
    public void decWinCoins(){
        winCoins-=1;
    }
    public ArrayList<String> getWordsUsed(){
        return wordsUsed;
    }

    public int getGamePoints() {
        return gamePoints;
    }
    public void gotWin(){
        numWins+=1;
        winCoins+=1;
    }
    public int getPermTimeInc() {
        return permTimeInc;
    }
    public void setCoins(int amt){
        coins-=amt;
    }
    public int getWinCoins() {
        return winCoins;
    }
    public void resetGamePoints(){
        gamePoints=0;
    }
    public ArrayList<ownedPowerUps> getBoughtPowerUps() {
        return boughtPowerUps;
    }


    public String getAccountName() {
        return accountName;
    }

    public int getCoins() {
        return coins;
    }

    public int getHighScore() {
        return highScore;
    }
    public void setHighScore(int newScore){
        highScore=newScore;
    }
    public int getNumWins() {
        return numWins;
    }
    public void addGamePoints(int amt){
        gamePoints+= amt;
    }
}
