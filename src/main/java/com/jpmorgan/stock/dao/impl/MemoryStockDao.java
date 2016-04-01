package com.jpmorgan.stock.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.jpmorgan.stock.dao.StockDao;
import com.jpmorgan.stock.model.Stock;

public class MemoryStockDao implements StockDao {

  private Map<String, Stock> stockMap = new HashMap<String, Stock>();

  public void addStock(Stock stock) {
    stockMap.put(stock.getSymbol(), stock);
  }

  public Stock getStock(String symbol) {
    return stockMap.get(symbol);
  }

}
