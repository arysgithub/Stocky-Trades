package com.company.financeflowapp.api;


//import com.company.financeflowapp.news.StockNews;
import com.company.financeflowapp.trading.OhlcvData;
import com.company.financeflowapp.trading.OhlcvDataTimeConverter;
import com.company.financeflowapp.trading.YourConcreteIOhlcvItem;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcvItem;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public  class FetchDataAPI {

    String getBuiltAPI;
    public String timeInterval ="5min";

    public String symbol = "IBM";
    public ApiGetBuilder apiGetBuilder;

    /**
      Sets the API endpoint to be used for fetching data.
     getBuiltAPI The endpoint of the API from which to fetch data.
     */
    public void setGetBuiltAPI(String getBuiltAPI){
        this.getBuiltAPI = getBuiltAPI;
    }


    /**
     Method to start data fetch asynchronously and process it using the given callback (Consumer<String> to handle the data once fetched.)
     So it Fetches financial data asynchronously and processes it into a list of OhlcvData objects.
     The processed data is then consumed by the provided onDataFetched consumer.
     onDataFetched A Consumer functional interface to handle the list of OhlcvData objects after fetching and processing.
     */
    public void newFetchDataAPI(Consumer<List<OhlcvData>> onDataFetched) {
        // Implementation that fetches data, processes it into a List<OhlcvData>, and then calls onDataFetched.accept(list)

        // Task<> handles background tasks concurrently with JavaFX threads & produces a String when completed
        Task<String> fetchDataTask = new Task<String>() { // handles background tasks concurrently with treads
            @Override
            protected String call() throws Exception {
                String urlString = getBuiltAPI;
                StringBuilder result = new StringBuilder();
                try {
                    // create URL object and fetch data from API
                    URL url = new URL(urlString);
                    //Open a connection to URL
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET"); //set request method to GET to request data

                    int responseCode = conn.getResponseCode();// Get HTTP response code

                    //checks if HTTP Status-Code HTTP_OK(200) :means OK.
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        result.append(line);
                    }
                    rd.close();
                    return result.toString(); // Successfully fetched data
                    } else {
                        //if response code is not OK, return an error message
                        return "Error: " + responseCode;
                    }
                } catch (Exception e) {
                    System.out.println("Error fetching data: " + e.getMessage());
                    return null; // Indicate failure
                }
            }
        };
// Define what to do when the data fetching is successfully completed
        fetchDataTask.setOnSucceeded(event -> {
            System.out.println("Event on Success responce");
            String rawData  = fetchDataTask.getValue();
            //System.out.println(rawData);//response to console.
            if (rawData != null) {
                System.out.println("Doesnt = null");
                List<OhlcvData> processedData = processData(rawData); // Assume this converts raw data to a list
                Platform.runLater(() -> {
                    onDataFetched.accept(processedData); // Now passing processed data to the consumer
                });
                //   List<OhlcvData> ohlcvDataList = processData(data);
            }
        });

        // Define what to do in case of failure during data fetching
        fetchDataTask.setOnFailed(event -> {
            Throwable exception = fetchDataTask.getException();
            System.out.println("Failed - Error occurred during data fetching: " + exception.getMessage());
        });

        // Start the Task in a new thread
        new Thread(fetchDataTask).start();
    }


    /**
      Processes the raw JSON string data fetched from the API into a list of OhlcvData objects.
      data The raw JSON string data fetched from the API.
      A list of OhlcvData objects representing the processed financial data.
     */
    public List<OhlcvData> processData(String data) {
        System.out.println("Begining to process API data");
        List<OhlcvData> ohlcvDataArrayList = new ArrayList<>();
        // Convert the List<OhlcvData> into a List<IOhlcvItem> using Java Streams

        if (data == null || data.isEmpty()) {
            System.out.println("NOT processing Data is null or empty");
            return ohlcvDataArrayList; // Return empty list or handle this scenario as appropriate
        }

        try {
            JSONObject jsonObject = new JSONObject(data); // Parse the raw JSON string into a JSONObject for easy data access.
            /*
             Extract the object that contains the time series data. Each key within this object
             represents a specific datetime, and the value is another JSONObject with the stock's
              financial data (open, high, low, close, volume) for that time.
             */
            JSONObject timeSeries = jsonObject.getJSONObject("Time Series ("+timeInterval+")");

            /*
             Iterate over each datetime key in the time series. The keys() method returns an iterator
             of keys, and forEachRemaining is a Java 8 method that allows iterating over each element.
             For each key (datetime), extract the corresponding JSONObject which contains
             the stock's financial data for that time.
            */
            timeSeries.keys().forEachRemaining(key -> {
                JSONObject entry = timeSeries.getJSONObject(key);
                /*
                converts to right format for parsing data in strutured manner
                 Extract each piece of financial data from the JSONObject:date open, high, low,
                 close prices, and volume. These are typical components of OHLCV data in finance.
                 */
                LocalDateTime dateTime = LocalDateTime.parse(key, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                double open = entry.getDouble("1. open");
                double high = entry.getDouble("2. high");
                double low = entry.getDouble("3. low");
                double close = entry.getDouble("4. close");
                long volume = entry.getLong("5. volume");

                /*
                 Use the converter method
                OhlcvDataTimeConverter - Convert LocalDateTime to Date in Java defined method
                So Now you can use the date in the OhlcvData constructor

                 */
                OhlcvData ohlcvData = OhlcvDataTimeConverter.convertToOhlcvData(dateTime, open, high, low, close, volume);
               System.out.println("ohlcvData - "+ohlcvData.getBuiltOhlcvData());
                //add to arraylist for further processing
                ohlcvDataArrayList.add(ohlcvData);
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing JSON data: " + e.getMessage());
            // Handle error accordingly
            return null;
        }
       return ohlcvDataArrayList;
    }

    /**
      Fetches stock symbol suggestions based on a user query.
      Updates a ListView with these suggestions.

      query The user input used to fetch symbol suggestions.
      suggestionsList The ListView to be updated with the suggestions.
     */
    public void fetchSuggestions(String query, ListView<String> suggestionsList) {
        // Implement API call to Alpha Vantage SYMBOL_SEARCH endpoint
        apiGetBuilder = new ApiGetBuilder();
        // Build the URL for fetching suggestions based on the user's query
        String apiUrl = apiGetBuilder.buildSuggestionApi(query);;

        // Execute the API call on a separate thread to avoid freezing the UI
        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                // Open a connection to the specified URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String response = new BufferedReader(new InputStreamReader(connection.getInputStream())).
                            lines().collect(Collectors.joining());

                    // Parse the JSON response to get suggestions already processing data
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray bestMatches = jsonObject.getJSONArray("bestMatches");
                    List<String> symbols = new ArrayList<>();
                    for (int i = 0; i < bestMatches.length(); i++) {
                        JSONObject match = bestMatches.getJSONObject(i);
                        String symbol = match.getString("1. symbol");
                        String name = match.getString("2. name");
                        symbols.add(symbol + " - " + name);
                    }

                    // Update the ListView with suggestions on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        System.out.println("Perfect- Search suggest working ");
                        suggestionsList.getItems().setAll(symbols);
                        suggestionsList.setVisible(true);
                    });
                } else {
                    System.out.println("Error fetching suggestions: " + responseCode);

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                System.out.println("Error fetching suggestions: " + e.getMessage());
            }
        }).start();
    }

    public void setSimAndInt(String symbol, String interval) {
        this.timeInterval= interval;
        this.symbol = symbol;

    }
}
