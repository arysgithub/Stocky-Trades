package com.company.financeflowapp.trading;

import io.fair_acc.dataset.spi.financial.api.attrs.AttributeModel;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcvItem;

import java.util.Date;

/**
  Represents OHLCV (Open, High, Low, Close, Volume) data for a given point in time.
  This class encapsulates all relevant financial data necessary for creating and
  managing OHLCV items which are typically used in financial charting.

  Methods include the ability to convert this data into an IOhlcvItem format,
  which can then be used directly in charting libraries that require data in
  the IOhlcvItem format. This is essential for integrating with complex financial
  visualization tools that rely on a standardized interface for data items.
 */
public class OhlcvData {
    public String builtOhlcvData;  // String representation of the OHLCV data for easy logging/debugging
    private Date time; // The timestamp for this data point.
    private double open; //The opening price.
    private double high; // The highest price during the interval.
    private double low; //  The lowest price during the interval.
    private double close; // The closing price at the end of the interval.
    private long volume; // The trading volume during the interval.

    // Constructor
    public OhlcvData(Date time, double open, double high, double low, double close, long volume) {
        this.time = time;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;

        new YourConcreteIOhlcvItem(time, open, high, low, close, volume);

        // Build a string representation for logging or debugging purposes
        this.builtOhlcvData =
                "time: " + time + " open: " + open + " high: " + high+
                        " low: " + low +
                        " close: " + close +
                        " volume: " + volume;

    }

    // Method to convert this OhlcvData into an IOhlcvItem
    /*
    The line return new YourConcreteIOhlcvItem(time, open, high, low, close, volume); instantiates a new
    object of the class YourConcreteIOhlcvItem, passing it the parameters necessary to represent an OHLCV data item.
     This implementation stores the provided OHLCV data internally and provide
     methods to access these values, as defined by the IOhlcvItem interface.

      Converts this data object into an IOhlcvItem, which is required for compatibility with
      financial charting libraries that operate on the IOhlcv interface.
      s an instance of IOhlcvItem containing this object's data.

     Purpose:
     - The purpose of this line is to convert a more generic data representation (OhlcvData) into a specific format
       (IOhlcvItem) that your charting or data processing system can work with directly.
       This conversion makes it easier to integrate with libraries or frameworks expecting a certain interface (IOhlcvItem),
        facilitating the separation between data fetching and data visualization or processing.
     */
    public IOhlcvItem toIOhlcvItem() {
        // Assuming YourConcreteIOhlcvItem exists and can be initialized with OhlcvData's fields
        return new YourConcreteIOhlcvItem(time, open, high, low, close, volume);
    }
    public String getBuiltOhlcvData(){
        return builtOhlcvData;
    }

    // Getters
    public Date getTime() {
        return time;
    }

    public double getOpen() {
        return open;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setClose(double close) {
        this.close = close;
    }
    public double getHigh() {
        return high;
    }
    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }
    public long getVolume() {
        return volume;
    }

}
