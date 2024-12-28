package com.example.generaltemplate;

public class powerUps {
    String name;
    int cost;
    int difficultyAffect;
    int timeAffect;
    double pointAffect;
    public powerUps(String name,int cost,int difficultyAffect,int timeAffect,double pointAffect){
        this.name=name;
        this.cost=cost;
        this.difficultyAffect=difficultyAffect;
        this.timeAffect=timeAffect;
        this.pointAffect=pointAffect;
    }

    public String getName() {
        return name;
    }

    public int getTimeAffect() {
        return timeAffect;
    }

    public double getPointAffect() {
        return pointAffect;
    }

    public int getDifficultyAffect() {
        return difficultyAffect;
    }

    public int getCost() {
        return cost;
    }
}
