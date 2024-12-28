package com.example.generaltemplate;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class HelloController {
    public ListView lstAccountsActive;
    public ListView lstAccounts;
    public TextField txtNewAccountName;
    public Label lblAccountError;
    public Label lblTimeLeft;
    public Label lblWordCriteria;
    public TextField txtInput;
    public ListView lstUsedWords;
    public ListView lstPowerUps;
    public Label lblPlayerTurn;
    public Button btnEasy;
    public Button btnMedium;
    public Button btnHard;
    public TabPane tab;
    public Tab tabRounds;
    public Tab tabShop;
    public Tab tabScores;
    public Label lblPlayerPoints;
    public Label lblUsed;
    public Label lblPlayerTurnShop;
    public Label lblTimeLeftShop;
    public ListView lstPowerUpsShop;
    public ListView lstCartShop;
    public Label lblPlayerCoinsShop;
    public Label lblTotalCostShop;
    public ProgressBar permTimeBar;
    public Label lblPlayerWinCoins;
    public ListView lstOwnedPowerUpsShop;
    public Label lblDifficultyAffect;
    public Label lblPointAffect;
    public ListView lstFinalScores;
    public TextField txtNumRounds;
    public ListView lstHighScores;
    public ListView lstPreviousRoundScores;
    public Label lblCurrentRound;
    public ListView lstNotifications;
    public Tab tabMenu;
    ArrayList<accounts> accountSlots = new ArrayList<>();
    ArrayList<accounts> accountsActive = new ArrayList<>();
    ArrayList<String> LetterWords3 = new ArrayList<>();
    ArrayList<String> LetterWords4 = new ArrayList<>();
    ArrayList<String> LetterWords5 = new ArrayList<>();
    ArrayList<powerUps> availablePowerUps = new ArrayList<>();
    ArrayList<powerUps> cart = new ArrayList<>();
    ArrayList<String> allTimeHighScores = new ArrayList<>();
    int playerTurn;
    int difficulty=-1;
    int wordLength;
    int numLetterCriteria;
    String letterCriteria;
    String letterCriteria2;
    String criteriaType;
    int numRounds;
    int currentRound;
    long counter;
    int countdown;
    int pointCounter;
    int currentDifficultyAffect;
    int currentTimeAffect;
    double currentPointAffect;
    String currentTimerStatus;
    public void timerFunction(){
        countdown=19;
        pointCounter=1;
        lblTimeLeftShop.setText("Time Left: "+(countdown+1));
        counter=System.nanoTime();
        new AnimationTimer(){
            @Override
            public void handle(long now) {
                if (now-counter >1000000000L){
                    counter=System.nanoTime();
                    if (currentTimerStatus.equals("rounds")){
                        lblTimeLeft.setText("Time Left: "+countdown);
                        countdown-=1;
                        if (countdown%5==0){
                            pointCounter+=1;
                        }
                        if (countdown<0 && playerTurn+1<accountsActive.size()){
                            currentTimerStatus="shopping";
                            tabRounds.setDisable(true);
                            tabShop.setDisable(false);
                            tab.getSelectionModel().select(tabShop);
                            playerTurn++;
                            countdown=19;
                            lblTimeLeftShop.setText("Time Left: 20");
                        }else if(playerTurn+1>=accountsActive.size() && countdown<0 && currentRound<numRounds){
                            currentRound+=1;
                            playerTurn=0;
                            currentTimerStatus="shopping";
                            tabRounds.setDisable(true);
                            tabShop.setDisable(false);
                            tab.getSelectionModel().select(tabShop);
                            countdown=19;
                            lblTimeLeftShop.setText("Time Left: 20");
                        } else if (playerTurn+1>=accountsActive.size() && countdown<0 && currentRound==numRounds){
                            tab.getSelectionModel().select(tabScores);
                            tabScores.setDisable(false);
                            tabRounds.setDisable(true);
                        }
                    }
                    if (currentTimerStatus.equals("shopping")){
                        lblTimeLeftShop.setText("Time Left: "+countdown);
                        countdown-=1;
                        if (countdown<0){
                            currentTimerStatus="rounds";
                            tabRounds.setDisable(false);
                            tabShop.setDisable(true);
                            tab.getSelectionModel().select(tabRounds);
                            countdown=10*difficulty-1+accountsActive.get(playerTurn).getPermTimeInc();
                            lblTimeLeft.setText("Time Left: "+(countdown+1));
                            currentDifficultyAffect=0;
                            currentTimeAffect=0;
                            currentPointAffect=1;
                            loadQuestion();
                        }
                    }
                }
            }
        }.start();
    }
    @FXML
    public void initialize(){
        availablePowerUps.add(new powerUps("+5 Seconds",5,0,5,0));
        availablePowerUps.add(new powerUps("+10 Seconds",8,0,10,0));
        availablePowerUps.add(new powerUps("1.5x Point Multiplier",6,0,0,1.5));
        availablePowerUps.add(new powerUps("3x Point Multiplier",10,0,0,3));
        availablePowerUps.add(new powerUps("Difficulty -1",10,1,0,0));
        tabRounds.setDisable(true);
        tabShop.setDisable(true);
        tabScores.setDisable(true);
        File file = new File("src/main/resources/3LetterWords.txt");
        try{
            FileReader read = new FileReader(file);
            Scanner in = new Scanner(read);
            while (in.hasNextLine()){
                LetterWords3.add(in.nextLine());
            }
        }catch(FileNotFoundException ignore){

        }
        file = new File("src/main/resources/highscores.txt");
        try{
            FileReader read = new FileReader(file);
            Scanner in = new Scanner(read);
            while (in.hasNextLine()){
                allTimeHighScores.add(in.nextLine());
            }
        }catch(FileNotFoundException ignore){

        }
        file = new File("src/main/resources/4LetterWords.txt");
        try{
            FileReader read = new FileReader(file);
            Scanner in = new Scanner(read);
            while (in.hasNextLine()){
                LetterWords4.add(in.nextLine());
            }
        }catch(FileNotFoundException ignore){

        }
        file = new File("src/main/resources/5LetterWords.txt");
        try{
            FileReader read = new FileReader(file);
            Scanner in = new Scanner(read);
            while (in.hasNextLine()){
                LetterWords5.add(in.nextLine());
            }
        }catch(FileNotFoundException ignore){

        }
        for (int i=0;i<10;i++){
            file = new File("src/main/resources/accounts/account " + i + ".txt");
            try{
                FileReader read = new FileReader(file);
                Scanner in = new Scanner(read);
                if (in.hasNextLine()){
                    String name =in.nextLine();
                    int coins = Integer.parseInt(in.nextLine());
                    int numWins = Integer.parseInt(in.nextLine());
                    int highScore=Integer.parseInt(in.nextLine());
                    int winCoins = Integer.parseInt(in.nextLine());
                    int permTimeInc = Integer.parseInt(in.nextLine());
                    accountSlots.add(new accounts(name,coins,numWins,highScore,winCoins,permTimeInc));
                }else{
                    accountSlots.add(null);
                }
            }catch(FileNotFoundException ignore){
                lstAccounts.getItems().add("Error " + i);
            }
        }
        lstAccounts.getItems().clear();
        for (int i=0;i<10;i++){
            if (accountSlots.get(i)!=null){
                lstAccounts.getItems().add(accountSlots.get(i).getAccountName());
            }else{
                lstAccounts.getItems().add("Account Slot "+(i+1));
            }
        }
        System.out.println(allTimeHighScores);
        lstHighScores.getItems().clear();
        for (int i=0;i<allTimeHighScores.size();i++){
            lstHighScores.getItems().add(allTimeHighScores.get(i));
        }
    }
    public void handleActivateAccount(ActionEvent actionEvent) {
        if (accountSlots.get(lstAccounts.getSelectionModel().getSelectedIndex())!=null && accountsActive.size()<5){
            boolean canAdd=true;
            for (accounts x: accountsActive){
                if (x.getAccountName().equals(accountSlots.get(lstAccounts.getSelectionModel().getSelectedIndex()).getAccountName())){
                    canAdd=false;
                }
            }
            if (canAdd){
                accountsActive.add(accountSlots.get(lstAccounts.getSelectionModel().getSelectedIndex()));
                lstAccountsActive.getItems().add(accountsActive.get(accountsActive.size()-1).getAccountName());
            }else{
                lblAccountError.setText("Error: Account Already Activated");
            }
        }
    }

    public void handleDeactivateAccount(ActionEvent actionEvent) {
        accountsActive.remove(lstAccountsActive.getSelectionModel().getSelectedIndex());
        lstAccountsActive.getItems().remove(lstAccountsActive.getSelectionModel().getSelectedIndex());
    }

    public void handleDeleteAccount(ActionEvent actionEvent) {
        accountSlots.set(lstAccounts.getSelectionModel().getSelectedIndex(),null);
        lstAccounts.getItems().clear();
        for (int i=0;i<10;i++){
            if (accountSlots.get(i)!=null){
                lstAccounts.getItems().add(accountSlots.get(i).getAccountName());
            }else{
                lstAccounts.getItems().add("Account Slot "+(i+1));
            }
        }
        save();
    }

    public void handleAddAccount(ActionEvent actionEvent) {
        if (!txtNewAccountName.getText().isEmpty()){
            boolean canAdd=true;
            for (accounts x: accountSlots){
                if (x!=null && x.getAccountName().equals(txtNewAccountName.getText())){
                    canAdd=false;
                }
            }
            if (canAdd){
                accountSlots.set(lstAccounts.getSelectionModel().getSelectedIndex(),new accounts(txtNewAccountName.getText()));
                lstAccounts.getItems().clear();
                for (int i=0;i<10;i++){
                    if (accountSlots.get(i)!=null){
                        lstAccounts.getItems().add(accountSlots.get(i).getAccountName());
                    }else{
                        lstAccounts.getItems().add("Account Slot "+(i+1));
                    }
                }
                save();
            }else{
                lblAccountError.setText("Error: Account With Name Exists");
            }

        }
    }
    public void save(){
        for (int i=0;i<10;i++){
            File file = new File("src/main/resources/accounts/account " + i + ".txt");
            try{
                PrintWriter out = new PrintWriter(file);
                if (accountSlots.get(i)!=null){
                    out.println(accountSlots.get(i).getAccountName());
                    out.println(accountSlots.get(i).getCoins());
                    out.println(accountSlots.get(i).getNumWins());
                    out.println(accountSlots.get(i).getHighScore());
                    out.println(accountSlots.get(i).getWinCoins());
                    out.println(accountSlots.get(i).getPermTimeInc());
                }else{
                    out.print("");
                }
                out.close();
            }catch(FileNotFoundException ignore){
                System.out.println("No File");
            }
        }
        File file = new File("src/main/resources/highscores.txt");
        try{
            PrintWriter out = new PrintWriter(file);
            for (int i=0;i<allTimeHighScores.size();i++){
                out.println(allTimeHighScores.get(i));
            }
            out.close();
        }catch(FileNotFoundException ignore){
            System.out.println("No Files");
        }
    }
    public void handleSave(ActionEvent actionEvent) {
        save();
    }

    public void handleSubmitInput(ActionEvent actionEvent) {
        boolean alreadyUsed=false;
        lblUsed.setText("");
        for (int i=0; i<accountsActive.get(playerTurn).getWordsUsed().size();i++){
            if (txtInput.getText().equals(accountsActive.get(playerTurn).getWordsUsed().get(i))){
                alreadyUsed=true;
            }
        }
        if (currentPointAffect<1){
            currentPointAffect=1;
        }
        if (!alreadyUsed) {
            if (wordLength == 3) {
                if (criteriaType.equals("Letter")) {
                    if (numLetterCriteria == 1) {
                        for (int i = 0; i < LetterWords3.size(); i++) {
                            if (txtInput.getText().equals(LetterWords3.get(i)) && txtInput.getText().contains(letterCriteria)) {
                                accountsActive.get(playerTurn).addGamePoints((int)((10 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((10 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    } else if (numLetterCriteria == 2) {
                        for (int i = 0; i < LetterWords3.size(); i++) {
                            if (txtInput.getText().equals(LetterWords3.get(i)) && txtInput.getText().contains(letterCriteria) && txtInput.getText().contains(letterCriteria2)) {
                                accountsActive.get(playerTurn).addGamePoints((int)(((15 * difficulty) / pointCounter)*currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((15 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            } else if (txtInput.getText().equals(LetterWords3.get(i)) && txtInput.getText().contains(letterCriteria) || txtInput.getText().equals(LetterWords3.get(i)) && txtInput.getText().contains(letterCriteria)) {
                                accountsActive.get(playerTurn).addGamePoints((int)(((10 * difficulty) / pointCounter)*currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((10 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    }
                } else if (criteriaType.equals("Vowel")) {
                    String[] vowels = {"a", "e", "i", "o", "u"};
                    boolean isAWord = false;
                    for (int i = 0; i < LetterWords3.size(); i++) {
                        if (txtInput.getText().equals(LetterWords3.get(i))) {
                            isAWord = true;
                        }
                    }
                    if (isAWord == true) {
                        if (numLetterCriteria == 1) {
                            boolean vowelFound=false;
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i])&&!vowelFound) {
                                    accountsActive.get(playerTurn).addGamePoints((int)(((12 * difficulty) / pointCounter)*currentPointAffect));
                                    accountsActive.get(playerTurn).setCoins(-1*(int)((12 * difficulty) / pointCounter*currentPointAffect));
                                    accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                    currentPointAffect=0;
                                    lblPointAffect.setText("Current Point Affect:---");
                                    vowelFound=true;
                                }
                            }
                        } else if (numLetterCriteria == 2) {
                            boolean got1 = false;
                            String vowel1 = "";
                            boolean got2 = false;
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i])) {
                                    got1 = true;
                                    vowel1 = vowels[i];
                                }
                            }
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i]) && !vowels[i].equals(vowel1)) {
                                    got2 = true;
                                }
                            }
                            if (got1 && got2) {
                                accountsActive.get(playerTurn).addGamePoints((int)(((15 * difficulty) / pointCounter)*currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((15 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    }
                }
            } else if (wordLength == 4) {
                if (criteriaType.equals("Letter")) {
                    if (numLetterCriteria == 1) {
                        for (int i = 0; i < LetterWords4.size(); i++) {
                            if (txtInput.getText().equals(LetterWords4.get(i)) && txtInput.getText().contains(letterCriteria)) {
                                accountsActive.get(playerTurn).addGamePoints((int) ((12 * difficulty) / pointCounter * currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((12 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect = 0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    } else if (numLetterCriteria == 2) {
                        for (int i = 0; i < LetterWords4.size(); i++) {
                            if (txtInput.getText().equals(LetterWords4.get(i)) && txtInput.getText().contains(letterCriteria) && txtInput.getText().contains(letterCriteria2)) {
                                accountsActive.get(playerTurn).addGamePoints((int) (((18 * difficulty) / pointCounter) * currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((18 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect = 0;
                                lblPointAffect.setText("Current Point Affect:---");
                            } else if (txtInput.getText().equals(LetterWords4.get(i)) && txtInput.getText().contains(letterCriteria) || txtInput.getText().equals(LetterWords4.get(i)) && txtInput.getText().contains(letterCriteria)) {
                                accountsActive.get(playerTurn).addGamePoints((int) (((12 * difficulty) / pointCounter) * currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((12 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect = 0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    }
                } else if (criteriaType.equals("Vowel")) {
                    String[] vowels = {"a", "e", "i", "o", "u"};
                    boolean isAWord = false;
                    for (int i = 0; i < LetterWords4.size(); i++) {
                        if (txtInput.getText().equals(LetterWords4.get(i))) {
                            isAWord = true;
                        }
                    }
                    if (isAWord == true) {
                        if (numLetterCriteria == 1) {
                            boolean vowelFound=false;
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i])&&!vowelFound) {
                                    accountsActive.get(playerTurn).addGamePoints((int)(((12 * difficulty) / pointCounter)*currentPointAffect));
                                    accountsActive.get(playerTurn).setCoins(-1*(int)((12 * difficulty) / pointCounter*currentPointAffect));
                                    accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                    currentPointAffect=0;
                                    lblPointAffect.setText("Current Point Affect:---");
                                    vowelFound=true;
                                }
                            }
                        } else if (numLetterCriteria == 2) {
                            boolean got1 = false;
                            String vowel1 = "";
                            boolean got2 = false;
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i])) {
                                    got1 = true;
                                    vowel1 = vowels[i];
                                }
                            }
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i]) && !vowels[i].equals(vowel1)) {
                                    got2 = true;
                                }
                            }
                            if (got1 && got2) {
                                accountsActive.get(playerTurn).addGamePoints((int) (((18 * difficulty) / pointCounter) * currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((18 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect = 0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    }
                }
            }else if (wordLength == 5) {
                if (criteriaType.equals("Letter")) {
                    if (numLetterCriteria == 1) {
                        for (int i = 0; i < LetterWords5.size(); i++) {
                            if (txtInput.getText().equals(LetterWords5.get(i)) && txtInput.getText().contains(letterCriteria)) {
                                accountsActive.get(playerTurn).addGamePoints((int)((15 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((15 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    } else if (numLetterCriteria == 2) {
                        for (int i = 0; i < LetterWords5.size(); i++) {
                            if (txtInput.getText().equals(LetterWords5.get(i)) && txtInput.getText().contains(letterCriteria) && txtInput.getText().contains(letterCriteria2)) {
                                accountsActive.get(playerTurn).addGamePoints((int)(((22 * difficulty) / pointCounter)*currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((22 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            } else if (txtInput.getText().equals(LetterWords5.get(i)) && txtInput.getText().contains(letterCriteria) || txtInput.getText().equals(LetterWords5.get(i)) && txtInput.getText().contains(letterCriteria)) {
                                accountsActive.get(playerTurn).addGamePoints((int)(((15 * difficulty) / pointCounter)*currentPointAffect));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((15 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    }
                } else if (criteriaType.equals("Vowel")) {
                    String[] vowels = {"a", "e", "i", "o", "u"};
                    boolean isAWord = false;
                    for (int i = 0; i < LetterWords5.size(); i++) {
                        if (txtInput.getText().equals(LetterWords5.get(i))) {
                            isAWord = true;
                        }
                    }
                    if (isAWord == true) {
                        if (numLetterCriteria == 1) {
                            boolean vowelFound=false;
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i])&&!vowelFound) {
                                    accountsActive.get(playerTurn).addGamePoints((int)(((12 * difficulty) / pointCounter)*currentPointAffect));
                                    accountsActive.get(playerTurn).setCoins(-1*(int)((12 * difficulty) / pointCounter*currentPointAffect));
                                    accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                    currentPointAffect=0;
                                    lblPointAffect.setText("Current Point Affect:---");
                                    vowelFound=true;
                                }
                            }
                        } else if (numLetterCriteria == 2) {
                            boolean got1 = false;
                            String vowel1 = "";
                            boolean got2 = false;
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i])) {
                                    got1 = true;
                                    vowel1 = vowels[i];
                                }
                            }
                            for (int i = 0; i < vowels.length; i++) {
                                if (txtInput.getText().contains(vowels[i]) && !vowels[i].equals(vowel1)) {
                                    got2 = true;
                                }
                            }
                            if (got1 && got2) {
                                accountsActive.get(playerTurn).addGamePoints((int)(((22 * difficulty) / pointCounter*currentPointAffect)));
                                accountsActive.get(playerTurn).setCoins(-1*(int)((22 * difficulty) / pointCounter*currentPointAffect));
                                accountsActive.get(playerTurn).getWordsUsed().add(txtInput.getText());
                                currentPointAffect=0;
                                lblPointAffect.setText("Current Point Affect:---");
                            }
                        }
                    }
                }
            }
        }else{
            lblUsed.setText(txtInput.getText() + " has already been used");
        }
        loadQuestion();
        lblPlayerPoints.setText("Player Points: "+accountsActive.get(playerTurn).getGamePoints());
        lstUsedWords.getItems().clear();
        for (String x: accountsActive.get(playerTurn).getWordsUsed()){
            lstUsedWords.getItems().add(x);
        }
    }
    public void loadQuestion(){
        double ranLength= Math.random()*10;
        if (ranLength<(1.5*difficulty+currentDifficultyAffect)){
            wordLength=3;
        }else if(ranLength>=1.5*difficulty+currentDifficultyAffect && ranLength<(difficulty*2.5+(4-difficulty)+currentDifficultyAffect)){
            wordLength=4;
        }else{
            wordLength=5;
        }
        double ranCriteria = Math.random();
        if (ranCriteria<.6){
            double ranNumLetterCriteria= Math.random();
            if (ranNumLetterCriteria<(2.5*difficulty+currentDifficultyAffect)){
                numLetterCriteria=1;
            }else{
                numLetterCriteria = 2;
            }
            String[] letters={"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
            letterCriteria = letters[(int)(Math.random()*letters.length)];
            if (numLetterCriteria==2){
                letterCriteria2 = letters[(int)(Math.random()*letters.length)];
                lblWordCriteria.setText("Word Criteria: A " + wordLength +" letter word with the letters " + letterCriteria + " and(for 2x points)/or " + letterCriteria2);
            }else{
                lblWordCriteria.setText("Word Criteria: A " + wordLength +" letter word with the letter " + letterCriteria);
            }
            criteriaType="Letter";
        }else if (ranCriteria>=.6){// && ranCriteria<.8
            double ranNumLetterCriteria= Math.random();
            if (ranNumLetterCriteria<(2.5*difficulty+currentDifficultyAffect)){
                numLetterCriteria=1;
            }else{
                numLetterCriteria = 2;
            }
            criteriaType="Vowel";
            lblWordCriteria.setText("Word Criteria: A " + wordLength +" letter word with " + numLetterCriteria + " vowels");
        }
//        }else{
//
//        }
        currentDifficultyAffect=0;
        lblDifficultyAffect.setText("Current Difficulty Affect:---");
    }
    public void handleRoundDisplay(Event event) {
        lblPlayerTurn.setText("Player Turn: "+accountsActive.get(playerTurn).getAccountName());
        lblPlayerPoints.setText("Player Points: " + accountsActive.get(playerTurn).getGamePoints());
        lblCurrentRound.setText("Current Round: "+currentRound);
        lstPowerUps.getItems().clear();
        for (ownedPowerUps x: accountsActive.get(playerTurn).getBoughtPowerUps()){
            lstPowerUps.getItems().add(x.currentPowerUp.getName() + ", #" +x.getNumOwned());
        }
        lblDifficultyAffect.setText("Overall Difficulty Affect:---");
        lblPointAffect.setText("Overall Point Affect:---");
        lstUsedWords.getItems().clear();
        for (String x: accountsActive.get(playerTurn).getWordsUsed()){
            lstUsedWords.getItems().add(x);
        }
        loadQuestion();
    }

    public void handleDifficulty(ActionEvent actionEvent) {
        if (btnEasy.isArmed()){
            difficulty=3;
            btnEasy.setTextFill(Color.RED);
            btnMedium.setTextFill(Color.BLACK);
            btnHard.setTextFill(Color.BLACK);
        }else if (btnMedium.isArmed()){
            difficulty=2;
            btnEasy.setTextFill(Color.BLACK);
            btnMedium.setTextFill(Color.RED);
            btnHard.setTextFill(Color.BLACK);
        }else if (btnHard.isArmed()){
            difficulty=1;
            btnEasy.setTextFill(Color.BLACK);
            btnMedium.setTextFill(Color.BLACK);
            btnHard.setTextFill(Color.RED);
        }
    }

    public void handleStart(ActionEvent actionEvent) {
        if (difficulty!=-1 && !txtNumRounds.getText().isEmpty()){
            numRounds=Integer.parseInt(txtNumRounds.getText());
            currentRound=1;
            for (int i=0; i<100;i++){
                int account1= (int)(Math.random()*accountsActive.size());
                int account2= (int)(Math.random()*accountsActive.size());
                accounts temp=accountsActive.get(account1);
                accountsActive.set(account1,accountsActive.get(account2));
                accountsActive.set(account2,temp);
            }
            playerTurn=0;
            currentTimerStatus="shopping";
            tab.getSelectionModel().select(tabShop);
            tabMenu.setDisable(true);
            tabShop.setDisable(false);
            for (int i=0; i<accountsActive.size();i++){
                accountsActive.get(i).getWordsUsed().clear();
                accountsActive.get(i).resetGamePoints();
            }
            timerFunction();
        }

    }

    public void handleAddToCart(ActionEvent actionEvent) {
        cart.add(availablePowerUps.get(lstPowerUpsShop.getSelectionModel().getSelectedIndex()));
        int totalCost = 0;
        lstCartShop.getItems().clear();
        for (int i=0;i<cart.size();i++){
            totalCost+=cart.get(i).getCost();
            lstCartShop.getItems().add(cart.get(i).getName());
        }
        lblTotalCostShop.setText("Total Cost: "+totalCost);
    }

    public void handleRemoveFromCart(ActionEvent actionEvent) {
        cart.remove(lstCartShop.getSelectionModel().getSelectedIndex());
        int totalCost = 0;
        lstCartShop.getItems().clear();
        for (int i=0;i<cart.size();i++){
            totalCost+=cart.get(i).getCost();
            lstCartShop.getItems().add(cart.get(i).getName());
        }
        lblTotalCostShop.setText("Total Cost: "+totalCost);
    }

    public void handleCheckOut(ActionEvent actionEvent) {
        int totalCost = 0;
        for (int i=0;i<cart.size();i++){
            totalCost+=cart.get(i).getCost();
        }
        if (totalCost<=accountsActive.get(playerTurn).getCoins()){
            accountsActive.get(playerTurn).setCoins(totalCost);
            for (int i=0;i<cart.size();i++){
                boolean owned=false;
                for (int j=0;j<accountsActive.get(playerTurn).getBoughtPowerUps().size();j++){
                    if (accountsActive.get(playerTurn).getBoughtPowerUps().get(j).getCurrentPowerUp()==cart.get(i)){
                        owned=true;
                        accountsActive.get(playerTurn).getBoughtPowerUps().get(j).incNumOwned();
                    }
                }
                if (!owned){
                    accountsActive.get(playerTurn).getBoughtPowerUps().add(new ownedPowerUps(cart.get(i),1));
                }
            }
            lblTotalCostShop.setText("Total Cost:---");
            lblPlayerCoinsShop.setText("Player Coins: " + accountsActive.get(playerTurn).getCoins());
            lstCartShop.getItems().clear();
            cart.clear();
            lstOwnedPowerUpsShop.getItems().clear();
            for (ownedPowerUps x: accountsActive.get(playerTurn).getBoughtPowerUps()){
                lstOwnedPowerUpsShop.getItems().add(x.currentPowerUp.getName() + ", #" +x.getNumOwned());
            }
        }

    }

    public void handleShopDisplay(Event event) {
        lblPlayerTurnShop.setText("Player Turn: "+accountsActive.get(playerTurn).getAccountName());
        lstPowerUpsShop.getItems().clear();
        for (int i=0; i<availablePowerUps.size();i++){
            lstPowerUpsShop.getItems().add(availablePowerUps.get(i).getName());
        }
        lstOwnedPowerUpsShop.getItems().clear();
        for (ownedPowerUps x: accountsActive.get(playerTurn).getBoughtPowerUps()){
            lstOwnedPowerUpsShop.getItems().add(x.currentPowerUp.getName() + ", #" +x.getNumOwned());
        }
        permTimeBar.setProgress(((double) accountsActive.get(playerTurn).getPermTimeInc()/20));
        lblPlayerWinCoins.setText("Player Win Coins: "+accountsActive.get(playerTurn).getWinCoins());
        lblPlayerCoinsShop.setText("Player Coins: "+ accountsActive.get(playerTurn).getCoins());
        lstCartShop.getItems().clear();
        lblTotalCostShop.setText("");
    }

    public void handleBuyPermTime(ActionEvent actionEvent) {
        if (accountsActive.get(playerTurn).getWinCoins()>0 &&accountsActive.get(playerTurn).getPermTimeInc()<20){
            accountsActive.get(playerTurn).incPermTimeInc();
            accountsActive.get(playerTurn).decWinCoins();
            permTimeBar.setProgress(((double) accountsActive.get(playerTurn).getPermTimeInc()/20));
            lblPlayerWinCoins.setText("Player Win Coins: "+accountsActive.get(playerTurn).getWinCoins());
        }
    }

    public void handleUsePower(ActionEvent actionEvent) {
        countdown+=accountsActive.get(playerTurn).getBoughtPowerUps().get(lstPowerUps.getSelectionModel().getSelectedIndex()).currentPowerUp.getTimeAffect();
        currentDifficultyAffect+=accountsActive.get(playerTurn).getBoughtPowerUps().get(lstPowerUps.getSelectionModel().getSelectedIndex()).currentPowerUp.getDifficultyAffect();
        currentPointAffect+=accountsActive.get(playerTurn).getBoughtPowerUps().get(lstPowerUps.getSelectionModel().getSelectedIndex()).currentPowerUp.getPointAffect();
        lblDifficultyAffect.setText("Current Difficulty Affect: "+currentDifficultyAffect);
        lblPointAffect.setText("Current point Affect: "+currentPointAffect);
        if (accountsActive.get(playerTurn).getBoughtPowerUps().get(lstPowerUps.getSelectionModel().getSelectedIndex()).getNumOwned()>1){
            accountsActive.get(playerTurn).getBoughtPowerUps().get(lstPowerUps.getSelectionModel().getSelectedIndex()).decNumOwned();
        }else{
            accountsActive.get(playerTurn).getBoughtPowerUps().remove(lstPowerUps.getSelectionModel().getSelectedIndex());
        }
        lstPowerUps.getItems().clear();
        for (ownedPowerUps x: accountsActive.get(playerTurn).getBoughtPowerUps()){
            lstPowerUps.getItems().add(x.currentPowerUp.getName() + ", #" +x.getNumOwned());
        }
    }

    public void handleSkipTime(ActionEvent actionEvent) {
        countdown=0;
    }
    public void handleResultsDisplay(Event event) {
        lstNotifications.getItems().clear();
        for (int i=0;i<accountsActive.size();i++){
            for(int j=0;j<accountsActive.size();j++){
                if (accountsActive.get(i).getGamePoints()>accountsActive.get(j).getGamePoints()){
                    accounts temp = accountsActive.get(j);
                    accountsActive.set(j,accountsActive.get(i));
                    accountsActive.set(i,temp);
                }
            }
        }
        for (int i=0;i<accountsActive.size();i++){
            if (accountsActive.get(i).getGamePoints()>accountsActive.get(i).getHighScore()){
                accountsActive.get(i).setHighScore(accountsActive.get(i).getGamePoints());
                lstNotifications.getItems().add(accountsActive.get(i).getAccountName() + " got a new highscore of " + accountsActive.get(i).getHighScore());
            }
        }
        for (int i=0;i<accountsActive.size();i++){
            if (!accountsActive.get(i).isDone()) {
                if (allTimeHighScores.size() < 5) {
                    if (allTimeHighScores.isEmpty()){
                        allTimeHighScores.add(accountsActive.get(i).getAccountName() + ": " + accountsActive.get(i).getGamePoints());
                        lstNotifications.getItems().add(accountsActive.get(i).getAccountName() + " is now places in " + allTimeHighScores.size() + " on all time highscores with " + accountsActive.get(i).getGamePoints() + " points");
                    }else{
                        boolean finished=false;
                        for (int j = 0; j < allTimeHighScores.size(); j++) {
                            if (!finished && accountsActive.get(i).getGamePoints() > Integer.parseInt(allTimeHighScores.get(j).substring(allTimeHighScores.get(j).indexOf(": ")+2))) {
                                allTimeHighScores.add(j, (j + 1) + "; " + accountsActive.get(i).getAccountName() + ": " + accountsActive.get(i).getGamePoints());
                                finished=true;
                                lstNotifications.getItems().add(accountsActive.get(i).getAccountName() + " is now places in " + (j + 1) + " on all time highscores with " + accountsActive.get(i).getGamePoints() + " points");
                            }
                        }
                    }
                } else {
                    for (int j = 0; j < allTimeHighScores.size(); j++) {
                        if (accountsActive.get(i).getGamePoints() > Integer.parseInt(allTimeHighScores.get(j).substring(allTimeHighScores.get(j).indexOf(": ")+2))) {
                            allTimeHighScores.set(j, (j + 1) + "; " + accountsActive.get(i).getAccountName() + ": " + accountsActive.get(i).getGamePoints());
                            lstNotifications.getItems().add(accountsActive.get(i).getAccountName() + " is now places in " + (j + 1) + " on all time highscores with " + accountsActive.get(i).getGamePoints() + " points");
                        }
                    }
                }
                accountsActive.get(i).setDone(true);
            }
        }
        lstFinalScores.getItems().clear();
        for (int i=0; i<accountsActive.size();i++){
            lstFinalScores.getItems().add((i+1) +": "+ accountsActive.get(i).getAccountName() + ", "+ accountsActive.get(i).getGamePoints() + " points");
        }
        accountsActive.get(0).gotWin();

    }
    public void handleMenuDisplay(Event event) {
        lstHighScores.getItems().clear();
        for (int i=0;i<allTimeHighScores.size();i++){
            lstHighScores.getItems().add(allTimeHighScores.get(i));
        }
        lstPreviousRoundScores.getItems().clear();
        for (int i=0; i<accountsActive.size();i++){
            lstPreviousRoundScores.getItems().add((i+1) +": "+ accountsActive.get(i).getAccountName() + ", "+ accountsActive.get(i).getGamePoints() + " points");
        }
    }

    public void handleReturnToMenu(ActionEvent actionEvent) {
        currentTimerStatus="";
        tabMenu.setDisable(false);
        tabRounds.setDisable(true);
        tab.getSelectionModel().select(tabMenu);
        for (int i=0;i<accountsActive.size();i++){
            accountsActive.get(i).setDone(false);
        }
    }
}