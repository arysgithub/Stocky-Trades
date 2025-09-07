package com.company.financeflowapp.trading;

import io.fair_acc.dataset.spi.financial.api.attrs.AttributeModel;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcv;
import io.fair_acc.dataset.spi.financial.api.ohlcv.IOhlcvItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
  This class implements the IOhlcv interface, providing a custom container for OHLCV data items.
  It is designed to manage a collection of IOhlcvItem instances, offering functionalities such as
  adding new data items and retrieving specific items by index. The class supports iterating over its elements,
  enabling seamless integration with financial data processing frameworks that require custom implementations of OHLCV data storage.
  It also supports an optional AttributeModel to handle additional data attributes.

  Methods:
  - addOhlcvItem(IOhlcvItem item): Adds an OHLCV item to the internal list.
  - getOhlcvItem(int index): Retrieves an OHLCV item at the specified index from the list.
  designed to manage a list of OHLCV (Open, High, Low, Close, Volume) items and an associated attribute model:
  - size(): Returns the number of OHLCV items in the collection.
  - getAddon(): Provides access to an additional attributes model associated with the OHLCV data, if any.
  - getAddonOrCreate(): Retrieves the existing attributes model or creates a new one if none exists.
  - iterator(): Provides an iterator over the OHLCV items.
 */
public class CustomOhlcv implements IOhlcv {
    private final List<IOhlcvItem> items = new ArrayList<>();
    private AttributeModel attributeModel;


    public void addOhlcvItem(IOhlcvItem item) {
        items.add(item);
    }

    @Override
    public IOhlcvItem getOhlcvItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public AttributeModel getAddon() {
        return attributeModel;
    }

    @Override
    public AttributeModel getAddonOrCreate() {
        if (attributeModel == null) {
            attributeModel = new AttributeModel() {
                // Implement necessary methods here
            };
        }
        return attributeModel;
    }

    @Override
    public Iterator<IOhlcvItem> iterator() {
        return items.iterator();
    }
}