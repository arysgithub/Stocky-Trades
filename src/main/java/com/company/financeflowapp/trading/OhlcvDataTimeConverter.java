package com.company.financeflowapp.trading;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
  Utility class to convert LocalDateTime to Date and then create OhlcvData objects.
  This class serves as a helper to facilitate the transformation of time data types
  used in financial applications where charting libraries may require data in specific formats.

  Methods:
  - convertToOhlcvData: Converts a given LocalDateTime and financial metrics into an OhlcvData object,
    ensuring compatibility with systems or APIs that require Date objects or specific representations of time.
 */
public class OhlcvDataTimeConverter {


    public static OhlcvData convertToOhlcvData(LocalDateTime time, double open, double high, double low, double close, long volume) {
        // Convert LocalDateTime to Date
        Date date = Date.from(time.atZone(ZoneId.systemDefault()).toInstant());

        // Now you can use the date in the OhlcvData constructor
        return new OhlcvData(date, open, high, low, close, volume);
    }
}
