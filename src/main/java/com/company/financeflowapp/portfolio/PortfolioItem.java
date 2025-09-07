package com.company.financeflowapp.portfolio;

public class PortfolioItem {
    private String stockID;
    private int quantityOwned;
    private double averagePrice;
    private double totalInvestment;
     String stockSymbol;

    /**
     Constructor for creating a new PortfolioItem object with specified parameters.
     Initializes the portfolio item with stock details.

     stockSymbol - The symbol representing the stock.
     stockID-  The unique identifier for the stock.
     quantityOwned - The number of shares owned.
     averagePrice  -The average purchase price of the stock.
     totalInvestment  -The total value invested in the stock.
     */
    public PortfolioItem( String stockSymbol, String stockID, int quantityOwned, double averagePrice, double totalInvestment) {
        this.stockID = stockID;
        this.quantityOwned = quantityOwned;
        this.averagePrice = averagePrice;
        this.totalInvestment = totalInvestment;
        this.stockSymbol = stockSymbol;
    }

    // Getters and setters for the PortfolioItem attributes
    public String getStockID() {
        return stockID;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    //TODO add validation so quantities and prices are not negative
    public int getQuantityOwned() {
        return quantityOwned;
    }

    public void setQuantityOwned(int quantityOwned) {
        this.quantityOwned = quantityOwned;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(double totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
}