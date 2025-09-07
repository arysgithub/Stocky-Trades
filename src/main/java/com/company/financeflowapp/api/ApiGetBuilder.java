package com.company.financeflowapp.api;

/**
  Constructs API URLs for fetching financial data from the Alpha Vantage API.
  This class handles building standard API calls, including time series data,
  news data, and symbol suggestions, based on specific parameters such as
  symbols and time intervals.
 */
public class ApiGetBuilder  {
    //https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=MSFT&outputsize=full&datatype=json&interval=5min&apikey=

    // The base URL for the Alpha Vantage API
    public static final String BASE_API_URL = "https://www.alphavantage.co/query?";
    private static final String entitlement = "&entitlement=delayed";

    private static final String API_KEY = "****************";//use your own api keu
    // when the other api key is at max capacity for the day


    // Initial values for the parameters
    private String timeseries = ""; //TIME_SERIES_INTRADAY, TIME_SERIES_DAILY
    private String symbol = ""; //IBM, APPL
    private String outputsize = "";
    private String interval = ""; //1min, 5min, 15min, 30min, 60min

    // The complete API URL that will be built
    private String builtApi;
    private String builtNewsApi;
    private String newsFunction ="";
    private String newsSort ="";
    private String newsTickers="";
    private String newsTopics="";

    private String query= "";
    private String getBuildSuggestedAPI;
    private String getbuildApiForSymbolAndInterval;


    /**
      Builds the URL for fetching time series data for a given stock symbol and interval.
      symbol -The stock symbol for which to fetch the time series data.
      interval -The time interval between consecutive data points in the time series.
      The URL for the API call.
     */
    public String buildApiForSymbolAndInterval(String symbol, String interval) {
        this.symbol= symbol;
        timeseries = "TIME_SERIES_INTRADAY";
        this.interval = interval;
        outputsize = "compact";

        return BASE_API_URL +
                "function="+timeseries+
                "&symbol=" + symbol +
                "&interval=" + interval +
                "&outputsize=" + outputsize +
                entitlement+
                "&apikey=" +API_KEY;


    }
    //setter method to the build API URL from main

    //default method search
    public String buildDefaultApi() {
        // Default symbol and interval
        String defaultSymbol = "IBM"; // Example default symbol
        String defaultInterval = "5min"; // Example default interval
        return buildApiForSymbolAndInterval(defaultSymbol, defaultInterval);
    }

    /**
     Builds the URL for fetching news data based on given parameters.

     newsFunction The function to be used in the API call.
     newsTickers The tickers to be used in the API call.
     newsSort The sorting criteria for the news data.
     newsTopics The topics to be included in the news data.
     */
    public void buildNewsApi( String newsFunction, String newsTickers, String newsSort, String newsTopics) {
        //set the provided parameters
        this.newsFunction = newsFunction;
        this.newsTickers = newsTickers;
        this.newsSort = newsSort;
        this.newsTopics = newsTopics;

        // Build the complete API URL with parameters

        this.builtNewsApi = BASE_API_URL +
                "function="+newsFunction+
                "&tickers=" + newsTickers +
                "&topics=" + newsTopics +
                "&sort=" +newsSort+
                "&limit=50"+
                "&datatype=json" +
                "&apikey=" +API_KEY;


    }
    public String getNewsBuiltApi(){
        if (builtNewsApi == null) {
            //default API-URL
            this.newsFunction = "NEWS_SENTIMENT";
            this.newsTopics = "technology,economy_macro,financial_markets,economy_fiscal,economy_monetary";
            this.newsSort = "LATEST";
            this.newsTickers = "IBM,STOCKS";
            builtApi = BASE_API_URL +
                    "function="+newsFunction+
                    "&tickers=" + newsTickers +
                    "&topics=" + newsTopics +
                    "&sort=" +newsSort+
                    "&limit=50"+
                    "&datatype=json" +
                    "&apikey=" +API_KEY;
        }


        return builtApi;
    }

    public String buildSuggestionApi(String query){
        this.query= query;
        this.getBuildSuggestedAPI= BASE_API_URL +
                "function=SYMBOL_SEARCH"+
                "&keywords=" + query+
                entitlement+
                "&apikey="+API_KEY;

        return getBuildSuggestedAPI;
    }

    public String getTimeseries() {
        return timeseries;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getOutputsize() {
        return outputsize;
    }

    public String getInterval() {
        return interval;
    }

    public void setBuiltApi(String builtApi) {
        this.builtApi = builtApi;
    }

    public void setBuiltNewsApi(String builtNewsApi) {
        this.builtNewsApi = builtNewsApi;
    }




}
