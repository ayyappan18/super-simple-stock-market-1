package com.jpmorgan.stock.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.jpmorgan.stock.dao.StockDao;
import com.jpmorgan.stock.dao.impl.MemoryStockDao;
import com.jpmorgan.stock.model.Stock;
import com.jpmorgan.stock.model.StockType;
import com.jpmorgan.stock.model.Trade;
import com.jpmorgan.stock.service.StockService;

public class StockServiceImpl implements StockService {

  private static StockServiceImpl instance = null;

  public static StockServiceImpl getInstance() {
    if (instance == null) {
      instance = new StockServiceImpl();
    }
    return instance;
  }

  private StockDao stockDao = new MemoryStockDao();

  public void addStock(Stock stock) {
    stockDao.addStock(stock);
  }

  public Stock getStock(String symbol) {
    return stockDao.getStock(symbol);
  }

  public double calculateDividendYield(Stock stock, double price) {
    if (StockType.PREFFERED.equals(stock.getType())) {
      return (stock.getFixedDividend() * stock.getParValue()) / price;
    }
    double result = stock.getLastDividend() / price;
    return round(result, 2);
  }

  public double calculatePERatio(Stock stock, double price) {
    double result = price / stock.getLastDividend();
    return round(result, 2);
  }

  public double calculateVolumeWeightedStockPrice(List<Trade> trades) {
    double sumOfPriceQuantity = 0;
    int sumOfQuantity = 0;

    for (Trade trade : trades) {
      sumOfPriceQuantity = sumOfPriceQuantity + (trade.getPrice() * trade.getQuantity());
      sumOfQuantity = sumOfQuantity + trade.getQuantity();
    }
    double result = sumOfPriceQuantity / sumOfQuantity;
    return round(result, 2);
  }

  public double calculateGBCE(List<Trade> trades) {
    double total = 1;
    for (Trade trade : trades) {
      total = total * trade.getPrice();
    }
    double result = Math.pow(total, (1D / trades.size()));
    return round(result, 2);
  }

  public static double round(double value, int places) {
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

}
