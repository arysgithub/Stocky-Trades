package com.company.financeflowapp.leaderboard;

/**
  Represents a user's profit score in the leaderboard.
  This class stores the username and the profit of the user. It provides methods to retrieve
  the username and profit and to represent the user profit score as a string.
 */
public class UserProfitScore  {
    String username;
    public double profit;

    public UserProfitScore(String username, double profit){
        this.username = username;
        this.profit = profit;
    }
    //get username and profit scored

    public String getUsername() {
        return username;
    }

    public double getProfit() {
        return profit;
    }
    @Override
    public String toString(){
        return username+ ":" + profit;
    }
}
