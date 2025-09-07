package com.company.financeflowapp.trading;

import com.company.financeflowapp.api.ApiGetBuilder;
import com.company.financeflowapp.api.FetchDataAPI;
import io.fair_acc.chartfx.XYChart;
import io.fair_acc.chartfx.axes.spi.DefaultNumericAxis;
import io.fair_acc.chartfx.renderer.spi.financial.CandleStickRenderer;
import io.fair_acc.chartfx.renderer.spi.financial.FinancialTheme;
import io.fair_acc.dataset.spi.financial.OhlcvDataSet;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcv;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcvItem;
import io.fair_acc.dataset.testdata.spi.SingleOutlierFunction;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


// Class representing the candlestick chart display
public class ChartFXCandleStickManager   {
    private FinancialTheme theme = FinancialTheme.Sand;
    private XYChart chart;

    private  ApiGetBuilder apiGetBuilder;
    private CandleStickRenderer candleStickRenderer;
    private ScheduledExecutorService scheduler;
    private Label currentStockPriceLabel;

    private VBox root;
    private String timeSeries;
    private String timeInterval = "5min";
    public String symbol = "IBM";
    private String outputSize;

    /**
      Initialises the ChartFXCandleStickManager with default settings and components.
      Sets up the environment including UI components for displaying the candlestick chart.
      Initialises chart on JavaFX thread
     */

    public ChartFXCandleStickManager() {
        currentStockPriceLabel = new Label();
        currentStockPriceLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
        root = new VBox();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        Platform.runLater(this::initializeChart); // Ensure UI components are initialized on the JavaFX thread
    }

    /**
      Initializes the chart with default configurations.
      This includes setting up axis labels, configuring the chart size, and applying a financial theme.
      It also triggers the initial data fetch and subsequent periodic updates.
     */
    private void initializeChart() {
    // Initialize the chart and axis
    apiGetBuilder = new ApiGetBuilder();
    System.out.println("initialising charts");
    final DefaultNumericAxis xAxisOHLC  = new DefaultNumericAxis("Time - Us/Eastern", "HH:mm:ss");
    final DefaultNumericAxis yAxisOHLC  = new DefaultNumericAxis("Price", "Dollars $");
    this.chart = new XYChart(xAxisOHLC, yAxisOHLC);
    this.chart.setPrefSize(600,600);
    theme.applyPseudoClasses(chart);
    // Initialize renderer
    prepareCandleStickRenderer(chart);
    fetchInitialDataAndThenStartPeriodicUpdates(chart);

    System.out.println("about to add chart to root");

    this.root.getChildren().addAll(chart,currentStockPriceLabel);
}
    /**
      Prepares and configures the CandleStickRenderer for the chart.
      This involves adding the renderer to the chart for displaying OHLC data.

      chart - The XYChart object to which the renderer is added.
     */
    private void prepareCandleStickRenderer(XYChart chart) {
        // Create and configure the candleStickRenderer
        candleStickRenderer = new CandleStickRenderer();

        // Add the candleStickRenderer to the chart
        this.chart.getRenderers().add(candleStickRenderer);
    }

    public Node getChartNode() {
        return this.root; // Return the VBox containing the chart
    }

    /**
      Fetches initial data and starts periodic updates using classes ApiGetBuilder and FetchDataAPI
      Sets up the chart with real-time data fetching by configuring the API calls.

       chart - The XYChart object to be updated with fetched data.
     */
    private void fetchInitialDataAndThenStartPeriodicUpdates(XYChart chart) {
        System.out.println("Fetching initial data");
        ApiGetBuilder builder = new ApiGetBuilder();
        FetchDataAPI fetchDataAPI = new FetchDataAPI();
       fetchDataAPI.setGetBuiltAPI(builder.buildDefaultApi());
       // fetchDataAPI.setGetBuiltAPI("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo");
        // Fetch initial data asynchronously, then start periodic updates
        fetchDataAPI.newFetchDataAPI(this::updateChartWithData);
                fetchDataPeriodically();
    }

    /**
      uses the classes ApigetBuilder and FetchDataAPi
      Updates the chart data for a specific stock symbol and interval.
      This method sets up the chart to reflect changes based on user input for symbol and time interval.

      symbol - The stock symbol to fetch data for.
       interval - The interval between data points (e.g., 5min, 30min).
     */
    public void updateChartForSymbol(String symbol, String interval) {
        this.symbol = symbol;
        this.timeInterval = interval;
        System.out.println("Updating chart for symbol: " + symbol + " with interval: " + interval);
        ApiGetBuilder builder = new ApiGetBuilder();
        // Calls this method in ApiGetBuilder
        String apiURL = builder.buildApiForSymbolAndInterval(symbol, interval);
        FetchDataAPI fetchDataAPI = new FetchDataAPI();
        fetchDataAPI.setSimAndInt(symbol,interval);
        fetchDataAPI.setGetBuiltAPI(apiURL);

        // Now, fetch data and update the chart
        fetchDataAPI.newFetchDataAPI(this::updateChartWithData);
        fetchDataPeriodically();
    }

    /**
      Periodically fetches data to update the chart - thread and scheduler used.
      This method is responsible for setting up a scheduler that periodically fetches new data
      to keep the chart updated.
     uses the classes FetchDataAPi

     */
    private void fetchDataPeriodically() {
        if (scheduler == null || scheduler.isShutdown()) {
            // Executor service is not running; initialize it
            scheduler = Executors.newSingleThreadScheduledExecutor();
        }
        System.out.println("Fetching data periodically...");
        FetchDataAPI fetchDataAPI = new FetchDataAPI();
       fetchDataAPI.setGetBuiltAPI(apiGetBuilder.buildApiForSymbolAndInterval(symbol,timeInterval)); //method configures the API call
//        fetchDataAPI.setGetBuiltAPI("https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=5min&apikey=demo");

        // This scheduler periodically calls newFetchDataAPI, which fetches and processes the data,
        // then directly updates the dataset with the new, processed data.
        this.scheduler.scheduleAtFixedRate(() -> {
            System.out.println("RE-FETCHING DATA NOW");
            fetchDataAPI.newFetchDataAPI(this::updateChartWithData);
        }, 1, 60, TimeUnit.SECONDS); // Initial delay of 1 second, then repeat every 60 seconds
        scheduler.close();
    }

    /**
      Stops the scheduled task that fetches data - used when loggiin button pressed.
      This method should be called when the application is about to close or when it is necessary to stop data fetching.
     */
    public void stopScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown(); // Or scheduler.shutdownNow();
            try {
                // Wait a while for existing tasks to terminate
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!scheduler.awaitTermination(60, TimeUnit.SECONDS))
                        System.err.println("Scheduler did not terminate");
                }
                System.out.println("scheduler shutting completed");
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                scheduler.shutdownNow();
                System.out.println("interupt need to sut down scheduler");
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
        }
    }
    /**
      Updates the chart with newly fetched data.
      Converts raw OHLCV data into a format suitable for rendering on the chart.
      Uses a thread the classes - YourConcreteIOhlcvItem(which uses other classes)
      it manipulates and maps datasets
     items - List of OhlcvData items representing the latest fetched data.
     */
    private void updateChartWithData(List<OhlcvData> items) {
        Platform.runLater(() -> {
                // First, clear existing datasets to remove old data from the chart
            candleStickRenderer.getDatasets().clear();

                // Convert the List<OhlcvData> into a List<IOhlcvItem>
            List<IOhlcvItem> ohlcvItems = items.stream()
                        .map(data -> new YourConcreteIOhlcvItem(
                                data.getTime(),
                                data.getOpen(),
                                data.getHigh(),
                                data.getLow(),
                                data.getClose(),
                                data.getVolume()
                        ))
                        .collect(Collectors.toList());
            ohlcvItems.forEach(item -> System.out.println(
                            "TimeStamp: " + item.getTimeStamp() +
                                    ", Open: " + item.getOpen() +
                                    ", High: " + item.getHigh() +
                                    ", Low: " + item.getLow() +
                                    ", Close: " + item.getClose() +
                                    ", Volume: " + item.getVolume()
                    ));

                // Now, update your chart dataset with this list of IOhlcvItems
            chart.setTitle("Symbol: "+ symbol);
            OhlcvDataSet dataSet = new OhlcvDataSet("Updated -" + symbol);
            CustomOhlcv customOhlcv = new CustomOhlcv(); // Assuming CustomOhlcv implements IOhlcv and can store IOhlcvItem list
               // customOhlcv.addOhlcvItem((IOhlcvItem) ohlcvItems); // Assuming a method to set the list of IOhlcvItem in CustomOhlcv
            ohlcvItems.forEach(customOhlcv::addOhlcvItem);
            dataSet.setData(customOhlcv);

                // With the  chart and renderer set up
            candleStickRenderer.getDatasets().clear();
            candleStickRenderer.getDatasets().add(dataSet);
            chart.setTitle("Symbol: "+ symbol);
            chart.updateAxisRange();
            System.out.println("data set -" + dataSet);

                // Call the method to update the current price label
            updateCurrentPriceLabel(items); // Pass the original list of OhlcvData items

        });
    }
    /**
      Updates the price lable with real time fetched data.
      Processes the list of OhlcvData items and updates the current closing price with the dataset.
     */
    private void updateCurrentPriceLabel(List<OhlcvData> items) {
        if (items.isEmpty()) {
            return; // No data to display
        }
        // Get the most recent data point
        OhlcvData latestData = items.get(items.size() - 1);
        // Update the label with the closing price of the latest data point
        double currentPrice = latestData.getClose();
        Platform.runLater(() -> currentStockPriceLabel.setText("Current Price: $" + currentPrice));

    }
}
