package com.jpmorgan.stock.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jpmorgan.stock.dao.impl.MemoryTradeDao;
import com.jpmorgan.stock.model.Stock;
import com.jpmorgan.stock.model.StockType;
import com.jpmorgan.stock.model.Trade;
import com.jpmorgan.stock.model.TradeType;

/**
 * Tests for {@code TradeDao}
 * @author nd@nathandeamer.com
 */
public class TradeDaoTest {

  private TradeDao tradeDao = null;
  private Stock stock;
  private Stock stock2;
  private Stock stock3;

  @Before
  public void setup() {
    tradeDao = new MemoryTradeDao();
    stock = new Stock("Test", StockType.COMMON, 1, 0, 100);
    stock2 = new Stock("Test2", StockType.Preferred, 1, 0, 100);
    stock3 = new Stock("Tes3", StockType.COMMON, 1, 0, 100);
  }

  @Test
  public void testAddTrade() {
    Trade trade = new Trade(stock, Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0);
    tradeDao.addTrade(trade);
    assertEquals(1, tradeDao.getAllTrades().size());
  }

  @Test
  public void testGetTrades() {
    Trade trade = new Trade(stock,
        Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0);
    tradeDao.addTrade(trade);

    Calendar c = Calendar.getInstance();
    c.add(Calendar.MINUTE, -16);
    Trade trade2 = new Trade(stock, c.getTime(), 1, TradeType.BUY, 1.0);
    tradeDao.addTrade(trade2);
    List<Trade> trades = tradeDao.getTrades(stock, 15);
    assertEquals(1, trades.size());
  }

  @Test
  public void testGetAllTrades() {

    Trade trade = new Trade(stock,
        Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0);
    tradeDao.addTrade(trade);

    Trade trade2 = new Trade(stock2,
        Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0);
    tradeDao.addTrade(trade2);

    Trade trade3 = new Trade(stock3,
        Calendar.getInstance().getTime(), 1, TradeType.BUY, 1.0);
    tradeDao.addTrade(trade3);

    assertEquals(3, tradeDao.getAllTrades().size());
  }

}
