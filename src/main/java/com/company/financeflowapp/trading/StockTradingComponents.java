package com.company.financeflowapp.trading;

import com.company.financeflowapp.api.ApiGetBuilder;
import com.company.financeflowapp.api.FetchDataAPI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// class representing stock trading screen
/**
 The StockTradingComponents class represents the interactive components of the stock trading UI within the application.
 It allows users to search for stock symbols, select a time series for price data, view real-time data through candlestick charts,
 and execute trades (buy/sell) based on current market data. This class manages various UI elements like buttons, sliders,
 and text fields organized in a BorderPane layout for an effective trading interface.
 */
public class StockTradingComponents  {

    private final  BorderPane view;
    private final TextField searchBar;
    private final Button  searchButton;
    private final Button buyButton;

    private ChartFXCandleStickManager candleStickChartPane;
    private Slider tradeAmountSlider;
    private Label tradeAmountLabel;

    private FetchDataAPI fetchDataAPI;
    private final Button sellButton;

    private final ComboBox<String> timeSeriesComboBox;
    private GridPane gridPane;
    public VBox searchSuggest;
    public VBox tradeAmountBox;
    private double userBalance ; // Example starting balance
    private double currentMarketPrice = 0.00; // Will be updated with the latest market price
    String selectedTimeSeries;
    //constructor to initialise components



    public StockTradingComponents( ) {
        //initialising components above
        fetchDataAPI= new FetchDataAPI();
        view = new BorderPane();
        gridPane = new GridPane();
        searchSuggest = new VBox();
        tradeAmountBox  =new VBox(20);
       // userBalance = new double();

        searchBar = new TextField();
        searchBar.setPromptText("Enter a new stock symbol");

        //initialise drop down box components
        timeSeriesComboBox = new ComboBox<>();
        timeSeriesComboBox.setPromptText("Select Your Time Series");
        timeSeriesComboBox.getItems().addAll("1min", "5min","15min", "30min", "60min");
        timeSeriesComboBox.setValue("5min");


        searchButton = new Button("Search");

        buyButton = new Button("Buy");

        tradeAmountSlider = new Slider(0, 10000, 500); // min, max, initial values
        tradeAmountLabel= new Label("Trade Amount: $1000"); //
        tradeAmountLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));

        sellButton = new Button("Sell");

        setupSlider();

        setupSearchSuggest();
        
        initializeCandleStickChart();



        // TODo For displaying buying/selling potential
// For candlestick charts, you'll integrate with a chart library
// This is a conceptual approach since actual implementation depends on the library
    // TODO    candleStickChartPane.updateChart(data); // Assume updateChart is a method to refresh the chart with new data
        setupEventHandlers();
    }

    /**
      Sets up the slider component used for selecting the amount of money to trade.
     */

    private void setupSlider() {
        tradeAmountSlider.setShowTickLabels(true);
        tradeAmountSlider.setShowTickMarks(true);
        tradeAmountSlider.setMajorTickUnit(2500);
        tradeAmountSlider.setBlockIncrement(50);

        tradeAmountSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            tradeAmountSlider.setValue(newVal.intValue()); // Snap to ticks
            tradeAmountLabel.setText("Trade Amount: $" + newVal.intValue());
        });
        tradeAmountBox.getChildren().addAll(tradeAmountLabel, tradeAmountSlider);
    }

    /**
     Sets up the search suggestion feature, showing possible stock symbols as the user types.
     */
    private void setupSearchSuggest() {
        //ListView for displaying suggestions
        ListView<String> suggestionsList = new ListView<>();
        suggestionsList.setVisible(false);

        //position and style suggestion list
        suggestionsList.setMaxHeight(250);
        suggestionsList.setStyle("-fx-background-color: white; -fx-border-color: black;"); // Example styling

        //Search event handler here
        // Listener for changes in the text field to trigger the search
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Call the API with the search query, and update the suggestionsList with results
            if (!newValue.isEmpty()) {
                //populate suggestion list based on new value
                fetchDataAPI.fetchSuggestions(newValue, suggestionsList);
                suggestionsList.setVisible(true);//show suggest
            } else {
                suggestionsList.getItems().clear();
                suggestionsList.setVisible(false);
            }
            // Position the suggestions list directly below the searchBar
            // Adjust layoutX and layoutY on UI view
            Point2D p = searchBar.localToScene(0.0, 0.0);
            suggestionsList.setLayoutX(p.getX());
            suggestionsList.setLayoutY(p.getY() + searchBar.getHeight());
            System.out.println("listView and Listner Success");
        });

        // Handle selection from suggestions
        suggestionsList.setOnMouseClicked(event -> {
            String selectedItem = suggestionsList.getSelectionModel().getSelectedItem();
            if (selectedItem != null && !selectedItem.isEmpty()) {
                String selectedSymbol = selectedItem.split(" - ")[0]; // Extract the symbol
                searchBar.setText(selectedSymbol); //update searchBar to what user selected
                suggestionsList.setVisible(false); // Hide suggestions after selection
                onSymbolSelected(selectedSymbol); // go to this method for to begin updating charts
            }
        });
        searchSuggest.getChildren().addAll(searchBar,suggestionsList);

    }

    /*
     Instantiate the ChartFXCandleStickManager which automatically sets up the chart
     Uses a method within ChartFXCandleStickManager to get the chart as a Node
     */
    public void initializeCandleStickChart() {
        candleStickChartPane = new ChartFXCandleStickManager();
        Node chartNode = candleStickChartPane.getChartNode(); // returns the Candlestick chart
        if (chartNode == null) {
            System.out.println("Chart node is null.");
        } else {
            System.out.println("Chart node is not null.");
        }
        setupLayout(chartNode); //pass to method to be added to the gridPane
    }


//    private void updateUserBalanceLabel() {
//        userBalanceLabel.setText(String.format("Balance: $%.2f", userBalance));
//    }

    //setting layout of stock trading screen
    private void setupLayout(Node chartNode) {
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setGridLinesVisible(false);
        gridPane.setPrefSize(800,450);
        gridPane.add(chartNode, 2, 3);

        HBox searchLay = new HBox();
        searchLay.getChildren().addAll(searchSuggest,timeSeriesComboBox,searchButton);
        gridPane.add(searchLay, 2, 0);

      //  gridPane.add(currentStockPrice, 2,5);
        gridPane.add(tradeAmountBox,2,7);
        gridPane.add(buyButton, 2, 9);
        gridPane.add(sellButton, 2, 10);

        view.setCenter(gridPane);
    }
    private void setupEventHandlers(){//Stock Search and Filter Implementation

        timeSeriesComboBox.setOnAction(e->{
             selectedTimeSeries = timeSeriesComboBox.getValue();
            if (selectedTimeSeries==null || selectedTimeSeries.isEmpty()) {
                System.out.println("no selected time series so default series implemented");
                selectedTimeSeries = "5min";
            }else {
                System.out.println("Time series is" +selectedTimeSeries );
            }
        });

        searchButton.setOnAction(e -> {
            String symbol = searchBar.getText().split(" - ")[0]; //Inorder to extract symbol
            onSymbolSelected(symbol);
        });


        //slider priciing this is causing problems with its listener feature

//        tradeAmountSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            tradeAmountLabel.setText(String.format("Trade Amount: $%.2f", newValue.doubleValue()));
//        });
        // Add functionality to the buy and sell buttons
        // Display the current stock price above the button
        buyButton.setOnAction(e -> {
            // Dummy buy action
            executeTrade(true, tradeAmountSlider.getValue());
            System.out.println("Buying stocks..."+"selectedStockPrice");
        });

        sellButton.setOnAction(e -> {
                    // Dummy sell action
            executeTrade(false, tradeAmountSlider.getValue());
                    System.out.println("Selling stocks..."+ "selectedStockPrice");
        });


    }

    private void onSymbolSelected(String symbol) {
        System.out.println("ACCESSED ON SYMBOL SELECTED -"+symbol);
        // Determine the time interval for fetching stock data.
        // If no interval is explicitly selected in the timeSeriesComboBox, default to "5min".
        String interval = timeSeriesComboBox.getValue() == null ? "5min" : timeSeriesComboBox.getValue();

        // Update the candlestick chart with the new symbol and interval.
        // This involves calling the ChartFXCandleStickManager's method to fetch new data
        // for the specified symbol and interval, and then updating the chart to display this data.
        candleStickChartPane.updateChartForSymbol(symbol, interval);
      //  candleStickManager.stopScheduler();
    }

    //TODO
    public void executeTrade(boolean isBuying, double tradeAmount) {
        // You'll need to implement the trade logic, possibly communicating with an API or a database
        String tradeType = isBuying ? "bought" : "sold";
        System.out.println("You have " + tradeType + " stocks for $" + tradeAmount);
    }

    public BorderPane getView(){
        return view;
    }
}
