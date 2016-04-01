package com.jpmorgan.stock.dao;

import java.util.List;

import com.jpmorgan.stock.model.Stock;
import com.jpmorgan.stock.model.Trade;

public interface TradeDao {

  void addTrade(Trade trade);

  List<Trade> getTrades(Stock stock, int minutes);

  List<Trade> getAllTrades();

}
