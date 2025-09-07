package com.company.financeflowapp.trading;

import io.fair_acc.dataset.spi.financial.api.attrs.AttributeModel;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcvItem;

import java.util.Date;

/**
  Implements the IOhlcvItem interface to represent Open-High-Low-Close-Volume (OHLCV) data points for financial time series.
  This class is a concrete implementation used specifically in the trading application to encapsulate
  the price and volume data at a specific point in time. It includes methods to access each component of the OHLCV data,
  as well as support for an optional AttributeModel for additional data attributes.
 */
public class YourConcreteIOhlcvItem implements IOhlcvItem {
    private final Date timeStamp;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;
    // Assuming open interest is not provided in OhlcvData and set to a default value
    private final double openInterest = 0;
    private AttributeModel addon;


    public YourConcreteIOhlcvItem(Date timeStamp, double open, double high, double low, double close, double volume){

        this.timeStamp = timeStamp;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }



    @Override
    public Date getTimeStamp() {
        return timeStamp;
    }

    @Override
    public double getOpen() {
        return open;
    }

    @Override
    public double getHigh() {
        return high;
    }

    @Override
    public double getLow() {
        return low;
    }

    @Override
    public double getClose() {
        return close;
    }

    @Override
    public double getVolume() {
        return volume;
    }

    @Override
    public double getOpenInterest() {
        return openInterest;
    }

    @Override
    public AttributeModel getAddon() {
        return addon;
    }

    @Override
    public AttributeModel getAddonOrCreate() {
        if (addon == null) {
            addon = new AttributeModel() {
            };
        }
        return addon;
    }
}