package com.jpmorgan.stock.service;

import java.util.List;

import com.jpmorgan.stock.model.Stock;
import com.jpmorgan.stock.model.Trade;

public interface StockService {

  public void addStock(Stock stock);

  public Stock getStock(String symbol);

  public double calculateDividendYield(Stock stock, double price);

  public double calculatePERatio(Stock stock, double price);

  public double calculateVolumeWeightedStockPrice(List<Trade> trades);

  public double calculateGBCE(List<Trade> trades);
}
