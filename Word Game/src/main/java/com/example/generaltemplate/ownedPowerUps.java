package com.example.generaltemplate;

public class ownedPowerUps {
    private int numOwned =0;
    powerUps currentPowerUp;
    public ownedPowerUps(powerUps power, int numBought){
        numOwned=numBought;
        currentPowerUp=power;
    }
    public powerUps getCurrentPowerUp() {
        return currentPowerUp;
    }

    public int getNumOwned() {
        return numOwned;
    }
    public void incNumOwned(){
        numOwned+=1;
    }
    public void decNumOwned(){
        numOwned-=1;
    }
}
