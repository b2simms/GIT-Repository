package com.example.bsimmons.navigation_drawer;

/**
 * Created by bsimmons on 05/06/2015.
 */
public class Game {

    private String game_id;
    private String team1;
    private String team2;
    private String day;
    private String time;
    private String location;
    private String date;

    public String getLocation() {        return location;    }

    public void setLocation(String location) {        this.location = location;    }

    public String getDate() {        return date;    }

    public void setDate(String date) {        this.date = date;    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}



