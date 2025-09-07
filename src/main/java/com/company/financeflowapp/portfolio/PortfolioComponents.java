package com.company.financeflowapp.portfolio;

import com.company.financeflowapp.mySQL.ConnectToDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.sql.ResultSet;
import java.util.List;


public class PortfolioComponents {
    //components of stock trading screen that are connected to their own class
    private final BorderPane view;
    private TableView<PortfolioItem> portfolioTable; // tableview to display portfolio items
    private PieChart assetsPieChart; ///visualise distribution of assets

    private Button closeButton;
    // Other UI components and database interaction variables

    public PortfolioComponents() {

        view = new BorderPane();
        // Initialize UI components and set up event handlers
        initializePortfolioTable();
        initializeAssetsPieChart(); //TODo make sure initialise PieChart
         initializeCloseButton();
        // Initialize database connection and retrieve initial portfolio data
        initializeDatabase();
       updatePieChart(); //updates pie chart with initial data, also call this where any portfolio data changes
    }

    /**
      Initializes the TableView for displaying portfolio items. Sets up columns and their properties,
      including cell value factories for mapping data to the UI. Adds the TableView to the UI layout.
     */
    private void initializePortfolioTable() {
        portfolioTable = new TableView<>(); //Ui component to display tabular portfolio data

        // Table Column for the "Stock ID" field
        TableColumn<PortfolioItem, String> stockIdColumn = new TableColumn<>("Stock ID");
        // Set up how the data for this column should be populated
        // PropertyValueFactory is used to specify how to retrieve data for each cell
        // Here, it expects a property named "stockID" in the PortfolioItem class
        // The PropertyValueFactory will use the getStockID() method to retrieve the data
        stockIdColumn.setCellValueFactory(new PropertyValueFactory<>("stockID"));

        // Set other properties and cell value factories as needed
        // to Disable column reordering
        stockIdColumn.setReorderable(false);

        TableColumn<PortfolioItem, String> stockSymbolColumn = new TableColumn<>("Stock Symbol");
        stockSymbolColumn.setCellValueFactory(new PropertyValueFactory<>("stockSymbol"));
        stockSymbolColumn.setReorderable(false);

        TableColumn<PortfolioItem, Integer> quantityOwnedColumn = new TableColumn<>("Quantity Owned");
        quantityOwnedColumn.setCellValueFactory(new PropertyValueFactory<>("quantityOwned"));
        quantityOwnedColumn.setReorderable(false);

        TableColumn<PortfolioItem, Double> averagePriceColumn = new TableColumn<>("Average Price");
        averagePriceColumn.setCellValueFactory(new PropertyValueFactory<>("averagePrice"));
        averagePriceColumn.setReorderable(false);

        TableColumn<PortfolioItem, Double> totalInvestmentColumn = new TableColumn<>("Total Investment");
        totalInvestmentColumn.setCellValueFactory(new PropertyValueFactory<>("totalInvestment"));
        totalInvestmentColumn.setReorderable(false);
        // Add other columns as needed

        // Add the defined columns to the TableView
        // This sets up the structure of the table, specifying which columns will be displayed and in what order
        portfolioTable.getColumns().addAll(stockIdColumn, stockSymbolColumn, quantityOwnedColumn, averagePriceColumn, totalInvestmentColumn);

        // Add portfolioTable to the layout
        view.setBottom(portfolioTable);
    }

    /**
     Initializes the PieChart for visualizing the distribution of assets in the portfolio.
     Configures chart properties and adds it to the UI layout. Calls updatePieChart to populate
     initial data.
     */
    private void initializeAssetsPieChart() {
        System.out.println("Initial is now working");
        assetsPieChart = new PieChart(); // Initialize the PieChart object
        assetsPieChart.setTitle("Portfolio Distribution"); // Set a title for the chart

        // Optional: Customize the pie chart further if needed
        assetsPieChart.setLegendSide(Side.BOTTOM); // Set the legend position
        assetsPieChart.setLabelLineLength(10); // Set the line length from the pie chart to the label
        assetsPieChart.setClockwise(true); // Set the direction of the slices in the pie chart

        updatePieChart(); // Call this method to populate the pie chart with initial data

        view.setLeft(assetsPieChart); // Add the pie chart to your UI, adjust layout as needed

    }

    /**
      Updates the PieChart with current data from the portfolio. Fetches data from the database
      and calculates proportions for the chart. Sets the data in the PieChart to reflect the current
      portfolio distribution.
     */
    private void updatePieChart() {
        // Create an instance of DatabaseManager to fetch portfolio data
        DatabaseManager databaseManager = new DatabaseManager();
        List<PortfolioItem> portfolioItems = databaseManager.fetchPortfolioData();

        // Create an ObservableList to hold the data for the pie chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        // Calculate the total investment in the portfolio to determine each item's proportion
        double totalInvestment = portfolioItems.stream().mapToDouble(PortfolioItem::getTotalInvestment).sum();

        // Loop through each PortfolioItem, creating a new PieChart.Data object for each
        for (PortfolioItem item : portfolioItems) {
            // The proportion of the total investment is calculated for each item
            double proportion = item.getTotalInvestment() / totalInvestment;
            // Add the item to the pie chart data, labeling it with the stock symbol
            pieChartData.add(new PieChart.Data(item.getStockSymbol(), proportion));
        }

        // Set the pie chart's data to the newly created list of PieChart.Data objects
        assetsPieChart.setData(pieChartData);
        // Optionally, you can add more customization here, like setting a title
    }

    /**
      Initializes the close button for trades. Sets up the button and its event handler which triggers
      the closure of selected trades in the portfolio.
     */
    private void initializeCloseButton() {
        closeButton = new Button("Close Trade");
        closeButton.setOnAction(event -> {
            // Connect to the database and fetch initial portfolio data
            //TODO set the open trading stock pair to close once button pressed
            handleCloseTrade();
            DatabaseManager databaseManager = new DatabaseManager();
            //List<PortfolioItem> portfolioItems = databaseManager.closeTrade( need the trade to close);

        });
        // Add closeButton to the layout
    }

    /**
      Establishes a connection to the database using databaseManager class and fetches initial portfolio data to populate the UI.
      This method is called during initialization to ensure the UI reflects the current database state.
     */
    private void initializeDatabase() {
        System.out.println("Initialize DB in session");
        DatabaseManager databaseManager = new DatabaseManager();
        List<PortfolioItem> portfolioItems = databaseManager.fetchPortfolioData();
        // Update UI with fetched data
        portfolioTable.getItems().addAll(portfolioItems);
    }

    /**
      Handles the action of closing a trade from the portfolio. This involves database operations
      to update the trade status and refreshing the UI to reflect these changes.
     */
    private void handleCloseTrade() {
        // Handle the closing of a trade
        // Update database and refresh UI accordingly
        PortfolioItem selectedItem = portfolioTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.closeTrade(selectedItem);
            // Refresh UI after closing the trade
            portfolioTable.getItems().remove(selectedItem);
            // Update other UI components as needed
        }
    }

      //Returns the main view component of this class.

    public BorderPane getView() {
        return view;
    }

}

