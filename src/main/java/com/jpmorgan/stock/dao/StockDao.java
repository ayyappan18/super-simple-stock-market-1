package com.jpmorgan.stock.dao;

import com.jpmorgan.stock.model.Stock;

public interface StockDao {

  void addStock(Stock stock);

  Stock getStock(String symbol);

}
