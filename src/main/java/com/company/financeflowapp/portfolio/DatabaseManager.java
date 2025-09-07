package com.company.financeflowapp.portfolio;



import com.company.financeflowapp.mySQL.ConnectToDB;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends ConnectToDB  {    // Methods to interact with the database


    /**
      Fetches portfolio data for a specific user from the database. This method queries the portfolio table
      to retrieve information such as stock symbol, stock ID, quantity owned, average price, and total investment
      for the logged-in user.
      return A list of PortfolioItem objects representing the user's portfolio.
     */
    public List<PortfolioItem> fetchPortfolioData() {
        System.out.println("We are in the Portfolio database manager ");
        List<PortfolioItem> portfolioItems = new ArrayList<>();

        //create a ConnectToDB object
        //using the ConnectToDB class and calling the method establishConnection()
        //ConnectToDB newConnectionObj = new ConnectToDB();
        //Becoz we extend ConnnectToDB we don't need to instantiate, just inherit and user methods instantly
        establishConnection();


        //sql query:
        try (
                //ResultSet resultSet = newConnwctionObj.runQuery("SELECT UserID, StockID, StockSymbol, " +
                ResultSet resultSet = runQuery("SELECT UserID, StockID, StockSymbol, " +
                        "QuantityOwned, AveragePrice, TotalInvestment FROM portfolio WHERE UserID = 1;")) {
            if (resultSet != null) { // had an error was because I never had this integrated, now working fine
                while (resultSet.next()) { // result set is coming us as null - fixed query, so it now works
                    System.out.println("database manager works going round ");
                    PortfolioItem item = new PortfolioItem(
                            resultSet.getString("stockSymbol"),
                            resultSet.getString("stockID"),
                            resultSet.getInt("quantityOwned"),
                            resultSet.getDouble("averagePrice"),
                            resultSet.getDouble("totalInvestment")
                    );
                    portfolioItems.add(item);
                    System.out.println("Stock Symbol - " +item.getStockSymbol());//to output nicecly you need the item...
                    System.out.println("Ave Price- " +item.getAveragePrice());
                    System.out.println("Stock ID - " +item.getStockID());
                    System.out.println("Total Investment  - " +item.getTotalInvestment());
                    System.out.println("Quantity Owned - " +item.getQuantityOwned());
                }
                    closeConnection();
            } else {
                    System.out.println("ResultSet is null. No data fetched from the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Portfolio database manager does not work");
        }
        return portfolioItems;
    }

    /**
      Closes a trade for a specific PortfolioItem by deleting it from the portfolio database table.
      This method constructs a SQL DELETE query to remove the item based on its stock ID.
      It handles SQL exceptions and ensures the database connection is properly closed after the operation.
     item - The PortfolioItem to be closed/deleted from the database.
     */
    public void closeTrade(PortfolioItem item) {
//        ConnectToDB newConnectionObj = new ConnectToDB();
//        newConnectionObj
        establishConnection();

        String deleteQuery = "DELETE FROM portfolio WHERE stockID = ?";
        try {
            int rowsAffected = executeDeleteQuery(deleteQuery, item.getStockID() );
            //TODO add the data to close the stock at the given api market rate
            System.out.println(rowsAffected + " trade(s) closed successfully.");

        } catch (Exception e){
            e.printStackTrace();
            // Handle SQL exception specifically, log it, or throw a custom exception
            // Handle other exceptions if needed
        }finally {
            closeConnection(); // ensure connection closed
        }
    }
}

