package com.jpmorgan.stock.service;

import java.util.List;

import com.jpmorgan.stock.model.Stock;
import com.jpmorgan.stock.model.Trade;

public interface TradeService {

  public void recordTrade(Trade trade);

  public List<Trade> getTrades(Stock stock, int numberOfMinutes);

  public List<Trade> getAllTrades();
}
